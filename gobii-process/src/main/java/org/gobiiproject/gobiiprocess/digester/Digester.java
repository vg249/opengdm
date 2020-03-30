package org.gobiiproject.gobiiprocess.digester;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.FileAppender;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.gobiiproject.gobiiapimodel.restresources.gobii.GobiiUriFactory;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContext;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.GobiiCropConfig;
import org.gobiiproject.gobiimodel.config.RestResourceId;
import org.gobiiproject.gobiimodel.cvnames.JobProgressStatusType;
import org.gobiiproject.gobiimodel.dto.instructions.loader.DigestResults;
import org.gobiiproject.gobiimodel.dto.instructions.loader.DigesterConfig;
import org.gobiiproject.gobiimodel.dto.instructions.loader.LoaderInstructionFilesDTO;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiFile;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderInstruction;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderMetadata;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderProcedure;
import org.gobiiproject.gobiimodel.dto.instructions.loader.IFLLineCounts;
import org.gobiiproject.gobiimodel.types.DatasetOrientationType;
import org.gobiiproject.gobiimodel.types.GobiiAutoLoginType;
import org.gobiiproject.gobiimodel.types.GobiiFileType;
import org.gobiiproject.gobiimodel.types.ServerType;
import org.gobiiproject.gobiimodel.utils.FileSystemInterface;
import org.gobiiproject.gobiimodel.utils.HelperFunctions;
import org.gobiiproject.gobiimodel.utils.InstructionFileValidator;
import org.gobiiproject.gobiimodel.utils.LineUtils;
import org.gobiiproject.gobiimodel.utils.SimpleTimer;
import org.gobiiproject.gobiimodel.utils.email.DigesterProcessMessage;
import org.gobiiproject.gobiimodel.utils.email.MailInterface;
import org.gobiiproject.gobiiprocess.HDF5Interface;
import org.gobiiproject.gobiiprocess.JobStatus;
import org.gobiiproject.gobiiprocess.digester.HelperFunctions.MobileTransform;
import org.gobiiproject.gobiiprocess.digester.HelperFunctions.SequenceInPlaceTransform;
import org.gobiiproject.gobiiprocess.digester.csv.CSVInstructionProcessor;
import org.gobiiproject.gobiiprocess.digester.utils.validation.DigestFileValidator;
import org.gobiiproject.gobiimodel.dto.instructions.validation.ValidationConstants;
import org.gobiiproject.gobiimodel.dto.instructions.validation.ValidationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.gobiiproject.gobiimodel.utils.FileSystemInterface.rmIfExist;
import static org.gobiiproject.gobiimodel.utils.HelperFunctions.getDestinationFile;
import static org.gobiiproject.gobiimodel.utils.HelperFunctions.getWebserviceConnectionString;

/**
 * Base class for processing instruction files. Start of chain of control for Digester. Takes first argument as instruction file, or promts user.
 * The File Reader runs off the Instruction Files, which tell it where the input files are, and how to process them.
 * {@link CSVInstructionProcessor} and deal with specific file formats. Overall logic and program flow come from this class.
 * <p>
 * This class deals with external commands and scripts, and coordinates uploads to the IFL and directly talks to HDF5 and MonetDB.
 *
 * @author jdl232 Josh L.S.
 */
public class Digester {

    private Logger LOGGER = LoggerFactory.getLogger("Digester");
    
    private final String VARIANT_CALL_TABNAME = "matrix";
    private final String LINKAGE_GROUP_TABNAME = "linkage_group";
    private final String GERMPLASM_PROP_TABNAME = "germplasm_prop";
    private final String GERMPLASM_TABNAME = "germplasm";
    private final String MARKER_TABNAME = "marker";
    private final String DS_MARKER_TABNAME = "dataset_marker";
    private final String DS_SAMPLE_TABNAME = "dataset_dnarun";
    private final String SAMPLE_TABNAME = "dnarun";

    private HDF5Interface hdf5Interface = new HDF5Interface();

    private final DigesterConfig digesterConfig;

    private String loaderScriptPath;
    
    public Digester(DigesterConfig config) {

        this.digesterConfig = config;
    }

