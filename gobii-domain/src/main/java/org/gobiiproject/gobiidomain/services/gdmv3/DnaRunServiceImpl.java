package org.gobiiproject.gobiidomain.services.gdmv3;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.gobiiproject.gobiidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.cvnames.CvGroupTerm;
import org.gobiiproject.gobiimodel.dto.gdmv3.DnaRunUploadRequestDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.JobDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.templates.DnaRunTemplateDTO;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.*;
import org.gobiiproject.gobiimodel.entity.*;
import org.gobiiproject.gobiimodel.modelmapper.AspectMapper;
import org.gobiiproject.gobiimodel.types.*;
import org.gobiiproject.gobiimodel.utils.IntegerUtils;
import org.gobiiproject.gobiisampletrackingdao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@SuppressWarnings("unchecked")
@Transactional
public class DnaRunServiceImpl implements DnaRunService {

    @Autowired
    private LoaderTemplateDao loaderTemplateDao;

    @Autowired
    private ContactDao contactDao;

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private ExperimentDao experimentDao;

    @Autowired
    private CvDao cvDao;

    @Autowired
    private JobService jobService;

    final ObjectMapper mapper = new ObjectMapper();

    final String loadType = "SAMPLES";

    final String propertyGroupSeparator = ".";

    final HashSet<String> propertyFields = new HashSet<>(Arrays.asList(
        "dnaRunProperties",
        "dnaSampleProperties",
        "germplasmProperties"));

    final Map<String, CvGroupTerm> propertyFieldsCvGroupMap = Map.of(
        "dnaRunProperties", CvGroupTerm.CVGROUP_DNARUN_PROP,
        "dnaSampleProperties", CvGroupTerm.CVGROUP_DNASAMPLE_PROP,
        "germplasmProperties", CvGroupTerm.CVGROUP_GERMPLASM_PROP);

