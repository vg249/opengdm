package org.gobiiproject.gobiiprocess.digester.aspectsdigest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.type.MapType;
import lombok.extern.slf4j.Slf4j;
import org.gobiiproject.gobiidomain.GobiiDomainException;
import org.gobiiproject.gobiidomain.services.gdmv3.Utils;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.cvnames.CvGroupTerm;
import org.gobiiproject.gobiimodel.dto.gdmv3.DnaRunUploadRequestDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.templates.DnaRunTemplateDTO;
import org.gobiiproject.gobiimodel.dto.instructions.loader.DigesterResult;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.*;
import org.gobiiproject.gobiimodel.entity.Cv;
import org.gobiiproject.gobiimodel.entity.Experiment;
import org.gobiiproject.gobiimodel.entity.LoaderTemplate;
import org.gobiiproject.gobiimodel.entity.Project;
import org.gobiiproject.gobiimodel.modelmapper.AspectMapper;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.gobiiproject.gobiimodel.utils.AspectUtils;
import org.gobiiproject.gobiimodel.utils.IntegerUtils;
import org.gobiiproject.gobiiprocess.digester.GobiiDigester;
import org.gobiiproject.gobiiprocess.digester.utils.FileUtils;
import org.gobiiproject.gobiiprocess.spring.SpringContextLoaderSingleton;
import org.gobiiproject.gobiisampletrackingdao.CvDao;
import org.gobiiproject.gobiisampletrackingdao.ExperimentDao;
import org.gobiiproject.gobiisampletrackingdao.LoaderTemplateDao;
import org.gobiiproject.gobiisampletrackingdao.ProjectDao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

@Slf4j
public class SamplesDigest extends AspectDigest {

    final HashSet<String> propertyFields = new HashSet<>(Arrays.asList(
        "dnaRunProperties",
        "dnaSampleProperties",
        "germplasmProperties"));

    final Map<String, CvGroupTerm> propertyFieldsCvGroupMap = Map.of(
        "dnaRunProperties", CvGroupTerm.CVGROUP_DNARUN_PROP,
        "dnaSampleProperties", CvGroupTerm.CVGROUP_DNASAMPLE_PROP,
        "germplasmProperties", CvGroupTerm.CVGROUP_GERMPLASM_PROP);

    private ProjectDao projectDao;
    private ExperimentDao experimentDao;
    private CvDao cvDao;
    private LoaderTemplateDao loaderTemplateDao;

    SamplesDigest(LoaderInstruction loaderInstruction,
                  ConfigSettings configSettings) throws GobiiException {
        super(loaderInstruction, configSettings);
        this.projectDao = SpringContextLoaderSingleton.getInstance().getBean(ProjectDao.class);
        this.experimentDao =
            SpringContextLoaderSingleton.getInstance().getBean(ExperimentDao.class);
        this.cvDao = SpringContextLoaderSingleton.getInstance().getBean(CvDao.class);
    }

    public DigesterResult digest() {

        File inputFile = new File(loaderInstruction.getInputFile());

        List<File> filesToDigest = new ArrayList<>();

        try {
            if (inputFile.exists()) {

                if(inputFile.isDirectory()) {
                    File[] filesArray = inputFile.listFiles();
                    if(filesArray != null) {
                        filesToDigest.addAll(Arrays.asList(filesArray));
                    }
                }
                else if(inputFile.getName().endsWith(FileUtils.TAR_GUNZIP_EXTENSION) &&
                    inputFile.getName().endsWith(FileUtils.GUNZIP_EXTENSION)) {
                    filesToDigest = FileUtils.extractTarGunZipFile(inputFile);
                }
                else if(inputFile.getName().endsWith(FileUtils.GUNZIP_EXTENSION)) {
                    filesToDigest.add(FileUtils.extractGunZipFile(inputFile));
                }
                else {
                    filesToDigest.add(inputFile);
                }

                // Digest each file
                for(File fileToDigest : filesToDigest) {

                    // Ignore non text file
                    if(!FileUtils.isFileTextFile(fileToDigest)) {
                        continue;
                    }

                    Map<String, Table> aspects = getAspects(fileToDigest);
                    Map<String, File> intermediateDigestFileMap = masticate(aspects);

                    List<String> loadOrder = getLoadOrder();
                    if(loadOrder == null || loadOrder.size() <= 0) {
                        loadOrder = new ArrayList<>(intermediateDigestFileMap.keySet());
                    }

                    DigesterResult digesterResult =
                        new DigesterResult(
                            true,
                            false,
                            loaderInstruction.getCropType(),
                            this.cropConfig,
                            loaderInstruction.getOutputDir(),
                            loaderInstruction.getLoadType(),
                            intermediateDigestFileMap,
                            loadOrder,
                            loaderInstruction.getDatasetType(),
                            jobStatus,
                            null,
                            loaderInstruction.getJobName());
                    digesterResult.setContactEmail(loaderInstruction.getContactEmail());
                    return digesterResult;
                }
            }
            else {
                throw new GobiiException("Input file does not exist");
            }
        }
        catch (Exception e) {
            log.error(e.getMessage());
            throw new GobiiException(e);
        }

        return null;
    }