    public void run(LoaderInstructionFilesDTO instruction) throws Exception {

        final GobiiLoaderProcedure procedure = instruction.getProcedure();

        final DigestResults results = new DigestResults();

		String extractorScriptPath = digesterConfig + "extractors/";
        loaderScriptPath = digesterConfig.getRootDir() + "loaders/";
        hdf5Interface.setPathToHDF5(loaderScriptPath + "hdf5/bin/");
        hdf5Interface.setPathToHDF5Files(digesterConfig.getPathToHDF5Files());

        boolean success = true;
        Map<String, File> loaderInstructionMap = new HashMap<>();//Map of Key to filename
        List<String> loaderInstructionList = new ArrayList<>(); //Ordered list of loader instructions to execute, Keys to loaderInstructionMap

        ConfigSettings configuration = null;
        try {
            configuration = new ConfigSettings(digesterConfig.getPropertiesFile());
            LOGGER.debug("Opened config settings at " + digesterConfig.getPropertiesFile());

        } catch (Exception e1) {
            e1.printStackTrace();
        }

        JobStatus jobStatus = null;
        try {
            jobStatus = new JobStatus(configuration, procedure.getMetadata().getGobiiCropType(), instruction.getName());
        } catch (Exception e) {
            LOGGER.error("Error Checking Status", e);
        }



        success = jobStatus.set(JobProgressStatusType.CV_PROGRESSSTATUS_INPROGRESS.getCvName(), "Beginning Digest");

        Path cropPath = Paths.get(digesterConfig.getRootDir() + "crops/" + procedure.getMetadata().getGobiiCropType().toLowerCase());
        if (!(Files.exists(cropPath) &&
                Files.isDirectory(cropPath))) {
            LOGGER.error("Unknown Crop Type: " + procedure.getMetadata().getGobiiCropType());
            return;
        }
        GobiiCropConfig gobiiCropConfig;
        try {
            gobiiCropConfig = configuration.getCropConfig(procedure.getMetadata().getGobiiCropType());
        } catch (Exception e) {
            LOGGER.error("Digester Unknown loading error", e);
            return;
        }
        if (gobiiCropConfig == null) {
            LOGGER.error("Unknown Crop Type: " + procedure.getMetadata().getGobiiCropType() + " in the Configuration File");
            return;
        }
        if (hdf5Interface.getPathToHDF5Files() == null)
            hdf5Interface.setPathToHDF5Files(cropPath.toString() + "/hdf5/");

        String errorPath = getLogName(procedure, procedure.getMetadata().getGobiiCropType());


        success = jobStatus.set(JobProgressStatusType.CV_PROGRESSSTATUS_VALIDATION.getCvName(), "Beginning Validation");
        // Instruction file Validation
        InstructionFileValidator instructionFileValidator = new InstructionFileValidator(procedure);
        instructionFileValidator.processInstructionFile();
        String validationStatus = instructionFileValidator.validateMarkerUpload();
        if (validationStatus != null) {
            LOGGER.error("Marker validation failed. " + validationStatus);
            success = false;
        }

        validationStatus = instructionFileValidator.validateSampleUpload();
        if (validationStatus != null) {
            LOGGER.error("Sample validation failed. " + validationStatus);
            success = false;
        }

        validationStatus = instructionFileValidator.validate();
        if (validationStatus != null) {
            LOGGER.error("Validation failed. " + validationStatus);
            success = false;
        }

        String jobName = getJobReadableIdentifier(procedure.getMetadata().getGobiiCropType(), procedure);
        results.setJobName(jobName);

        String logDir = configuration.getFileSystemLog();
        String logFile = null;
        if (logDir != null) {
            LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
            context.putProperty("log-dir",logFile);
            FileAppender<ILoggingEvent> appender = (FileAppender<ILoggingEvent>)context.getLoggerList().get(0).getAppender("FILE");
            appender.stop();
            appender.setFile(logFile);
            appender.start();
            results.setLogFile(logFile);
        }

        SimpleTimer.start("FileRead");

        success = jobStatus.set(JobProgressStatusType.CV_PROGRESSSTATUS_DIGEST.getCvName(), "Beginning file digest");
        //Pre-processing - make sure all files exist, find the cannonical dataset id
        for (GobiiLoaderInstruction inst : procedure.getInstructions()) {
            if (inst == null) {
                LOGGER.error("Missing or malformed instruction in " + instruction.getName());
                success = false;
                continue;
            }
        }

        if (procedure.getMetadata().getGobiiFile() == null) {
            LOGGER.error("Digester", "Instruction " + instruction.getName() + " has bad 'file' column");
            success = false;
        }
        GobiiFileType instructionFileType = procedure.getMetadata().getGobiiFile().getGobiiFileType();
        if (instructionFileType == null) {
            LOGGER.error("Digester", "Instruction " + instruction.getName() + " has missing file format");
            success = false;
        }


        //Section - Processing
        LOGGER.trace("Beginning List Processing");

        DigesterInstructionProcessor instructionProcessor = (p, i) ->
                new CSVInstructionProcessor(loaderScriptPath).process(p, i);
        DigesterFileProcessor digesterFileProcessor = new DigesterFileProcessor(instructionProcessor);

        switch (procedure.getMetadata().getGobiiFile().getGobiiFileType()) { //All instructions should have the same file type, all file types go through CSV Instruction Processor
            case HAPMAP:
                //INTENTIONAL FALLTHROUGH
            case VCF:
                //INTENTIONAL FALLTHROUGH
            case GENERIC:
                digesterFileProcessor.parseInstructionFile(procedure);
                break;
            default:
                System.err.println("Unable to deal with file type " + procedure.getMetadata().getGobiiFile().getGobiiFileType());
                break;
        }

        //Database Validation
        success = jobStatus.set(JobProgressStatusType.CV_PROGRESSSTATUS_VALIDATION.getCvName(), "Database Validation");
        databaseValidation(loaderInstructionMap, procedure.getMetadata(), gobiiCropConfig);

        boolean isVCF = GobiiFileType.VCF.equals(procedure.getMetadata().getGobiiFile().getGobiiFileType());

        for (GobiiLoaderInstruction inst : procedure.getInstructions()) {

            //Section - Matrix Post-processing
            //Dataset is the first non-empty dataset type

            //Switch used for VCF transforms is currently a change in dataset type. See 'why is VCF a data type' GSD
            if (isVCF) {
                procedure.getMetadata().getDataset().setName("VCF");
            }


            String fromFile = getDestinationFile(procedure, inst);
            SequenceInPlaceTransform intermediateFile = new SequenceInPlaceTransform(fromFile, errorPath);
            if (procedure.getMetadata().getDatasetType().getName() != null
                    && inst.getTable().equals(VARIANT_CALL_TABNAME)) {
                errorPath = getLogName(procedure.getMetadata(), procedure.getMetadata().getGobiiCropType(), "Matrix_Processing"); //Temporary Error File Name

                if (DatasetOrientationType.SAMPLE_FAST.equals(procedure.getMetadata().getDatasetOrientationType())) {
                    //Rotate to marker fast before loading it - all data is marker fast in the system
                    File transposeDir = new File(new File(fromFile).getParentFile(), "transpose");
                    intermediateFile.transform(MobileTransform.getTransposeMatrix(transposeDir.getPath()));
                }
            }
            success = jobStatus.set(JobProgressStatusType.CV_PROGRESSSTATUS_TRANSFORMATION.getCvName(), "Metadata Transformation");
            String instructionName = inst.getTable();
            loaderInstructionMap.put(instructionName, new File(getDestinationFile(procedure, inst)));
            loaderInstructionList.add(instructionName);//TODO Hack - for ordering

            if (MARKER_TABNAME.equals(instructionName)) {//Convert 'alts' into a jsonb array
                intermediateFile.transform(MobileTransform.PGArray);
            }

            intermediateFile.returnFile(); // replace intermediateFile where it came from

            //DONE WITH TRANSFORMS
        }


        GobiiClientContext gobiiClientContext = GobiiClientContext.getInstance(configuration, procedure.getMetadata().getGobiiCropType(), GobiiAutoLoginType.USER_RUN_AS);
        if (LineUtils.isNullOrEmpty(gobiiClientContext.getUserToken())) {
            LOGGER.error("Unable to log in with user " + GobiiAutoLoginType.USER_RUN_AS.toString());
            return;
        }
        String currentCropContextRoot = GobiiClientContext.getInstance(null, false).getCurrentCropContextRoot();

        GobiiUriFactory guf = new GobiiUriFactory(currentCropContextRoot);
        guf.resourceColl(RestResourceId.GOBII_CALLS);

        if (LoaderGlobalConfigs.isEnableValidation()) {
            List<ValidationResult> validationResults = validateMetadata(configuration, procedure, gobiiCropConfig);
            results.setValidationResults(validationResults);

            boolean hasAnyFailedStatuses =
                    validationResults.stream()
                        .map(Objects::toString)
                        .anyMatch(ValidationConstants.FAILURE::equalsIgnoreCase);

            if (hasAnyFailedStatuses) {
                LOGGER.error("Validation failures");
                success = false;
            }
        }


        if (success) {
            success = jobStatus.set(JobProgressStatusType.CV_PROGRESSSTATUS_METADATALOAD.getCvName(), "Loading Metadata");
            errorPath = getLogName(procedure.getMetadata(), procedure.getMetadata().getGobiiCropType(), "IFLs");
            String pathToIFL = loaderScriptPath + "postgres/gobii_ifl/gobii_ifl.py";
            String connectionString = " -c " + HelperFunctions.getPostgresConnectionString(gobiiCropConfig);

            //Load PostgreSQL
            boolean loadedData = false;
            for (String key : loaderInstructionList) {
                if (!VARIANT_CALL_TABNAME.equals(key)) {
                    String dstDir = procedure.getMetadata().getGobiiFile().getDestination();
                    String inputFile = " -i " + loaderInstructionMap.get(key);
                    String outputFile = " -o " + dstDir + "/"; //Output here is temporary files, needs terminal /

                    LOGGER.info("Running IFL: " + pathToIFL + " <connection string> " + inputFile + outputFile);
                    //Lines affected returned by method call - THIS IS NOW IGNORED
                    HelperFunctions.tryExec(pathToIFL + connectionString + inputFile + outputFile + " -l", digesterConfig.isVerbose() ? dstDir + "/iflOut" : null, errorPath);

                    IFLLineCounts counts = calculateTableStats(loaderInstructionMap, dstDir, key);

                    if (counts != null) {
                        results.getIflLineCounts().add(counts);
                    }

                    if (counts.getLoadedData() == 0) {
                        LOGGER.debug("No data loaded for table " + key);
                    } else {
                        loadedData = true;
                    }
                    if (counts.getInvalidData() > 0 && !isVariableLengthTable(key)) {
                        LOGGER.warn("Invalid data in table " + key);
                    } else {
                        //If there are no issues in the load, clean up temporary intermediate files
                        if (!LoaderGlobalConfigs.isKeepAllIntermediates()) {
                            //And if 'delete intermediate files' is true, clean up all IFL files (we don't need them any more
                            deleteIFLFiles(dstDir, key,!LoaderGlobalConfigs.isDeleteIntermediateFiles());
                        }
                    }

                }


            }
            if (! loadedData) {
                LOGGER.error("No new data was uploaded.");
                success = false;
            }

            final Integer dataSetId = procedure.getMetadata().getDataset().getId();
            errorPath = getLogName(procedure.getMetadata(), procedure.getMetadata().getGobiiCropType(), "Matrix_Upload");
            String variantFilename = "DS" + dataSetId;
            File variantFile = loaderInstructionMap.get(VARIANT_CALL_TABNAME);

            if (variantFile != null && dataSetId == null) {
                LOGGER.error("Data Set ID is null for variant call");
                success = false;
            }

            if ((variantFile != null) && dataSetId != null) { //Create an HDF5 and a Monet
                success = jobStatus.set(JobProgressStatusType.CV_PROGRESSSTATUS_MATRIXLOAD.getCvName(), "Matrix Upload");
                boolean HDF5Success = hdf5Interface.createHDF5FromDataset(procedure.getMetadata().getDatasetType().getName(),
                        configuration, dataSetId, procedure.getMetadata().getGobiiCropType(), errorPath, variantFilename, variantFile);
                rmIfExist(variantFile.getPath());
                success &= HDF5Success;
            }

            if (success) {
                LOGGER.info("Successful Data Upload");
                if (procedure.getMetadata().isQcCheck()) {
                    success =jobStatus.set(JobProgressStatusType.CV_PROGRESSSTATUS_QCPROCESSING.getCvName(), "Processing QC Job");

                    new QcHandler().executeQc(configuration, procedure.getMetadata());
                } else {
                    success =jobStatus.set(JobProgressStatusType.CV_PROGRESSSTATUS_COMPLETED.getCvName(), "Successful Data Load");
                }

            } else { //endIf(success)
                LOGGER.warn("Unsuccessful Upload");
                jobStatus.setError("Unsuccessfully Uploaded Files");
            }
        }//endif Digest section


        DigesterProcessMessage processMessage = DigesterProcessMessage.create(configuration, digesterConfig, instruction, results);
        try {
            new MailInterface(configuration).send(processMessage);
        } catch (Exception e) {
            LOGGER.error("MailInterface -- Error Sending Mail", e);
        }
    }