    /**
     * Uploads dnaruns in the file to the database.
     * Also, loads dna samples and germplasms when provided in the same file.
     *
     * @param dnaRunFile            Dnarun input file byte array
     * @param dnaRunUploadRequest   Request object with meta data and template
     * @param cropType              Crop type to which the dnaruns need to uploaded
     * @return {@link JobDTO}   when dnarun loader job is successfully submitted.
     * @throws GobiiException   Gobii Exception for bad request or if any run time system error
     */
    @Override
    public JobDTO loadDnaRuns(byte[] dnaRunFile,
                              DnaRunUploadRequestDTO dnaRunUploadRequest,
                              String cropType) throws GobiiException {
        LoaderInstruction loaderInstruction = new LoaderInstruction();
        loaderInstruction.setLoadType(loadType);
        loaderInstruction.setAspects(new HashMap<>());

        Map<String, Object> dnaRunTemplateMap;
        DnaRunTemplateDTO dnaRunTemplate;

        loaderInstruction.setCropType(cropType);

        // Set Marker Aspects
        Map<String, Object> aspects = new HashMap<>();

        DnaRunTable dnaRunTable = new DnaRunTable();
        DnaSampleTable dnaSampleTable = new DnaSampleTable();
        GermplasmTable germplasmTable = new GermplasmTable();

        // Read Dnarun load template
        LoaderTemplate loaderTemplate = loaderTemplateDao.getById(
            dnaRunUploadRequest.getDnaRunTemplateId());
        try {
            dnaRunTemplateMap = mapper.treeToValue(
                loaderTemplate.getTemplate(), HashMap.class);
            dnaRunTemplate = mapper.treeToValue(
                loaderTemplate.getTemplate(),
                DnaRunTemplateDTO.class);
        }
        catch (JsonProcessingException jE) {
            throw new GobiiDomainException(
                GobiiStatusLevel.ERROR,
                GobiiValidationStatusType.BAD_REQUEST,
                "Invalid dnarun template object.");
        }

        // Get user submitting the load
        String userName = ContactService.getCurrentUser();
        Contact createdBy = contactDao.getContactByUsername(userName);

        Cv newStatus = cvDao.getCvs(
            "new",
            CvGroupTerm.CVGROUP_STATUS.getCvGroupName(),
            GobiiCvGroupType.GROUP_TYPE_SYSTEM).get(0);

        // Get a new Job object for samples loading
        JobDTO jobDTO = new JobDTO();
        jobDTO.setPayload(GobiiLoaderPayloadTypes.SAMPLES.getTerm());
        JobDTO job = jobService.createLoaderJob(jobDTO);

        String jobName = job.getJobName();

        // Set contact email in loader instruction
        loaderInstruction.setContactEmail(createdBy.getEmail());

        //Set Input file
        String inputFilePath =
            Utils.writeInputFile(dnaRunFile, "samples.txt",jobName, cropType);
        loaderInstruction.setInputFile(inputFilePath);

        //Set output dir
        String outputFilesDir = Utils.getOutputDir(jobName, cropType);
        loaderInstruction.setOutputDir(outputFilesDir);

        //Get API fields Entity Mapping
        Map<String, List<String>> fileColumnsApiFieldsMap =
            Utils.getFileColumnsApiFieldsMap(dnaRunTemplateMap, propertyFields);

        // Map for Aspect values to each api field.
        Map<String, Object> aspectValues = new HashMap<>();

        String[] fileColumns = Utils.getHeaders(dnaRunFile);

        Map<String, Map<String, Cv>> propertiesCvMaps = new HashMap<>();

        // Set Aspect for each file column
        for(int i = 0; i < fileColumns.length; i++) {
            String fileColumn = fileColumns[i];
            ColumnAspect columnAspect = new ColumnAspect(1, i);

            for(String apiFieldName : fileColumnsApiFieldsMap.get(fileColumn)) {

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
                        propertyGroupName);
                } else {
                    aspectValues.put(apiFieldName, columnAspect);
                }
            }
        }

        // Set Project Id in DnaRun and DnaSample Table Aspects
        if(!IntegerUtils.isNullOrZero(dnaRunUploadRequest.getProjectId())) {
            Project project = projectDao.getProject(dnaRunUploadRequest.getProjectId());
            if (project == null) {
                throw new GobiiDomainException(GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.BAD_REQUEST,
                    "Invalid Project");
            }
            dnaRunTable.setProjectId(project.getProjectId().toString());
            dnaSampleTable.setProjectId(project.getProjectId().toString());
        }

        // Set Experiment Id in DnaRun Table Aspects
        if(!IntegerUtils.isNullOrZero(dnaRunUploadRequest.getExperimentId())) {
            Experiment experiment =
                experimentDao.getExperiment(dnaRunUploadRequest.getExperimentId());
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
            String dnaRunTableName = Utils.getTableName(DnaRunTable.class);
            dnaRunTable.setStatus(newStatus.getCvId().toString());
            aspects.put(dnaRunTableName, dnaRunTable);
        }

        if(dnaSampleMapped) {
            String dnaSampleTableName = Utils.getTableName(DnaSampleTable.class);
            dnaSampleTable.setStatus(newStatus.getCvId().toString());
            aspects.put(dnaSampleTableName, dnaSampleTable);
        }

        if(germplasmMapped) {
            String germplasmTableName = Utils.getTableName(GermplasmTable.class);
            germplasmTable.setStatus(newStatus.getCvId().toString());
            aspects.put(germplasmTableName, germplasmTable);
        }

        loaderInstruction.setAspects(aspects);

        // Write instruction file
        Utils.writeInstructionFile(loaderInstruction, jobName, cropType);

        return jobDTO;
    }

    private Map<String, Cv> getCvMapByTerm(CvGroupTerm cvGroupTerm) throws GobiiException {
        List<Cv> dnaRunPropertiesCvList = cvDao.getCvListByCvGroup(
            cvGroupTerm.getCvGroupName(),
            null);
        return Utils.mapCvNames(dnaRunPropertiesCvList);
    }

    private void setPropertyAspect(Map<String, Object> aspectValues,
                                   ColumnAspect columnAspect,
                                   Map<String, Map<String, Cv>> propertiesCvMaps,
                                   String propertyName,
                                   String propertyGroup
    ) throws GobiiException {

        JsonAspect jsonAspect;
        Map<String, Cv> cvMap;

        if (!aspectValues.containsKey(propertyGroup)) {
            // Initialize and set json aspect for properties field.
            jsonAspect = new JsonAspect();
            aspectValues.put(propertyGroup, jsonAspect);
        }
        else {
            jsonAspect = ((JsonAspect)aspectValues.get(propertyGroup));
        }

        if(!propertiesCvMaps.containsKey(propertyGroup)) {
            cvMap = getCvMapByTerm(propertyFieldsCvGroupMap.get(propertyGroup));
            propertiesCvMaps.put(propertyGroup, cvMap);
        }
        else {
            cvMap = propertiesCvMaps.get(propertyGroup);
        }
        if (cvMap.containsKey(propertyName)) {
            String propertyId = cvMap
                .get(propertyName)
                .getCvId()
                .toString();
            jsonAspect.getJsonMap().put(propertyId, columnAspect);
        }

    }
}
