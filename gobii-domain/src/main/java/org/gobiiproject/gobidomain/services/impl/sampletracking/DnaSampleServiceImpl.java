package org.gobiiproject.gobidomain.services.impl.sampletracking;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobidomain.services.*;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.cvnames.CvGroup;
import org.gobiiproject.gobiimodel.cvnames.JobPayloadType;
import org.gobiiproject.gobiimodel.cvnames.JobProgressStatusType;
import org.gobiiproject.gobiimodel.cvnames.JobType;
import org.gobiiproject.gobiimodel.dto.entity.auditable.ContactDTO;
import org.gobiiproject.gobiimodel.dto.entity.auditable.sampletracking.DnaSampleDTO;
import org.gobiiproject.gobiimodel.dto.entity.auditable.sampletracking.ProjectDTO;
import org.gobiiproject.gobiimodel.dto.entity.children.PropNameId;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.JobDTO;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.JobStatusDTO;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.ProjectSamplesDTO;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.SampleMetadataDTO;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiFileColumn;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderInstruction;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderMetadata;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderProcedure;
import org.gobiiproject.gobiimodel.entity.Cv;
import org.gobiiproject.gobiimodel.modelmapper.EntityFieldBean;
import org.gobiiproject.gobiimodel.modelmapper.ModelMapper;
import org.gobiiproject.gobiimodel.types.*;
import org.gobiiproject.gobiisampletrackingdao.CvDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class DnaSampleServiceImpl implements  DnaSampleService {

    Logger LOGGER = LoggerFactory.getLogger(DnaSampleServiceImpl.class);

    @Autowired
    private CvDao cvDao;

    @Autowired
    private ProjectService sampleTrackingProjectService;

    @Autowired
    private ContactService contactService;

    @Autowired
    private FilesService fileService = null;

    @Autowired
    private JobService jobService = null;

    @Override
    public ProjectSamplesDTO createSamples(ProjectSamplesDTO projectSamplesDTO)  throws GobiiDomainException {

        ProjectSamplesDTO returnVal = null;

        try {

            return projectSamplesDTO;

        } catch (Exception e) {

            LOGGER.error("Gobii service error", e);
            throw new GobiiDomainException(e);

        }
    }

    /**
     * Upload sample input file to the GDM crop database.
     * An instruction file for the sample upload job is created and posted in the instruction file folder
     * which will be fetched by the Cron Job running in compute node.
     * An entry for the job is also added to the backend database to keep track of the job.
     *
     * Input file should be a tab delimited file with header as its first row.
     *
     * The required fields for the prop tables are fetched from prop-table.properties file in config folder
     * TODO: The file propcolumn-required.properties was created by referring to dnasample_prop.nmap and germplasm_prop.name
     *   file used by the ifl(scripts to aid data loading). This might cause problems in future as there
     *   are two source of reference for required fields and both needs to be updated when there is a change.
     *   A request need to be raised to make Loader UI and IFL to use the same properties file in config folder.
     * @param is - input stream for the sample
     * @param sampleMetadata -  Meta data for sample upload, projectId and map object to map the
     *                       input file headers to the gdm system sample properties.
     * @param cropType - The crop database to which the samples needs to be uploaded
     * @return Job sttatus object with job id and other relevant details
     */
    @Override
    public JobDTO uploadSamples(InputStream is, SampleMetadataDTO sampleMetadata, String cropType) {

        BufferedReader br;

        GobiiLoaderProcedure gobiiLoaderProcedure = new GobiiLoaderProcedure();

        GobiiLoaderMetadata gobiiLoaderMetadata = new GobiiLoaderMetadata();

        List<GobiiLoaderInstruction> gobiiLoaderInstructionList = new ArrayList<>();

        JobDTO dnasampleLoadJob  = new JobStatusDTO();

        try {

            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            ContactDTO userContact = this.contactService.getContactByUserName(userName);

            gobiiLoaderMetadata.setContactEmail(userContact.getEmail());

            dnasampleLoadJob.setSubmittedBy(userContact.getContactId());
            dnasampleLoadJob.setSubmittedDate(new Date(new Date().getTime()));
            this.createDnaSampleUploadJob(dnasampleLoadJob);

            gobiiLoaderMetadata.setGobiiCropType(cropType);

            gobiiLoaderMetadata.setJobPayloadType(JobPayloadType.CV_PAYLOADTYPE_SAMPLES);

            PropNameId projectPropName = new PropNameId();
            if(sampleMetadata.getProjectId() == null) {
                throw new GobiiDomainException(
                        GobiiStatusLevel.ERROR,
                        GobiiValidationStatusType.MISSING_REQUIRED_VALUE,
                        "Project id is required");
            }
            ProjectDTO projectDTO = (ProjectDTO) sampleTrackingProjectService.getProjectById(
                    sampleMetadata.getProjectId());
            projectPropName.setId(projectDTO.getProjectId());
            projectPropName.setName(projectDTO.getProjectName());
            gobiiLoaderMetadata.setProject(projectPropName);

            br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String fileHeader = br.readLine();

            Map<String, List<GobiiFileColumn>> fileColumnByTableMap = this.mapFileCoulumnToGobiiTable(
                    fileHeader,
                    sampleMetadata);

            for(String tableName : fileColumnByTableMap.keySet()) {
                GobiiLoaderInstruction gobiiLoaderInstruction = new GobiiLoaderInstruction();
                gobiiLoaderInstruction.setTable(tableName);
                gobiiLoaderInstruction.setGobiiFileColumns(fileColumnByTableMap.get(tableName));
                gobiiLoaderInstructionList.add(gobiiLoaderInstruction);
            }



            gobiiLoaderProcedure.setMetadata(gobiiLoaderMetadata);
            gobiiLoaderProcedure.setInstructions(gobiiLoaderInstructionList);

            //Instruction file name needs to be same as job name
            String fileName = dnasampleLoadJob.getJobName() + ".json";
            this.writeDnasampleLoaderInstruction(gobiiLoaderProcedure, fileName, cropType);

            return dnasampleLoadJob;

        } catch(Exception e) {

            LOGGER.error(e.getMessage(), e);

            throw new GobiiDomainException(GobiiStatusLevel.ERROR, GobiiValidationStatusType.UNKNOWN, "Server error");

        }
    }

    private JobDTO createDnaSampleUploadJob(JobDTO jobDto) {


        try {

            //UUID for Jobname. would be used for tracking job details
            String jobName = UUID.randomUUID().toString().replace("-", "");

            jobDto.setJobName(jobName);
            jobDto.setMessage("Submitted the job for sample upload");
            jobDto.setPayloadType(JobPayloadType.CV_PAYLOADTYPE_SAMPLES.getCvName());
            jobDto.setType(JobType.CV_JOBTYPE_LOAD.getCvName());
            jobDto.setStatus(JobProgressStatusType.CV_PROGRESSSTATUS_PENDING.getCvName());

            jobDto = jobService.createJob(jobDto);

            return jobDto;

        }
        catch(GobiiException gE) {
            throw gE;
        }
        catch(Exception e) {

            LOGGER.error(e.getMessage(), e);

            throw new GobiiDomainException(
                    GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNKNOWN,
                    "Unable to submit Dnasample load job");

        }
    }

    private void writeDnasampleLoaderInstruction(
            GobiiLoaderProcedure gobiiLoaderProcedure, String fileName, String cropType) {

        ObjectMapper jsonMapper = new ObjectMapper();

        try {
            if (gobiiLoaderProcedure != null) {

                jsonMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
                jsonMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);

                String instruction = jsonMapper.writeValueAsString(gobiiLoaderProcedure);

                byte[] instructionBytes = instruction.getBytes("UTF-8");

                fileService.writeFileToProcessDir(
                        cropType,
                        fileName,
                        GobiiFileProcessDir.LOADER_INSTRUCTIONS,
                        instructionBytes);
            }
        }
        catch(Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new GobiiDomainException(
                    GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNKNOWN, "Unable to write instruction file");
        }

    }

    private Map<String, List<GobiiFileColumn>> mapFileCoulumnToGobiiTable(
            String fileHeader,
            SampleMetadataDTO sampleMetadata) {

        Map<String, List<GobiiFileColumn>> fileColumnByTableMap = new HashMap<>();

        try {

            Map<String, EntityFieldBean> dtoEntityMap = ModelMapper.getDtoEntityMap(DnaSampleDTO.class);

            //Add Project Id File column to dnasample table instruction
            fileColumnByTableMap.put("dnasample", new LinkedList<>());
            GobiiFileColumn projectIdColumn = new GobiiFileColumn();
            projectIdColumn.setGobiiColumnType(GobiiColumnType.CONSTANT);
            projectIdColumn.setSubcolumn(false);
            projectIdColumn.setConstantValue(sampleMetadata.getProjectId().toString());
            projectIdColumn.setName(dtoEntityMap.get("projectId").getColumnName());

            fileColumnByTableMap.get("dnasample").add(projectIdColumn);

            String[] fileHeaderList = fileHeader.split("\t");


            //Get Germplasm prop cvs TODO: Below cv mapping could be moved to a generalized function.
            //TODO: Create Cv service function to achieve the same.
            List<Cv> germplasmPropCvs = cvDao.getCvListByCvGroup(
                    CvGroup.CVGROUP_GERMPLASM_PROP.getCvGroupName(),
                    null);

            Map<String, Cv> germplasmPropByCvTerm = new HashMap<>();

            for(Cv germplasmPropCv : germplasmPropCvs) {
                if(germplasmPropCv.getTerm() != null) {
                    germplasmPropByCvTerm.put(germplasmPropCv.getTerm(), germplasmPropCv);
                }
            }

            //Get dnasample prop cvs
            List<Cv> dnasamplePropCvs = cvDao.getCvListByCvGroup(
                    CvGroup.CVGROUP_DNASAMPLE_PROP.getCvGroupName(),
                    null);

            Map<String, Cv> dnasamplePropByCvTerm = new HashMap<>();

            for(Cv dnasamplePropCv : dnasamplePropCvs) {
                if(dnasamplePropCv.getTerm() != null) {
                    dnasamplePropByCvTerm.put(dnasamplePropCv.getTerm(), dnasamplePropCv);
                }
            }


            for (int i = 0; i < fileHeaderList.length; i++) {


                String columnHeader = fileHeaderList[i];

                EntityFieldBean entityField = null;

                if (sampleMetadata.getMap().containsKey(columnHeader)) {

                    String dtoProp = sampleMetadata.getMap().get(columnHeader);

                    if (dtoEntityMap.containsKey(dtoProp) && dtoEntityMap.get(dtoProp) != null) {

                        entityField = dtoEntityMap.get(dtoProp);

                    } else {

                        entityField = new EntityFieldBean();

                        String propField = dtoProp.substring(0, dtoProp.lastIndexOf("."));

                        //TODO: More generalized solution where you can map properties to the instruction files
                        // needs to be figured
                        if (propField.equals("germplasm.properties")) {

                            String germplasmPropField = dtoProp.substring(dtoProp.lastIndexOf(".")+1);

                            if(germplasmPropByCvTerm.containsKey(germplasmPropField)) {

                                entityField.setColumnName(
                                        germplasmPropByCvTerm.get(germplasmPropField).getCvId().toString());
                                entityField.setTableName(CvGroup.CVGROUP_GERMPLASM_PROP.getCvGroupName());

                            }

                        } else if (propField.equals("properties")) {

                            String dnasamplePropField = dtoProp.substring(dtoProp.lastIndexOf(".")+1);

                            if(dnasamplePropByCvTerm.containsKey(dnasamplePropField)) {

                                entityField.setColumnName(dnasamplePropByCvTerm.get(
                                        dnasamplePropField).getCvId().toString());
                                entityField.setTableName(CvGroup.CVGROUP_DNASAMPLE_PROP.getCvGroupName());

                            }

                        } else if (dtoProp.equals("germplasm.germplasmSpecies")) {

                            entityField.setColumnName("species_name");

                            entityField.setTableName("germplasm");

                        } else if (dtoProp.equals("germplasm.germplasmType")) {

                            entityField.setColumnName("type_name");

                            entityField.setTableName("germplasm");

                        } else {
                            entityField = null;
                        }
                    }

                } else if (dtoEntityMap.containsKey(columnHeader)) {
                    entityField = dtoEntityMap.get(columnHeader);
                }

                if (entityField != null && entityField.getTableName() != null) {


                    if(entityField.getTableName().endsWith("prop")) {

                        GobiiFileColumn gobiiFileKeyColumn = new GobiiFileColumn();

                        //The key name needs to be props other wise loading will fail
                        if (!fileColumnByTableMap.containsKey(entityField.getTableName())) {
                            gobiiFileKeyColumn.setName("props");
                            gobiiFileKeyColumn.setSubcolumn(false);
                            fileColumnByTableMap.put(entityField.getTableName(), new LinkedList<>());
                        }
                        else {

                            gobiiFileKeyColumn.setSubcolumn(true);

                            //Add a comma file column between two keys as required by instruction file
                            GobiiFileColumn commaColumn = new GobiiFileColumn();

                            commaColumn.setGobiiColumnType(GobiiColumnType.CONSTANT);
                            commaColumn.setSubcolumn(true);
                            commaColumn.setConstantValue(",");
                            commaColumn.setName("comma"+entityField.getColumnName());

                            fileColumnByTableMap.get(entityField.getTableName()).add(commaColumn);

                        }

                        gobiiFileKeyColumn.setGobiiColumnType(GobiiColumnType.CONSTANT);
                        gobiiFileKeyColumn.setConstantValue(entityField.getColumnName());

                        fileColumnByTableMap.get(entityField.getTableName()).add(gobiiFileKeyColumn);

                        //Add Value column
                        //Add a comma gobii file column as required by instruction file
                        GobiiFileColumn valueColumn = new GobiiFileColumn();

                        valueColumn.setGobiiColumnType(GobiiColumnType.CSV_COLUMN);
                        valueColumn.setSubcolumn(true);
                        valueColumn.setName("comma"+entityField.getColumnName());
                        //The file header needs to be the first line.
                        // TODO: file header position hard coded for now. Will be made configurable at some point
                        valueColumn.setRCoord(1);
                        valueColumn.setCCoord(i);

                        fileColumnByTableMap.get(entityField.getTableName()).add(valueColumn);


                    }
                    else {

                        GobiiFileColumn gobiiFileColumn = new GobiiFileColumn();

                        //The file header needs to be the first line.
                        // TODO: file header position hard coded for now. Will be made configurable at some point
                        gobiiFileColumn.setRCoord(1);
                        gobiiFileColumn.setCCoord(i);

                        gobiiFileColumn.setName(entityField.getColumnName());
                        gobiiFileColumn.setGobiiColumnType(GobiiColumnType.CSV_COLUMN);
                        gobiiFileColumn.setSubcolumn(false);

                        if (!fileColumnByTableMap.containsKey(entityField.getTableName())) {
                            fileColumnByTableMap.put(entityField.getTableName(), new LinkedList<>());
                        }

                        fileColumnByTableMap.get(entityField.getTableName()).add(gobiiFileColumn);
                    }
                }
            }

            return fileColumnByTableMap;

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new GobiiDomainException(GobiiStatusLevel.ERROR, GobiiValidationStatusType.UNKNOWN, "Server error");
        }
    }
}