    private List<ValidationResult> validateMetadata(ConfigSettings configuration, GobiiLoaderProcedure procedure, GobiiCropConfig gobiiCropConfig) throws Exception {

        String directory = procedure.getMetadata().getGobiiFile().getDestination();
        //Validation logic before loading any metadata
        String baseConnectionString = getWebserviceConnectionString(gobiiCropConfig);
        String user = configuration.getLdapUserForBackendProcs();
        String password = configuration.getLdapPasswordForBackendProcs();
        DigestFileValidator digestFileValidator = new DigestFileValidator(directory, baseConnectionString,user, password);
        digestFileValidator.performValidation(gobiiCropConfig, procedure.getMetadata().getDatasetOrientationType());
        //Call validations here, update 'success' to false with any call to ErrorLOGGER.logError()
        List<Path> pathList =
                Files.list(Paths.get(directory))
                        .filter(Files::isRegularFile).filter(path -> String.valueOf(path.getFileName()).endsWith(".json")).collect(Collectors.toList());
        if (pathList.size() < 1) {
            LOGGER.error("Validation -- Unable to find validation checks");
        }
        ValidationResult[] validationResults = new ObjectMapper().readValue(pathList.get(0).toFile(), ValidationResult[].class);


        return Arrays.asList(validationResults);
    }

