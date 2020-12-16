package org.gobiiproject.gobiidomain.services.gdmv3;

import org.apache.commons.lang.StringUtils;
import org.gobiiproject.gobiidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.cvnames.CvGroupTerm;
import org.gobiiproject.gobiimodel.cvnames.JobType;
import org.gobiiproject.gobiimodel.dto.gdmv3.JobDTO;
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

import java.util.Date;
import java.util.UUID;

@Transactional
public class JobServiceImpl implements JobService {

    @Autowired
    private JobDao jobDao;

    @Autowired
    private CvDao cvDao;

    @Autowired
    private ContactDao contactDao;

    @Override
    public JobDTO getJobById(Integer jobId) throws GobiiException {
        JobDTO jobDTO = new JobDTO();
        Job job = jobDao.getById(jobId);
        ModelMapper.mapEntityToDto(job, jobDTO);
        return jobDTO;
    }

    public JobDTO createLoaderJob(JobDTO jobDTO) throws GobiiException {

        Job job = new Job();

        if(jobDTO.getPayload().isEmpty()) {
            throw new GobiiDomainException(
                GobiiStatusLevel.ERROR,
                GobiiValidationStatusType.BAD_REQUEST,
                "Required payload type");
        }

        // Set a unique job name if not set
        if(StringUtils.isEmpty(jobDTO.getJobName())) {
            String jobName = UUID.randomUUID().toString().replace("-", "");
            job.setJobName(jobName);
        }

        job.setMessage("Submitted marker upload job");

        // Set current user as job submitter
        Contact submittedBy = contactDao.getContactByUsername(ContactService.getCurrentUser());
        job.setSubmittedBy(submittedBy);

        // Get payload type
        Cv payloadType = cvDao.getCvs(jobDTO.getPayload(),
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
        jobDao.create(job);
        ModelMapper.mapEntityToDto(job, jobDTO);

        return jobDTO;

    }

}
