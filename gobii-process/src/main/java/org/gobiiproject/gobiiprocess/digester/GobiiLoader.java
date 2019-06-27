package org.gobiiproject.gobiiprocess.digester;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import org.apache.commons.cli.*;
import org.gobiiproject.gobiiapimodel.payload.Header;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.gobii.GobiiUriFactory;
import org.gobiiproject.gobiiapimodel.restresources.common.RestUri;
import org.gobiiproject.gobiimodel.config.RestResourceId;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContext;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiimodel.cvnames.JobPayloadType;
import org.gobiiproject.gobiimodel.cvnames.JobProgressStatusType;
import org.gobiiproject.gobiimodel.config.*;
import org.gobiiproject.gobiimodel.dto.instructions.extractor.*;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.DataSetDTO;
import org.gobiiproject.gobiimodel.dto.instructions.extractor.ExtractorInstructionFilesDTO;
import org.gobiiproject.gobiimodel.utils.DateUtils;
import org.gobiiproject.gobiimodel.utils.FileSystemInterface;
import org.gobiiproject.gobiiapimodel.payload.HeaderStatusMessage;
import org.gobiiproject.gobiimodel.utils.HelperFunctions;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.dto.instructions.loader.*;
import org.gobiiproject.gobiimodel.types.*;
import org.gobiiproject.gobiimodel.utils.*;
import org.gobiiproject.gobiimodel.utils.email.*;
import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;
import org.gobiiproject.gobiiprocess.HDF5Interface;
import org.gobiiproject.gobiiprocess.JobStateUpdater;
import org.gobiiproject.gobiiprocess.digester.HelperFunctions.*;
import org.gobiiproject.gobiiprocess.digester.csv.CSVFileReaderV2;
import org.gobiiproject.gobiiprocess.digester.utils.validation.DigestFileValidator;
import org.gobiiproject.gobiiprocess.digester.utils.validation.ValidationConstants;
import org.gobiiproject.gobiiprocess.digester.utils.validation.errorMessage.ValidationError;
import org.springframework.util.CollectionUtils;

import static org.gobiiproject.gobiimodel.utils.FileSystemInterface.rmIfExist;
import static org.gobiiproject.gobiimodel.utils.HelperFunctions.*;
import static org.gobiiproject.gobiimodel.utils.error.ErrorLogger.logError;

/**
 * Base class for processing instruction files. Start of chain of control for Digester. Takes first argument as instruction file, or promts user.
 * The File Reader runs off the Instruction Files, which tell it where the input files are, and how to process them.
 * {@link CSVFileReaderV2} and deal with specific file formats. Overall logic and program flow come from this class.
 * <p>
 * This class deals with external commands and scripts, and coordinates uploads to the IFL and directly talks to HDF5 and MonetDB.
 *
 * @author jdl232 Josh L.S.
 */
public class GobiiLoader {

    private static Messenger messenger;
    private static InstructionFileValidator instructionFileValidator = new InstructionFileValidator();

    private static final String VARIANT_CALL_TABNAME = "matrix";
    private static final String LINKAGE_GROUP_TABNAME = "linkage_group";
    private static final String GERMPLASM_PROP_TABNAME = "germplasm_prop";
    private static final String GERMPLASM_TABNAME = "germplasm";
    private static final String MARKER_TABNAME = "marker";
    private static final String DS_MARKER_TABNAME = "dataset_marker";
    private static final String DS_SAMPLE_TABNAME = "dataset_dnarun";
    private static final String SAMPLE_TABNAME = "dnarun";
    private static GobiiUriFactory gobiiUriFactory;
    private static GobiiExtractorInstruction qcExtractInstruction = null;

    //Trinary - was this load marker fast(true), sample fast(false), or unknown/not applicable(null)
    public static Boolean isMarkerFast=null;

    private GobiiLoaderConfig config;

    public GobiiLoader(GobiiLoaderConfig config) {
        this.config = config;

        HDF5Interface.setPathToHDF5Files(config.getHdf5FilesPath());
    }