    private void databaseValidation(Map<String, File> loaderInstructionMap, GobiiLoaderMetadata metadata, GobiiCropConfig gobiiCropConfig) {
        DatabaseQuerier querier = new DatabaseQuerier(gobiiCropConfig.getServer(ServerType.GOBII_PGSQL));

        //If we're doing a DS upload and there is no DS_Marker
        if (loaderInstructionMap.containsKey(VARIANT_CALL_TABNAME) && loaderInstructionMap.containsKey(DS_MARKER_TABNAME) && !loaderInstructionMap.containsKey(MARKER_TABNAME)) {
            querier.checkMarkerInPlatform(loaderInstructionMap.get(DS_MARKER_TABNAME), metadata.getPlatform().getId());
        }
        //If we're doing a DS upload and there is no DS_Sample
        if (loaderInstructionMap.containsKey(VARIANT_CALL_TABNAME) && loaderInstructionMap.containsKey(DS_SAMPLE_TABNAME) && !loaderInstructionMap.containsKey(SAMPLE_TABNAME)) {
            querier.checkDNARunInExperiment(loaderInstructionMap.get(DS_SAMPLE_TABNAME), metadata.getExperiment().getId());
        }

        if (loaderInstructionMap.containsKey(MARKER_TABNAME)) {
            querier.checkMarkerExistence(loaderInstructionMap.get(MARKER_TABNAME));
        }
        if (loaderInstructionMap.containsKey(GERMPLASM_TABNAME)) {
            querier.checkGermplasmTypeExistence(loaderInstructionMap.get(GERMPLASM_TABNAME));
            querier.checkGermplasmSpeciesExistence(loaderInstructionMap.get(GERMPLASM_TABNAME));
        }
        querier.close();
    }