    /**
     * Get aspects mapped for the given dnarun file headers
     * @return
     */
    private Map<String, Table> getAspects(File fileToDigest) throws GobiiException {

        // Set Marker Aspects
        Map<String, Table> aspects = new HashMap<>();

        Scanner fileScanner;

        DnaRunTemplateDTO dnaRunTemplate;
        DnaRunTable dnaRunTable = new DnaRunTable();
        DnaSampleTable dnaSampleTable = new DnaSampleTable();
        GermplasmTable germplasmTable = new GermplasmTable();

        // new status to set for new dna runs
        Cv newStatus = cvDao.getNewStatus();

        DnaRunUploadRequestDTO uploadRequest = mapper.convertValue(
            loaderInstruction.getUserRequest(),
            DnaRunUploadRequestDTO.class);

        dnaRunTemplate = (DnaRunTemplateDTO) getLoaderTemplate(
            uploadRequest.getDnaRunTemplateId(),
            DnaRunTemplateDTO.class);

        Integer headerLineNumber = dnaRunTemplate.getHeaderLineNumber();

        String fileHeaderLine="";

        try {
            fileScanner = new Scanner(fileToDigest);
        }
        catch (FileNotFoundException e) {
            throw new GobiiException("Input file does not exist");
        }
        while(headerLineNumber > 0 && fileScanner.hasNextLine()) {
            fileHeaderLine = fileScanner.nextLine();
            headerLineNumber--;
        }

        // Map for Aspect values to each api field.
        Map<String, Object> aspectValues = new HashMap<>();

        String[] fileHeaders =
            fileHeaderLine.split(dnaRunTemplate.getFileSeparator());


        //Get API fields Entity Mapping
        Map<String, List<String>> fileColumnsApiFieldsMap =
            getFileColumnsApiFieldsMap(dnaRunTemplate, propertyFields);

        // To memoize cv for each property group for each table.
        Map<String, Map<String, Cv>> propertiesCvMaps = new HashMap<>();

        // Set Aspect for each file column
        for(int i = 0; i < fileHeaders.length; i++) {
            String fileHeader = fileHeaders[i];
            ColumnAspect columnAspect = new ColumnAspect(1, i);

            for(String apiFieldName : fileColumnsApiFieldsMap.get(fileHeader)) {

                String propertyGroupName = null;
                if(apiFieldName.contains(propertyGroupSeparator)) {
                    propertyGroupName =
                        apiFieldName.substring(0, apiFieldName.indexOf(propertyGroupSeparator));
                }

                // Check for dna run properties fields
                if (propertyFields.contains(propertyGroupName)) {
                    String propertyName =
                        apiFieldName.replace(propertyGroupName+propertyGroupSeparator, "");
                    setPropertyAspect(
                        aspectValues,
                        columnAspect,
                        propertiesCvMaps,
                        propertyName,
                        propertyGroupName,
                        propertyFieldsCvGroupMap);
                } else {
                    aspectValues.put(apiFieldName, columnAspect);
                }
            }
        }

        // Set Project Id in DnaRun and DnaSample Table Aspects
        if(!IntegerUtils.isNullOrZero(uploadRequest.getProjectId())) {
            Project project = projectDao.getProject(uploadRequest.getProjectId());
            if (project == null) {
                throw new GobiiDomainException(GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.BAD_REQUEST,
                    "Invalid Project");
            }
            dnaRunTable.setProjectId(project.getProjectId().toString());
            dnaSampleTable.setProjectId(project.getProjectId().toString());
        }

        // Set Experiment Id in DnaRun Table Aspects
        if(!IntegerUtils.isNullOrZero(uploadRequest.getExperimentId())) {
            Experiment experiment =
                experimentDao.getExperiment(uploadRequest.getExperimentId());
            if (experiment == null) {
                throw new GobiiDomainException(GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.BAD_REQUEST,
                    "Invalid Experiment");
            }
            dnaRunTable.setExperimentId(experiment.getExperimentId().toString());
        }

        boolean dnaRunMapped =
            AspectMapper.mapTemplateToAspects(dnaRunTemplate, dnaRunTable, aspectValues);

        boolean dnaSampleMapped =
            AspectMapper.mapTemplateToAspects(dnaRunTemplate, dnaSampleTable, aspectValues);

        boolean germplasmMapped =
            AspectMapper.mapTemplateToAspects(dnaRunTemplate, germplasmTable, aspectValues);

        if(dnaRunMapped) {
            String dnaRunTableName = AspectUtils.getTableName(DnaRunTable.class);
            dnaRunTable.setStatus(newStatus.getCvId().toString());
            aspects.put(dnaRunTableName, dnaRunTable);
        }

        if(dnaSampleMapped) {
            String dnaSampleTableName = AspectUtils.getTableName(DnaSampleTable.class);
            dnaSampleTable.setStatus(newStatus.getCvId().toString());
            aspects.put(dnaSampleTableName, dnaSampleTable);
        }

        if(germplasmMapped) {
            String germplasmTableName = AspectUtils.getTableName(GermplasmTable.class);
            germplasmTable.setStatus(newStatus.getCvId().toString());
            aspects.put(germplasmTableName, germplasmTable);
        }

        return aspects;

    }


}
