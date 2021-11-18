package org.gobiiproject.gobiidomain.services.gdmv3;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.gobiiproject.gobiidomain.GobiiDomainException;
import org.gobiiproject.gobiidomain.services.rabbitmq.Send;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.config.RabbitMqConfig;
import org.gobiiproject.gobiimodel.cvnames.CvGroupTerm;
import org.gobiiproject.gobiimodel.dto.gdmv3.DnaRunUploadRequestDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.JobDTO;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.LoaderInstruction3;
import org.gobiiproject.gobiimodel.entity.Experiment;
import org.gobiiproject.gobiimodel.entity.Project;
import org.gobiiproject.gobiimodel.types.GobiiLoaderPayloadTypes;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.gobiiproject.gobiimodel.utils.IntegerUtils;
import org.gobiiproject.gobiisampletrackingdao.ContactDao;
import org.gobiiproject.gobiisampletrackingdao.ExperimentDao;
import org.gobiiproject.gobiisampletrackingdao.ProjectDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
public class DnaRunServiceImpl implements DnaRunService {

    @Autowired
    RabbitMqConfig rabbitMqConfig;

    @Autowired
    private ContactDao contactDao;

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private ExperimentDao experimentDao;

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
     * @param dnaRunUploadRequest   Request object with meta data and template
     * @param cropType              Crop type to which the dnaruns need to uploaded
     * @return {@link JobDTO}   when dnarun loader job is successfully submitted.
     * @throws GobiiException   Gobii Exception for bad request or if any run time system error
     */
    @Override
    public JobDTO loadDnaRuns(DnaRunUploadRequestDTO dnaRunUploadRequest,
                              String cropType) throws GobiiException {

        // Validation
        Utils.checkIfInputFilesAreValid(dnaRunUploadRequest.getInputFiles());
        validateExperiment(dnaRunUploadRequest);
        validateProject(dnaRunUploadRequest);

        // Get a new Job object for samples loading
        JobDTO jobDTO = new JobDTO();
        jobDTO.setPayload(GobiiLoaderPayloadTypes.SAMPLES.getTerm());
        jobDTO.setJobMessage("Submitted dnarun load job.");

        JobDTO job = jobService.createLoaderJob(jobDTO);
        String jobName = job.getJobName();

        // Create Loader Instructions
        String submitterEmail = contactDao
            .getContactByUsername(ContactService.getCurrentUserName())
            .getEmail();
        LoaderInstruction3 loaderInstruction = new LoaderInstruction3();
        loaderInstruction.setAspects(new HashMap<>());
        loaderInstruction.setContactEmail(submitterEmail);
        loaderInstruction.setCropType(cropType);
        loaderInstruction.setInstructionType("v3");
        loaderInstruction.setJobName(jobName);
        loaderInstruction.setLoadType(loadType);
        loaderInstruction.setOutputDir(Utils.getOutputDir(jobName, cropType));
        loaderInstruction.setUserRequest(dnaRunUploadRequest);

        deliverInstructions(loaderInstruction, jobName, cropType);

        return jobDTO;
    }

    private void deliverInstructions(LoaderInstruction3 instructions, String jobName, String cropType) throws GobiiException {
        if (rabbitMqConfig.getHost().isEmpty()) {
            Utils.writeInstructionFile(instructions, jobName, cropType);
        } else {
            String loaderInstructionJson = "";
            try {
                loaderInstructionJson = mapper.writeValueAsString(instructions);
                Send rabbitMQ = new Send();
                rabbitMQ.send("processNewLoaderInstructions", loaderInstructionJson);
            } catch (Exception e) {
                log.error(e.getMessage());
                e.printStackTrace();
                throw new GobiiException(e);
            }
        }
    }

    private void validateProject(DnaRunUploadRequestDTO dnaRunUploadRequest) {
        Integer projectId = dnaRunUploadRequest.getProjectId();
        if (!IntegerUtils.isNullOrZero(projectId)) {
            Project project = projectDao.getProject(projectId);
            if (project == null) {
                throw new GobiiDomainException(GobiiStatusLevel.ERROR,
                        GobiiValidationStatusType.BAD_REQUEST, "Invalid Project");
            }
        }
    }

    private void validateExperiment(DnaRunUploadRequestDTO dnaRunUploadRequest) {
        Integer experimentId = dnaRunUploadRequest.getExperimentId();
        if (!IntegerUtils.isNullOrZero(experimentId)) {
            Experiment experiment = experimentDao.getExperiment(experimentId);
            if (experiment == null) {
                throw new GobiiDomainException(GobiiStatusLevel.ERROR,
                        GobiiValidationStatusType.BAD_REQUEST, "Invalid Experiment");
            }
        }
    }

}