    /**
     * Read ppd and nodups files to determine their length, and add the row corresponding to the key to the digester message status.
     * Assumes IFL was run with output of dstDir on key in instructionMap.
     *
     * @param loaderInstructionMap Map of key/location of loader instructions
     * @param dstDir               Destination directory for IFL call run on key's table
     * @param key                  Key in loaderInstructionMap
     * @return
     */
    private IFLLineCounts calculateTableStats(Map<String, File> loaderInstructionMap, String dstDir, String key) {

        String ppdFile = new File(dstDir, "ppd_digest." + key).getAbsolutePath();
        //If there is a deduplicated PPD file, use it instead of the ppd file
        String ddpPpdFile = new File(dstDir, "ddp_ppd_digest." + key).getAbsolutePath();
        if (new File(ddpPpdFile).exists()) {
            ppdFile = ddpPpdFile;
        }

        String noDupsFile = new File(dstDir, "nodups_ppd_digest." + key).getAbsolutePath();
        //If there is a deduplicated nodups file, use it instead of the nodups file
        String ddpNoDupsFile = new File(dstDir, "nodups_ddp_ppd_digest." + key).getAbsolutePath();
        if (new File(ddpNoDupsFile).exists()) {
            noDupsFile = ddpNoDupsFile;
        }

        //-1 lines for header
        int totalLines = FileSystemInterface.lineCount(loaderInstructionMap.get(key).getAbsolutePath()) - 1;
        int ppdLines = FileSystemInterface.lineCount(ppdFile) - 1;
        int noDupsLines = FileSystemInterface.lineCount(noDupsFile) - 1;
        //They're -1 if the file is missing.
        if (totalLines < 0) totalLines = 0;
        if (ppdLines < 0) ppdLines = 0;
        if (noDupsLines < 0) noDupsLines = 0;

        boolean noDupsFileExists = new File(noDupsFile).exists();
        if (!noDupsFileExists) noDupsLines = ppdLines;
        //Begin Business Logic Zone
        int loadedLines = noDupsLines;
        int existingLines = ppdLines - noDupsLines;
        int invalidLines = totalLines - ppdLines;
        //End Business Logic Zone - regular logic can resume

        //If total lines/file lines less than 0, something's wrong. Also if total lines is < changed, something's wrong.


        IFLLineCounts counts = new IFLLineCounts();
        counts.setKey(key);
        counts.setTotalLines(totalLines);
        counts.setLoadedData(loadedLines);
        counts.setExistingData(existingLines);
        counts.setInvalidData(invalidLines);
        counts.setNoDupsFileExists(noDupsFileExists);
        return counts;
    }