    public void run(GobiiLoaderProcedure procedure) throws Exception {
        GobiiLoaderState state = new GobiiLoaderState();

        state.setProcedure(procedure);

        if (state.getProcedure().getMetadata().getGobiiCropType() == null)
            state.getProcedure().getMetadata().setGobiiCropType(divineCrop(config.getInstructionFile()));

        if (validateProcedure(config, state) == null) {
            return;
        }

        HDF5Interface.setPathToHDF5(config.getLoaderScriptPath() + "hdf5/bin/");

        boolean success = true;
        Map<String, File> loaderInstructionMap = new LinkedHashMap<>();//Map of Key to filename

        ConfigSettings configuration = null;
        try {
            configuration = new ConfigSettings(config.getPropertiesFile());
            ErrorLogger.logDebug("Config file path", "Opened config settings at " + config.getPropertiesFile());

        } catch (Exception e1) {
            e1.printStackTrace();
        }

        messenger = new MailInterface(configuration); // These configurations should be injected

        //Error logs go to a file based on crop (for human readability) and
        ErrorLogger.logInfo("Digester", "Beginning read of " + config.getInstructionFile());


        //Job Id is the 'name' part of the job file  /asd/de/name.json
        String filename = new File(config.getInstructionFile()).getName();
        String jobFileName = filename.substring(0, filename.lastIndexOf('.'));
        JobStateUpdater jobStateUpdater = null;
        try {
            jobStateUpdater = new JobStateUpdater(configuration, state.getProcedure().getMetadata().getGobiiCropType(), jobFileName);
        } catch (Exception e) {
            ErrorLogger.logError("GobiiLoader", "Error Checking Status", e);
        }

        jobStateUpdater.doUpdate(JobProgressStatusType.CV_PROGRESSSTATUS_INPROGRESS, "Beginning Digest");
        String dstFilePath = getDestinationFile(state.getProcedure().getMetadata(), state.getProcedure().getInstructions().get(0));//Intermediate 'file'

        if (! new File(dstFilePath).isDirectory()) { //Note: if dstDir is a non-existant
            state.getProcedure().getMetadata().getGobiiFile()
                    .setDestination(dstFilePath.substring(0, dstFilePath.lastIndexOf("/")));
        } else {
            state.getProcedure().getMetadata().getGobiiFile().setDestination(dstFilePath);
        }


        Path cropPath = Paths.get(config.getRootDir() + "crops/" + state.getProcedure().getMetadata().getGobiiCropType().toLowerCase());
        if (!(Files.exists(cropPath) &&
                Files.isDirectory(cropPath))) {
            logError("Digester", "Unknown Crop Type: " + state.getProcedure().getMetadata().getGobiiCropType());
            return;
        }
        GobiiCropConfig gobiiCropConfig;
        try {
            gobiiCropConfig = configuration.getCropConfig(state.getProcedure().getMetadata().getGobiiCropType());
        } catch (Exception e) {
            logError("Digester", "Unknown loading error", e);
            return;
        }
        if (gobiiCropConfig == null) {
            logError("Digester", "Unknown Crop Type: " + state.getProcedure().getMetadata().getGobiiCropType() + " in the Configuration File");
            return;
        }
        if (HDF5Interface.getPathToHDF5Files() == null)
            HDF5Interface.setPathToHDF5Files(cropPath.toString() + "/hdf5/");

        String errorPath = getLogName(state.getProcedure(), state.getProcedure().getMetadata().getGobiiCropType());

        jobStateUpdater.doUpdate(JobProgressStatusType.CV_PROGRESSSTATUS_VALIDATION, "Beginning Validation");

        //TODO: HACK - Job's name is
        state.setJobName(getJobReadableIdentifier(state.getProcedure()));


        String logDir = configuration.getFileSystemLog();
        if (logDir != null) {
            String logFile = null;
            String instructionName = new File(config.getInstructionFile()).getName();
            instructionName = instructionName.substring(0, instructionName.lastIndexOf('.'));
            logFile = logDir + "/" + instructionName + ".log";
            String oldLogFile = ErrorLogger.getLogFilepath();
            ErrorLogger.logDebug("Error Logger", "Moving error log to " + logFile);
            ErrorLogger.setLogFilepath(logFile);
            ErrorLogger.logDebug("Error Logger", "Moved error log to " + logFile);
            FileSystemInterface.rmIfExist(oldLogFile);
            state.setLogFile(logFile);
        }

        SimpleTimer.start("FileRead");

//		if (state.getProcedure().getMetaData().isQcCheck()) {//QC - Subsection #1 of 3
//			qcExtractInstruction = createQCExtractInstruction(zero, crop);
//		}


        jobStateUpdater.doUpdate(JobProgressStatusType.CV_PROGRESSSTATUS_DIGEST, "Beginning file digest");

        //Section - Processing
        ErrorLogger.logTrace("Digester", "Beginning List Processing");
        success = true;

        if (Sets.newHashSet(GobiiFileType.HAPMAP, GobiiFileType.VCF, GobiiFileType.GENERIC)
                .contains(state.getProcedure().getMetadata().getGobiiFile().getGobiiFileType())) {

            CSVFileReaderV2.parseInstructionFile(state.getProcedure(), config.getLoaderScriptPath());
        } else {
            System.err.println("Unable to deal with file type " + state.getProcedure().getMetadata().getGobiiFile().getGobiiFileType());
        }

        //Database Validation
        jobStateUpdater.doUpdate(JobProgressStatusType.CV_PROGRESSSTATUS_VALIDATION, "Database Validation");
        databaseValidation(loaderInstructionMap, state.getProcedure().getMetadata(), gobiiCropConfig);

        boolean sendQc = false;

        for (GobiiLoaderInstruction inst : state.getProcedure().getInstructions()) {

            //Switch used for VCF transforms is currently a change in dataset type. See 'why is VCF a data type' GSD
            if (state.getProcedure().getMetadata().getGobiiFile().getGobiiFileType().equals(GobiiFileType.VCF)) {
                state.getProcedure().getMetadata().getDatasetType().setName("VCF");
            }

            String fromFile = getDestinationFile(state.getProcedure().getMetadata(), inst);
            SequenceInPlaceTransform intermediateFile = new SequenceInPlaceTransform(fromFile, errorPath);
            errorPath = getLogName(state.getProcedure(), "Matrix_Processing"); //Temporary Error File Name

            if (state.getProcedure().getMetadata().getDatasetType().getName() != null
                    && inst.getTable().equals(VARIANT_CALL_TABNAME)) {

                if (DataSetOrientationType.SAMPLE_FAST.equals(state.getProcedure().getMetadata().getDataSetOrientationType())) {
                    //Rotate to marker fast before loading it - all data is marker fast in the system
                    File transposeDir = new File(new File(fromFile).getParentFile(), "transpose");
                    intermediateFile.transform(MobileTransform.getTransposeMatrix(transposeDir.getPath()));
                    isMarkerFast=false;
                }else{
                    isMarkerFast=true;
                }
            }

            jobStateUpdater.doUpdate(JobProgressStatusType.CV_PROGRESSSTATUS_TRANSFORMATION, "Metadata Transformation");
            String instructionName = inst.getTable();
            loaderInstructionMap.put(instructionName, new File(getDestinationFile(state.getProcedure().getMetadata(), inst)));

            if (LINKAGE_GROUP_TABNAME.equals(instructionName) || GERMPLASM_TABNAME.equals(instructionName) || GERMPLASM_PROP_TABNAME.equals(instructionName)) {
                success &= HelperFunctions.tryExec(config.getLoaderScriptPath() + "LGduplicates.py -i " + getDestinationFile(state.getProcedure().getMetadata(), inst));
            }
            if (MARKER_TABNAME.equals(instructionName)) {//Convert 'alts' into a jsonb array
                intermediateFile.transform(MobileTransform.PGArray);
            }

            intermediateFile.returnFile(); // replace intermediateFile where it came from

            //DONE WITH TRANSFORMS

            if (state.getProcedure().getMetadata().isQcCheck()) {//QC - Subsection #2 of 3
                qcExtractInstruction = createQCExtractInstruction(state.getProcedure().getMetadata());
                setQCExtractPaths(state.getProcedure(), configuration, state.getProcedure().getMetadata().getGobiiCropType(), config.getInstructionFile());
                sendQc = success;
            }

        }


        GobiiClientContext gobiiClientContext = GobiiClientContext.getInstance(configuration, state.getProcedure().getMetadata().getGobiiCropType(), GobiiAutoLoginType.USER_RUN_AS);
        if (LineUtils.isNullOrEmpty(gobiiClientContext.getUserToken())) {
            ErrorLogger.logError("Digester", "Unable to log in with user " + GobiiAutoLoginType.USER_RUN_AS.toString());
            return;
        }
        String currentCropContextRoot = GobiiClientContext.getInstance(null, false).getCurrentCropContextRoot();

        GobiiUriFactory guf = new GobiiUriFactory(currentCropContextRoot);
        guf.resourceColl(RestResourceId.GOBII_CALLS);

        validateMetadata(state, gobiiCropConfig, configuration);


        if (success && ErrorLogger.success()) {
            jobStateUpdater.doUpdate(JobProgressStatusType.CV_PROGRESSSTATUS_METADATALOAD, "Loading Metadata");
            errorPath = getLogName(state.getProcedure(), "IFLs");
            String pathToIFL = config.getLoaderScriptPath() + "postgres/gobii_ifl/gobii_ifl.py";
            String connectionString = " -c " + HelperFunctions.getPostgresConnectionString(gobiiCropConfig);

            String directory = new File(state.getProcedure().getMetadata().getGobiiFile().getDestination()).getAbsolutePath();

            //Load PostgreSQL
            boolean loadedData = false;
            for (String key : loaderInstructionMap.keySet()) {
                if (!VARIANT_CALL_TABNAME.equals(key)) {
                    String inputFile = " -i " + loaderInstructionMap.get(key);
                    String outputFile = " -o " + directory + "/"; //Output here is temporary files, needs terminal /

                    ErrorLogger.logInfo("Digester", "Running IFL: " + pathToIFL + " <conntection string> " + inputFile + outputFile);
                    //Lines affected returned by method call - THIS IS NOW IGNORED
                    HelperFunctions.tryExec(pathToIFL + connectionString + inputFile + outputFile + " -l", config.isVerbose() ? directory + "/iflOut" : null, errorPath);

                    calculateTableStats(state, loaderInstructionMap, new File(state.getProcedure().getMetadata().getGobiiFile().getDestination()), key);

                    if (state.getTableStats().get(key).getLinesLoaded() == 0) {
                        ErrorLogger.logDebug("FileReader", "No data loaded for table " + key);
                    } else {
                        loadedData = true;
                    }
                    if (state.getTableStats().get(key).getInvalidLines() > 0 && !isVariableLengthTable(key)) {
                        ErrorLogger.logWarning("FileReader", "Invalid data in table " + key);
                    } else {
                        if (LoaderGlobalConfigs.getDeleteIntermediateFiles()) {
                            deleteIFLFiles(new File(state.getProcedure().getMetadata().getGobiiFile().getDestination()), key);
                        }
                    }

                }


            }
            if (!loadedData) {
                ErrorLogger.logError("FileReader", "No new data was uploaded.");
            }
            //Load Monet/HDF5
            errorPath = getLogName(state.getProcedure(), "Matrix_Upload");
            String variantFilename = "DS" + state.getProcedure().getMetadata().getDataSet().getId();
            File variantFile = loaderInstructionMap.get(VARIANT_CALL_TABNAME);


            if (variantFile != null) {
                if (state.getProcedure().getMetadata().getDataSet().getId() == null) {
                    logError("Digester", "Data Set ID is null for variant call");
                } else { //Create an HDF5 and a Monet

                    jobStateUpdater.doUpdate(JobProgressStatusType.CV_PROGRESSSTATUS_MATRIXLOAD, "Matrix Upload");
                    boolean HDF5Success = HDF5Interface.createHDF5FromDataset(configuration, state.getProcedure(), errorPath, variantFilename, variantFile);
                    rmIfExist(variantFile.getPath());

                    if (HDF5Success) {
                        ErrorLogger.logInfo("Digester", "Successful Data Upload");
                        if (sendQc) {
                            jobStateUpdater.doUpdate(JobProgressStatusType.CV_PROGRESSSTATUS_QCPROCESSING, "Processing QC Job");
                            sendQCExtract(configuration, state.getProcedure().getMetadata().getGobiiCropType());
                        } else {
                            jobStateUpdater.doUpdate(JobProgressStatusType.CV_PROGRESSSTATUS_COMPLETED, "Successful Data Load");
                        }
                    } else {
                        ErrorLogger.logWarning("Digester", "Unsuccessful Upload");
                        jobStateUpdater.setError("Unsuccessfully Uploaded Files");
                    }
                }
            }
        }//endif Digest section
        else {
            ErrorLogger.logWarning("Digester", "Aborted - Unsuccessfully Generated Files");
            jobStateUpdater.setError("Unsuccessfully Generated Files - No Data Upload");
        }

        //Send Email
        messenger.send(generateMessage(config, state));

        HelperFunctions.completeInstruction(config.getInstructionFile(), configuration.getProcessingPath(state.getProcedure().getMetadata().getGobiiCropType(), GobiiFileProcessDir.LOADER_DONE));

    }

