package org.gobiiproject.gobiidomain.services.gdmv3;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.gobiiproject.gobiidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.cvnames.CvGroupTerm;
import org.gobiiproject.gobiimodel.cvnames.JobType;
import org.gobiiproject.gobiimodel.dto.gdmv3.DnaRunUploadRequestDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.JobDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.MarkerUploadRequestDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.templates.DnaRunTemplateDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.templates.MarkerTemplateDTO;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.*;
import org.gobiiproject.gobiimodel.entity.*;
import org.gobiiproject.gobiimodel.modelmapper.AspectMapper;
import org.gobiiproject.gobiimodel.modelmapper.ModelMapper;
import org.gobiiproject.gobiimodel.types.*;
import org.gobiiproject.gobiimodel.utils.IntegerUtils;
import org.gobiiproject.gobiisampletrackingdao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.nio.charset.StandardCharsets;
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
    private JobDao jobDao;

    final ObjectMapper mapper = new ObjectMapper();

    final String loadType = "SAMPLES";

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
        BufferedReader br;
        LoaderInstruction loaderInstruction = new LoaderInstruction();
        loaderInstruction.setLoadType(loadType);
        loaderInstruction.setAspects(new HashMap<>());

        String fileHeader;
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

        // Get a new Job object
        Job job = getNewJob();
        job.setSubmittedBy(createdBy);

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
        HashSet<String> propertyFields = new HashSet<>(
            Arrays.asList("dnaRunProperties", "dnaSampleProperties", "germplasmProperties"));
        Map<String, List<String>> fileColumnsApiFieldsMap =
            Utils.getFileColumnsApiFieldsMap(dnaRunTemplateMap, propertyFields);

        // Map for Aspect values to each api field.
        Map<String, Object> aspectValues = new HashMap<>();

        //Read Header
        InputStream fileInputStream = new ByteArrayInputStream(dnaRunFile);
        try {
            br = new BufferedReader(
                new InputStreamReader(fileInputStream, StandardCharsets.UTF_8));
            fileHeader = br.readLine();
        }
        catch (IOException io) {
            throw new GobiiDomainException(
                GobiiStatusLevel.ERROR,
                GobiiValidationStatusType.BAD_REQUEST,
                "No able to read file header");
        }

        String[] fileColumns = fileHeader.split("\t");

        Map<String, Cv> dnaRunPropertiesCvsMap = new HashMap<>();
        Map<String, Cv> dnaSamplePropertiesCvsMap = new HashMap<>();
        Map<String, Cv> germplasmPropertiesCvsMap = new HashMap<>();
        Map<String, ColumnAspect> dnaRunPropertiesAspects = new HashMap<>();
        Map<String, ColumnAspect> dnaSamplePropertiesAspects = new HashMap<>();
        Map<String, ColumnAspect> germplasmPropertiesAspects = new HashMap<>();

        for(int i = 0; i < fileColumns.length; i++) {
            String fileColumn = fileColumns[i];
            ColumnAspect columnAspect = new ColumnAspect(1, i);

            for(String apiFieldName : fileColumnsApiFieldsMap.get(fileColumn)) {

                // Check for dna run properties fields
                if (apiFieldName.startsWith("dnaRunProperties.")) {
                    if (dnaRunTable.getDnaRunProperties() == null) {

                        // Initialize and set json aspect for properties field.
                        JsonAspect jsonAspect = new JsonAspect();
                        jsonAspect.setJsonMap(dnaRunPropertiesAspects);
                        aspectValues.put("dnaRunProperties", jsonAspect);

                        List<Cv> dnaRunPropertiesCvList = cvDao.getCvListByCvGroup(
                            CvGroupTerm.CVGROUP_DNARUN_PROP.getCvGroupName(),
                            null);
                        dnaRunPropertiesCvsMap = Utils.mapCvNames(dnaRunPropertiesCvList);
                    }
                    String propertyName = apiFieldName
                        .replace("dnaRunProperties.", "");

                    if (dnaRunPropertiesCvsMap.containsKey(propertyName)) {
                        String propertyId = dnaRunPropertiesCvsMap
                            .get(propertyName)
                            .getCvId().toString();
                        dnaRunPropertiesAspects.put(propertyId, columnAspect);
                    }
                } else if (apiFieldName.startsWith("dnaSampleProperties.")) {
                    if (dnaSampleTable.getDnaSampleProperties() == null) {

                        JsonAspect jsonAspect = new JsonAspect();
                        jsonAspect.setJsonMap(dnaSamplePropertiesAspects);
                        aspectValues.put("dnaSampleProperties", jsonAspect);

                        List<Cv> dnaSamplePropertiesCvList = cvDao.getCvListByCvGroup(
                            CvGroupTerm.CVGROUP_DNASAMPLE_PROP.getCvGroupName(),
                            null);
                        dnaSamplePropertiesCvsMap = Utils.mapCvNames(dnaSamplePropertiesCvList);
                    }
                    String propertyName = apiFieldName
                        .replace("dnaSampleProperties.", "");

                    if (dnaSamplePropertiesCvsMap.containsKey(propertyName)) {
                        String propertyId = dnaSamplePropertiesCvsMap
                            .get(propertyName)
                            .getCvId().toString();
                        dnaSamplePropertiesAspects.put(propertyId, columnAspect);
                    }
                } else if (apiFieldName.startsWith("germplasmProperties.")) {
                    if (germplasmTable.getGermplasmProperties() == null) {

                        JsonAspect jsonAspect = new JsonAspect();
                        jsonAspect.setJsonMap(germplasmPropertiesAspects);
                        aspectValues.put("germplasmProperties", jsonAspect);

                        List<Cv> germplasmPropertiesCvList = cvDao.getCvListByCvGroup(
                            CvGroupTerm.CVGROUP_GERMPLASM_PROP.getCvGroupName(),
                            null);
                        germplasmPropertiesCvsMap = Utils.mapCvNames(germplasmPropertiesCvList);
                    }
                    String propertyName = apiFieldName
                        .replace("germplasmProperties.", "");

                    if (germplasmPropertiesCvsMap.containsKey(propertyName)) {
                        String propertyId = germplasmPropertiesCvsMap
                            .get(propertyName)
                            .getCvId().toString();
                        germplasmPropertiesAspects.put(propertyId, columnAspect);
                    }
                } else {
                    aspectValues.put(
                        apiFieldName,
                        columnAspect);
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

        JobDTO jobDTO = new JobDTO();
        jobDao.create(job);
        ModelMapper.mapEntityToDto(job, jobDTO);
        return jobDTO;
    }

    private Job getNewJob() throws GobiiException {
        String jobName = UUID.randomUUID().toString().replace("-", "");
        Job job = new Job();
        job.setJobName(jobName);
        job.setMessage("Submitted dnarun upload job");
        // Get payload type
        Cv payloadType = cvDao.getCvs(GobiiLoaderPayloadTypes.SAMPLES.getTerm(),
            CvGroupTerm.CVGROUP_PAYLOADTYPE.getCvGroupName(),
            GobiiCvGroupType.GROUP_TYPE_SYSTEM).get(0);
        job.setPayloadType(payloadType);

        // Get jobstatus as pending
        Cv jobStatus = cvDao.getCvs(
            GobiiJobStatus.PENDING.getCvTerm(),
            CvGroupTerm.CVGROUP_JOBSTATUS.getCvGroupName(),
            GobiiCvGroupType.GROUP_TYPE_SYSTEM).get(0);
        job.setStatus(jobStatus);

        job.setSubmittedDate(new Date());
        // Get load type
        Cv jobType = cvDao.getCvs(
            JobType.CV_JOBTYPE_LOAD.getCvName(),
            CvGroupTerm.CVGROUP_JOBTYPE.getCvGroupName(),
            GobiiCvGroupType.GROUP_TYPE_SYSTEM).get(0);
        job.setType(jobType);
        return job;
    }


}
