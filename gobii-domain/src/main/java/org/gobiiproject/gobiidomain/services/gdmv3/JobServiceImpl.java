package org.gobiiproject.gobiidomain.services.gdmv3;

import org.apache.commons.lang.StringUtils;
import org.gobiiproject.gobiidomain.GobiiDomainException;
import org.gobiiproject.gobiidomain.services.FilesService;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.GobiiCropConfig;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.cvnames.CvGroupTerm;
import org.gobiiproject.gobiimodel.cvnames.JobType;
import org.gobiiproject.gobiimodel.dto.gdmv3.JobDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;
import org.gobiiproject.gobiimodel.entity.Contact;
import org.gobiiproject.gobiimodel.entity.Cv;
import org.gobiiproject.gobiimodel.entity.Job;
import org.gobiiproject.gobiimodel.modelmapper.ModelMapper;
import org.gobiiproject.gobiimodel.types.*;
import org.gobiiproject.gobiisampletrackingdao.ContactDao;
import org.gobiiproject.gobiisampletrackingdao.CvDao;
import org.gobiiproject.gobiisampletrackingdao.JobDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.log4j.Log4j;

import java.util.Date;
import java.util.UUID;
import java.util.List;
import java.io.File;
import java.util.ArrayList;

@Transactional
@Log4j
public class JobServiceImpl implements JobService {
    public static final String EXTRACT = "extract";
    public static final String LOAD = "load";

    public static final String COMPLETED = "completed";
    public static final String FAILED = "failed";

    public static final String IN_PROGRESS = "in_progress";
    public static final String PENDING = "pending";

    @Autowired
    private JobDao jobDao;

    @Autowired
    private CvDao cvDao;

    @Autowired
    private ContactDao contactDao;

    @Autowired
    private ConfigSettings configSettings;

    @Override
    public JobDTO getJobById(Integer jobId) throws GobiiException {
        JobDTO jobDTO = new JobDTO();
        Job job = jobDao.getById(jobId);
        ModelMapper.mapEntityToDto(job, jobDTO);
        return jobDTO;
    }

    public JobDTO createLoaderJob(JobDTO jobDTO) throws GobiiException {

        Job job = new Job();

        if (jobDTO.getPayload().isEmpty()) {
            throw new GobiiDomainException(GobiiStatusLevel.ERROR, GobiiValidationStatusType.BAD_REQUEST,
                    "Required payload type");
        }

        // Set a unique job name if not set
        if (StringUtils.isEmpty(jobDTO.getJobName())) {
            String jobName = UUID.randomUUID().toString().replace("-", "");
            job.setJobName(jobName);
        }

        job.setMessage("Submitted marker upload job");

        // Set current user as job submitter
        Contact submittedBy = contactDao.getContactByUsername(ContactService.getCurrentUser());
        job.setSubmittedBy(submittedBy);

        // Get payload type
        Cv payloadType = cvDao.getCvs(jobDTO.getPayload(), CvGroupTerm.CVGROUP_PAYLOADTYPE.getCvGroupName(),
                GobiiCvGroupType.GROUP_TYPE_SYSTEM).get(0);

        job.setPayloadType(payloadType);

        // Get jobstatus as pending
        Cv jobStatus = cvDao.getCvs(GobiiJobStatus.PENDING.getCvTerm(), CvGroupTerm.CVGROUP_JOBSTATUS.getCvGroupName(),
                GobiiCvGroupType.GROUP_TYPE_SYSTEM).get(0);
        job.setStatus(jobStatus);

        job.setSubmittedDate(new Date());
        // Get load type
        Cv jobType = cvDao.getCvs(JobType.CV_JOBTYPE_LOAD.getCvName(), CvGroupTerm.CVGROUP_JOBTYPE.getCvGroupName(),
                GobiiCvGroupType.GROUP_TYPE_SYSTEM).get(0);

        job.setType(jobType);
        jobDao.create(job);
        ModelMapper.mapEntityToDto(job, jobDTO);

        return jobDTO;

    }

    @Override
    public PagedResult<JobDTO> getJobs(Integer page, Integer pageSize, Integer contactId, boolean loadAndExtractOnly) {
        List<Job> jobs = jobDao.getJobs(page, pageSize, contactId, loadAndExtractOnly);
        List<JobDTO> jobDTOs = new ArrayList<>();
        jobs.forEach(job -> {
            JobDTO jobDTO = new JobDTO();
            ModelMapper.mapEntityToDto(job, jobDTO);
            jobDTOs.add(jobDTO);
        });

        return PagedResult.createFrom(page, jobDTOs);
    }

    @Override
    public File getJobStatusDirectory(String cropType, String jobName) throws Exception {
        Job job = jobDao.getByName(jobName);
        if (job == null) {
            throw new GobiiException(GobiiStatusLevel.ERROR, GobiiValidationStatusType.BAD_REQUEST, "Job not found.");
        }

        String status =  job.getStatus().getTerm();
        String type = job.getType().getTerm();

        GobiiFileProcessDir dir = null;
        if (type.equals(EXTRACT)) {
            switch (status) {
                case COMPLETED:
                case FAILED: dir = GobiiFileProcessDir.EXTRACTOR_DONE; break;
                case IN_PROGRESS: dir = GobiiFileProcessDir.EXTRACTOR_INPROGRESS; break;
                default: dir = GobiiFileProcessDir.EXTRACTOR_INSTRUCTIONS; break;
            }
        } else if (type.equals(LOAD)) {
            switch(status) {
                case COMPLETED:
                case FAILED: dir = GobiiFileProcessDir.LOADER_DONE; break;
                case IN_PROGRESS: dir = GobiiFileProcessDir.LOADER_INPROGRESS_FILES; break;
                default: dir = GobiiFileProcessDir.LOADER_INSTRUCTIONS;
            }
        } else {
            throw new GobiiException(GobiiStatusLevel.ERROR, GobiiValidationStatusType.BAD_REQUEST, "Job type not supported");
        }

        String path = configSettings.getProcessingPath(cropType, dir);
        log.debug("Path for job is " + path + "/" + jobName);
        return new File(path, jobName);
    }

    

}