    public static GobiiLoaderState validateMetadata(GobiiLoaderState state, GobiiCropConfig gobiiCropConfig, ConfigSettings configuration) throws Exception {

        //Validation logic before loading any metadata
        String baseConnectionString = getWebserviceConnectionString(gobiiCropConfig);
        String user = configuration.getLdapUserForBackendProcs();
        String password = configuration.getLdapPasswordForBackendProcs();
        String directory = new File(state.getProcedure().getMetadata().getGobiiFile().getDestination()).getAbsolutePath();

        //Metadata Validation
        boolean reportedValidationFailures = false;
        if(LoaderGlobalConfigs.getValidation()) {
            DigestFileValidator digestFileValidator = new DigestFileValidator(directory, baseConnectionString, user, password);
            digestFileValidator.performValidation();
            //Call validations here, update 'success' to false with any call to ErrorLogger.logError()
            List<Path> pathList =
                    Files.list(Paths.get(directory))
                            .filter(Files::isRegularFile).filter(path -> String.valueOf(path.getFileName()).endsWith(".json")).collect(Collectors.toList());
            if (pathList.size() < 1) {
                ErrorLogger.logError("Validation","Unable to find validation checks");
            }
            state.setFileErrors(new ObjectMapper().readValue(pathList.get(0).toFile(), ValidationError[].class));

            Arrays.stream(state.getFileErrors())
                    .filter(err -> ValidationConstants.FAILURE.equalsIgnoreCase(err.status))
                    .findFirst()
                    .ifPresent(err -> ErrorLogger.logError("Validation", "Validation failures"));

        }

        return state;
    }