    /**
     * Returns a human readable name for the job.
     *
     * @param cropName Name of the crop being run
     * @return a human readable name for the job
     */
    private String getJobReadableIdentifier(String cropName, GobiiLoaderProcedure procedure) {
        cropName = cropName.charAt(0) + cropName.substring(1).toLowerCase();// MAIZE -> Maize
        return "[GOBII - Loader]: " + cropName + " - digest of \"" + procedure.getMetadata().getGobiiFile() + "\"";
    }

    /**
     * Converts the File input into the FIRST of the source files.
     *
     * @param file Reference to Instruction's File object.
     * @return String representation of first of source files
     */
    public String getSourceFileName(GobiiFile file) {
        String source = file.getSource();
        File sourceFolder = new File(source);
        File[] f = sourceFolder.listFiles();
        if (f.length != 0) source = f[0].getName();
        else {
            source = sourceFolder.getName();//Otherwise we get full paths in source.
        }
        return source;
    }

    /**
     * Generates a log file location given a crop name, crop type, and process ID. (Given by the process calling this method).
     * <p>
     * Currently works by placing logs in the intermediate file directory.
     *
     * @return The logfile location for this process
     */
    private String getLogName(GobiiLoaderProcedure procedure, String cropName) {
        String destination = procedure.getMetadata().getGobiiFile().getDestination();
        String table = procedure.getInstructions().get(0).getTable();
        return destination + "/" + cropName + "_Table-" + table + ".log";
    }

    /**
     * Generates a log file location given a crop name, crop type, and process ID. (Given by the process calling this method).
     * <p>
     * Currently works by placing logs in the intermediate file directory.
     *
     * @return The logfile location for this process
     */
    private String getLogName(GobiiLoaderMetadata metadata, String cropName, String process) {
        String destination = metadata.getGobiiFile().getDestination();
        return destination + "/" + cropName + "_Process-" + process + ".log";
    }

    /**
     * Determine crop type by looking at the intruction file's location for the name of a crop.
     *
     * @param instructionFile
     * @return GobiiCropType
     */
    private static String divineCrop(String instructionFile) {
        String upper = instructionFile.toUpperCase();
        String from = "/CROPS/";
        int fromIndex = upper.indexOf(from) + from.length();
        String crop = upper.substring(fromIndex, upper.indexOf('/', fromIndex));
        return crop;
    }

    private String getJDBCConnectionString(GobiiCropConfig config) {
        return HelperFunctions.getJdbcConnectionString(config);
    }

    /**
     * Given a string key, determine if the table is one-to-one with relation to the input file size.
     * If not, several metrics become meaningless.
     *
     * @param tableKey
     * @return true if the table will have different PPD rows than input rows
     */


    /**
     * Deletes all files in directory that contain '.tablename' suffix
     *
     * @param directory
     * @param tableName
     */
    private static void deleteIFLFiles(String directory, String tableName, boolean onlyTemps) {
        File[] fileList = new File(directory).listFiles();
        if (fileList == null) return;
        for (File f : fileList) {
            if (f.getName().endsWith("." + tableName)) {
                if(!onlyTemps || (f.getName().startsWith("digest."))) {
                    rmIfExist(f);
                }
            }
        }
    }

    private static boolean isVariableLengthTable(String tableKey) {
        return tableKey.contains("_prop");
    }

}