    public static GobiiLoaderState validateProcedure(GobiiLoaderConfig config, GobiiLoaderState state) {

        if (CollectionUtils.isEmpty(state.getProcedure().getInstructions())) {
            logError("Digester", "No instruction for file " + config.getInstructionFile());
            return null;
        }

        // Instruction file Validation
        instructionFileValidator.processInstructionFile(state.getProcedure().getInstructions());

        String markerValidation = instructionFileValidator.validateMarkerUpload();
        if (markerValidation != null) {
            ErrorLogger.logError("Marker validation failed.", markerValidation);
        }

        String sampleValidation = instructionFileValidator.validateSampleUpload();
        if (sampleValidation != null) {
            ErrorLogger.logError("Sample validation failed.", sampleValidation);
        }

        String totalValidation = instructionFileValidator.validate();
        if (totalValidation != null) {
            ErrorLogger.logError("Validation failed.", totalValidation);
        }

        if (state.getProcedure().getMetadata().getGobiiFile() == null) {
            logError("Digester", "Instruction " + config.getInstructionFile() + " has bad 'file' column");
        } else if (state.getProcedure().getMetadata().getGobiiFile().getGobiiFileType() == null) {
            logError("Digester", "Instruction " + config.getInstructionFile() + " has missing file format");
        }


        //Pre-processing - make sure all files exist, find the cannonical dataset id
        for (GobiiLoaderInstruction inst : state.getProcedure().getInstructions()) {
            if (inst == null) {
                logError("Digester", "Missing or malformed instruction in " + config.getInstructionFile());
            } else if (inst.getTable().equals(VARIANT_CALL_TABNAME)
                        && (state.getProcedure().getMetadata().getDatasetType()!=null)
                        && state.getProcedure().getMetadata().getGobiiFile().getGobiiFileType().equals(GobiiFileType.VCF)
                        && (! "NUCLEOTIDE_2_LETTER".equals(state.getProcedure().getMetadata().getDatasetType().getName()))) {

                ErrorLogger.logError("GobiiLoader", "Invalid Dataset Type selected for VCF file. Expected 2 Letter Nucleotide. Received " + state.getProcedure().getMetadata().getDatasetType());
            }
        }

        return state;
    }

    public static ProcessMessage addValidationError(ProcessMessage pm, ValidationError status) {
        status.failures.forEach(
            failure ->
                    pm.addValidateTableElement(status.fileName,
                                               status.status,
                                               failure.reason,
                                               failure.columnName,
                                               failure.values));

        return pm;
    }

    public static ProcessMessage generateMessage(GobiiLoaderConfig config, GobiiLoaderState state) {

        ProcessMessage pm = new ProcessMessage();

        pm.addPath("Instruction File", new File(config.getInstructionFile()).getAbsolutePath(), true);

        pm.addIdentifier("Project", state.getProcedure().getMetadata().getProject());
        pm.addIdentifier("Platform", state.getProcedure().getMetadata().getPlatform());
        pm.addIdentifier("Experiment", state.getProcedure().getMetadata().getExperiment());
        pm.addIdentifier("Dataset", state.getProcedure().getMetadata().getDataSet());
        pm.addIdentifier("Mapset", state.getProcedure().getMetadata().getMapset());
        pm.addIdentifier("Dataset Type", state.getProcedure().getMetadata().getDatasetType());

        pm.addFolderPath("Destination Directory", state.getProcedure().getMetadata().getGobiiFile().getDestination());//Convert to directory
        pm.addFolderPath("Input Directory", state.getProcedure().getMetadata().getGobiiFile().getSource());

        pm.setUser(state.getProcedure().getMetadata().getContactEmail());

        Arrays.stream(state.getFileErrors())
                .filter(err -> err.status.equalsIgnoreCase(ValidationConstants.FAILURE))
                .forEach(err -> addValidationError(pm, err));

        boolean hasAnyFailedStatuses = Arrays.stream(state.getFileErrors())
                .anyMatch(err -> ValidationConstants.FAILURE.equalsIgnoreCase(err.status));

        if (hasAnyFailedStatuses) {
            Arrays.stream(state.getFileErrors())
                    .filter(err -> err.status.equalsIgnoreCase(ValidationConstants.SUCCESS))
                    .forEach(err -> pm.addValidateTableElement(err.fileName, err.status));
        }

        for (Map.Entry<String, GobiiLoaderState.TableStats> entry : state.getTableStats().entrySet()) {

            String key = entry.getKey();
            GobiiLoaderState.TableStats stats = entry.getValue();

            //Default to 'we had an error'
            String totalLinesVal, linesLoadedVal, existingLinesVal, invalidLinesVal;
            totalLinesVal = linesLoadedVal = existingLinesVal = invalidLinesVal = "error";

            if (isVariableLengthTable(key)) {
                totalLinesVal = stats.getTotalLines() + "";
                linesLoadedVal = stats.getLinesLoaded() + "";
                //Existing and Invalid may be absolutely random numbers in EAV JSON objects
                //Also, loaded may be waaaay above total, this is normal. So lets not report these two fields at all
                existingLinesVal = "";
                invalidLinesVal = "";

                //We can still warn people if no lines were loaded
                if (stats.getLinesLoaded() == 0) {
                    linesLoadedVal = "<b style=\"background-color:yellow\">" + stats.getLinesLoaded() + "</b>";
                }
            } else {
                totalLinesVal = stats.getTotalLines() + "";
                linesLoadedVal = stats.getLinesLoaded() + "";//Header
                existingLinesVal = stats.getExistingLines() + "";
                invalidLinesVal = stats.getInvalidLines() + "";

                if (!stats.isNoDupsFileExists()) {
                    existingLinesVal = "";
                }
                if (stats.getInvalidLines() != 0) {
                    invalidLinesVal = "<b style=\"background-color:red\">" + stats.getInvalidLines() + "</b>";
                }
                if (stats.getLinesLoaded() == 0) {
                    linesLoadedVal = "<b style=\"background-color:yellow\">" + stats.getLinesLoaded() + "</b>";
                }
            }

            pm.addEntry(key, totalLinesVal, linesLoadedVal, existingLinesVal, invalidLinesVal);
        }

        pm.addPath("matrix directory", HDF5Interface.getPathToHDF5Files());

        GobiiFileType loadType = state.getProcedure().getMetadata().getGobiiFile().getGobiiFileType();
        String loadTypeName = "";//No load type name if default
        if (loadType != GobiiFileType.GENERIC) loadTypeName = loadType.name();
        pm.addPath("Error Log", state.getLogFile());
        pm.setBody(state.getJobName(), loadTypeName, SimpleTimer.stop("FileRead"), ErrorLogger.getFirstErrorReason(), ErrorLogger.success(), ErrorLogger.getAllErrorStringsHTML());

        return pm;
    }

    /**
     * Main class of Digester Jar file. Uses command line parameters to determine instruction file, and runs whole program.
     *
     * @param args See Digester.jar -? to get a list of arguments
     * throws FileNotFoundException, IOException, ParseException, InterruptedException
     */
    public static void main(String[] args) throws Exception {

        GobiiLoaderConfig.GobiiLoaderConfigBuilder configBuilder = GobiiLoaderConfig.builder();

        //Section - Setup
        Options o = new Options()
                .addOption("v", "verbose", false, "Verbose output")
                .addOption("e", "errlog", true, "Error log override location")
                .addOption("r", "rootDir", true, "Fully qualified path to gobii root directory")
                .addOption("c", "config", true, "Fully qualified path to gobii configuration file")
                .addOption("h", "hdfFiles", true, "Fully qualified path to hdf files");
        LoaderGlobalConfigs.addOptions(o);
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cli = parser.parse(o, args);

            String rootDir;
            if (cli.hasOption("rootDir")) {
                rootDir = cli.getOptionValue("rootDir");
            } else {
                rootDir = "../";
            }
            if (cli.hasOption("verbose")) configBuilder.verbose(true);
            if (cli.hasOption("errLog")) configBuilder.errorLog(cli.getOptionValue("errLog"));
            if (cli.hasOption("config") && cli.getOptionValue("config") != null) {
                configBuilder.propertiesFile(cli.getOptionValue("config"));
            } else {
                configBuilder.propertiesFile(rootDir + "config/gobii-web.xml");
            }
            if (cli.hasOption("hdfFiles")) configBuilder.hdf5FilesPath(cli.getOptionValue("hdfFiles"));
            LoaderGlobalConfigs.setFromFlags(cli);
            args = cli.getArgs();//Remaining args passed through

            configBuilder.extractorScriptPath(rootDir + "extractors/");
            configBuilder.loaderScriptPath(rootDir + "loaders/");


        } catch (org.apache.commons.cli.ParseException exp) {
            new HelpFormatter().printHelp("java -jar Digester.jar ", "Also accepts input file directly after arguments\n" +
                    "Example: java -jar Digester.jar -c /home/jdl232/customConfig.properties -v /home/jdl232/testLoad.json", o, null, true);
            System.exit(2);
        }

        if (args.length == 0 || "".equals(args[0])) {
            Scanner s = new Scanner(System.in);
            System.out.println("Enter Loader Instruction File Location:");
            configBuilder.instructionFile(s.nextLine());
        } else {
            configBuilder.instructionFile(args[0]);
        }


        GobiiLoaderConfig config = configBuilder.build();

        GobiiLoader loader = new GobiiLoader(config);

        GobiiLoaderProcedure procedure = parseInstructionFile(config.getInstructionFile());
        loader.run(procedure);
    }

    private static void databaseValidation(Map<String, File> loaderInstructionMap, GobiiLoaderMetadata meta, GobiiCropConfig gobiiCropConfig) {
        DatabaseQuerier querier = new DatabaseQuerier(gobiiCropConfig.getServer(ServerType.GOBII_PGSQL));

        //If we're doing a DS upload and there is no DS_Marker
        if (loaderInstructionMap.containsKey(VARIANT_CALL_TABNAME) && loaderInstructionMap.containsKey(DS_MARKER_TABNAME) && !loaderInstructionMap.containsKey(MARKER_TABNAME)) {
            querier.checkMarkerInPlatform(loaderInstructionMap.get(DS_MARKER_TABNAME), meta.getPlatform().getId());
        }
        //If we're doing a DS upload and there is no DS_Sample
        if (loaderInstructionMap.containsKey(VARIANT_CALL_TABNAME) && loaderInstructionMap.containsKey(DS_SAMPLE_TABNAME) && !loaderInstructionMap.containsKey(SAMPLE_TABNAME)) {
            querier.checkDNARunInExperiment(loaderInstructionMap.get(DS_SAMPLE_TABNAME), meta.getExperiment().getId());
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

    private static GobiiExtractorInstruction createQCExtractInstruction(GobiiLoaderMetadata meta) {
        GobiiExtractorInstruction gobiiExtractorInstruction;
        ErrorLogger.logInfo("Digester", "qcCheck detected");
        ErrorLogger.logInfo("Digester", "Entering into the QC Subsection #1 of 3...");
        gobiiExtractorInstruction = new GobiiExtractorInstruction();
        gobiiExtractorInstruction.setContactEmail(meta.getContactEmail());
        gobiiExtractorInstruction.setContactId(meta.getContactId());
        gobiiExtractorInstruction.setGobiiCropType(meta.getGobiiCropType());
        gobiiExtractorInstruction.getMapsetIds().add(meta.getMapset().getId());
        gobiiExtractorInstruction.setQcCheck(true);
        ErrorLogger.logInfo("Digester", "Done with the QC Subsection #1 of 3!");
        return gobiiExtractorInstruction;
    }

    private static void setQCExtractPaths(GobiiLoaderProcedure proc, ConfigSettings configuration, String crop, String instructionFile) throws Exception {
        ErrorLogger.logInfo("Digester", "Entering into the QC Subsection #2 of 3...");
        GobiiDataSetExtract gobiiDataSetExtract = new GobiiDataSetExtract();
        gobiiDataSetExtract.setAccolate(false);  // It is unused/unsupported at the moment
        gobiiDataSetExtract.setDataSet(proc.getMetadata().getDataSet());
        gobiiDataSetExtract.setGobiiDatasetType(proc.getMetadata().getDatasetType());

        // According to Liz, the Gobii extract filter type is always "WHOLE_DATASET" for any QC job
        gobiiDataSetExtract.setGobiiExtractFilterType(GobiiExtractFilterType.WHOLE_DATASET);
        gobiiDataSetExtract.setGobiiFileType(GobiiFileType.HAPMAP);
        // It is going to be set by the Gobii web services
        gobiiDataSetExtract.setGobiiJobStatus(null);
        qcExtractInstruction.getDataSetExtracts().add(gobiiDataSetExtract);
        ErrorLogger.logInfo("Digester", "Done with the QC Subsection #2 of 3!");
    }

    private static void sendQCExtract(ConfigSettings configuration, String crop) throws Exception {
        ErrorLogger.logInfo("Digester", "Entering into the QC Subsection #3 of 3...");
        ExtractorInstructionFilesDTO extractorInstructionFilesDTOToSend = new ExtractorInstructionFilesDTO();
        extractorInstructionFilesDTOToSend.getGobiiExtractorInstructions().add(qcExtractInstruction);
        extractorInstructionFilesDTOToSend.setInstructionFileName("extractor_" + DateUtils.makeDateIdString());
        GobiiClientContext gobiiClientContext = GobiiClientContext.getInstance(configuration, crop, GobiiAutoLoginType.USER_RUN_AS);
        if (LineUtils.isNullOrEmpty(gobiiClientContext.getUserToken())) {
            ErrorLogger.logError("Digester", "Unable to log in with user " + GobiiAutoLoginType.USER_RUN_AS.toString());
            return;
        }
        String currentCropContextRoot = GobiiClientContext.getInstance(null, false).getCurrentCropContextRoot();
        gobiiUriFactory = new GobiiUriFactory(currentCropContextRoot);
        PayloadEnvelope<ExtractorInstructionFilesDTO> payloadEnvelope = new PayloadEnvelope<>(extractorInstructionFilesDTOToSend, GobiiProcessType.CREATE);
        GobiiEnvelopeRestResource<ExtractorInstructionFilesDTO, ExtractorInstructionFilesDTO> gobiiEnvelopeRestResourceForPost = new GobiiEnvelopeRestResource<>(gobiiUriFactory
                .resourceColl(RestResourceId.GOBII_FILE_EXTRACTOR_INSTRUCTIONS));
        PayloadEnvelope<ExtractorInstructionFilesDTO> extractorInstructionFileDTOResponseEnvelope = gobiiEnvelopeRestResourceForPost.post(ExtractorInstructionFilesDTO.class,
                payloadEnvelope);

        if (extractorInstructionFileDTOResponseEnvelope != null) {

            Header header = extractorInstructionFileDTOResponseEnvelope.getHeader();
            if (header.getStatus().isSucceeded()) {
                ErrorLogger.logInfo("Digester", "Extractor Request Sent");

            } else {

                String messages = extractorInstructionFileDTOResponseEnvelope.getHeader().getStatus().messages();

                for (HeaderStatusMessage currentStatusMesage : header.getStatus().getStatusMessages()) {
                    messages += (currentStatusMesage.getMessage()) + "; ";
                }

                ErrorLogger.logError("Digester", "Error sending extract request: " + messages);

            }
        } else {
            ErrorLogger.logInfo("Digester", "Error Sending Extractor Request");
        }
        ErrorLogger.logInfo("Digester", "Done with the QC Subsection #3 of 3!");
    }

    /**
     * Read ppd and nodups files to determine their length, and add the row corresponding to the key to the digester message status.
     * Assumes IFL was run with output of dstDir on key in instructionMap.
     *
     * @param state
     * @param loaderInstructionMap Map of key/location of loader instructions
     * @param dstDir               Destination directory for IFL call run on key's table
     * @param key                  Key in loaderInstructionMap
     * @return
     */
    private static GobiiLoaderState calculateTableStats(GobiiLoaderState state, Map<String, File> loaderInstructionMap, File dstDir, String key) {

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


        GobiiLoaderState.TableStats stats
                = new GobiiLoaderState.TableStats()
                .setLinesLoaded(loadedLines)
                .setExistingLines(existingLines)
                .setInvalidLines(invalidLines)
                .setTotalLines(totalLines)
                .setNoDupsFileExists(noDupsFileExists);

        state.getTableStats().put(key, stats);

        return state;
    }

    /**
     * Returns a human readable name for the job.
     *
     * @param procedure the GobiiLoaderProcedure for the job
     * @return a human readable name for the job
     */
    private static String getJobReadableIdentifier(GobiiLoaderProcedure procedure) {
        String cropName = procedure.getMetadata().getGobiiCropType();
        cropName = cropName.charAt(0) + cropName.substring(1).toLowerCase();// MAIZE -> Maize
        String jobName = "[GOBII - Loader]: " + cropName + " - digest of \"" + getSourceFileName(procedure.getMetadata().getGobiiFile()) + "\"";
        return jobName;
    }

    /**
     * Converts the File input into the FIRST of the source files.
     *
     * @param file Reference to Instruction's File object.
     * @return String representation of first of source files
     */
    public static String getSourceFileName(GobiiFile file) {
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
    private static String getLogName(GobiiLoaderProcedure procedure) {
        String destination = procedure.getMetadata().getGobiiFile().getDestination();
        String table = procedure.getInstructions().get(0).getTable();
        return destination + "/" + procedure.getMetadata().getGobiiCropType() + "_Table-" + table + ".log";
    }

    /**
     * Generates a log file location given a crop name, crop type, and process ID. (Given by the process calling this method).
     * <p>
     * Currently works by placing logs in the intermediate file directory.
     *
     * @return The logfile location for this process
     */
    private static String getLogName(GobiiLoaderProcedure procedure, String process) {
        String destination = procedure.getMetadata().getGobiiFile().getDestination();
        return destination + "/" + procedure.getMetadata().getGobiiCropType() + "_Process-" + process + ".log";
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

    /**
     * Updates Postgresql through the webservices to update the DataSet's monetDB and HDF5File references.
     *
     * @param config         Configuration settings, used to determine connections
     * @param cropName       Name of the crop
     * @param dataSetId      Data set to update
     * @param monetTableName Name of the table in the monetDB database for this dataset.
     * @param hdfFileName    Name of the HDF5 file for this dataset (Note, these should be obvious)
     */
    public static void updateValues(ConfigSettings config, String cropName, Integer dataSetId, String monetTableName, String hdfFileName) {
        try {
            // set up authentication and so forth
            // you'll need to get the current from the instruction file
            GobiiClientContext context = GobiiClientContext.getInstance(config, cropName, GobiiAutoLoginType.USER_RUN_AS);

            if (LineUtils.isNullOrEmpty(context.getUserToken())) {
                logError("Digester", "Unable to login with user " + GobiiAutoLoginType.USER_RUN_AS.toString());
                return;
            }

            String currentCropContextRoot = context.getInstance(null, false).getCurrentCropContextRoot();
            GobiiUriFactory gobiiUriFactory = new GobiiUriFactory(currentCropContextRoot);

            RestUri projectsUri = gobiiUriFactory
                    .resourceByUriIdParam(RestResourceId.GOBII_DATASETS);
            projectsUri.setParamValue("id", dataSetId.toString());
            GobiiEnvelopeRestResource<DataSetDTO, DataSetDTO> gobiiEnvelopeRestResourceForDatasets = new GobiiEnvelopeRestResource<>(projectsUri);
            PayloadEnvelope<DataSetDTO> resultEnvelope = gobiiEnvelopeRestResourceForDatasets
                    .get(DataSetDTO.class);

            DataSetDTO dataSetResponse;
            if (!resultEnvelope.getHeader().getStatus().isSucceeded()) {
                System.out.println();
                logError("Digester", "Data doUpdate response response errors");
                for (HeaderStatusMessage currentStatusMesage : resultEnvelope.getHeader().getStatus().getStatusMessages()) {
                    logError("HeaderError", currentStatusMesage.getMessage());
                }
                return;
            } else {
                dataSetResponse = resultEnvelope.getPayload().getData().get(0);
            }

            dataSetResponse.setDataTable(monetTableName);
            dataSetResponse.setDataFile(hdfFileName);

            resultEnvelope = gobiiEnvelopeRestResourceForDatasets
                    .put(DataSetDTO.class, new PayloadEnvelope<>(dataSetResponse, GobiiProcessType.UPDATE));


            //dataSetResponse = dtoProcessor.process(dataSetResponse);
            // if you didn't succeed, do not pass go, but do log errors to your log file
            if (!resultEnvelope.getHeader().getStatus().isSucceeded()) {
                logError("Digester", "Data doUpdate response response errors");
                for (HeaderStatusMessage currentStatusMesage : resultEnvelope.getHeader().getStatus().getStatusMessages()) {
                    logError("HeaderError", currentStatusMesage.getMessage());
                }
                return;
            }
        } catch (Exception e) {
            logError("Digester", "Exception while referencing data sets in Postgresql", e);
            return;
        }
    }

    /**
     * Given a string key, determine if the table is one-to-one with relation to the input file size.
     * If not, several metrics become meaningless.
     *
     * @param tableKey
     * @return true if the table will have different PPD rows than input rows
     */
    private static boolean isVariableLengthTable(String tableKey) {
        return tableKey.contains("_prop");
    }

    /**
     * Deletes all files in directory that contain '.tablename' suffix
     *
     * @param directory
     * @param tableName
     */
    private static void deleteIFLFiles(File directory, String tableName) {
        File[] fileList = directory.listFiles();
        if (fileList == null) return;
        for (File f : fileList) {
            if (f.getName().endsWith("." + tableName)) {
                rmIfExist(f);
            }
        }
    }
}