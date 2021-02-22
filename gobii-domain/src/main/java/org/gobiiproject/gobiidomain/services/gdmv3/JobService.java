package org.gobiiproject.gobiidomain.services.gdmv3;

import java.io.File;

import org.gobiiproject.gobiidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.gdmv3.JobDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;


public interface JobService {
    JobDTO getJobById(Integer jobId) throws GobiiDomainException;
    JobDTO createLoaderJob(JobDTO jobDTO) throws GobiiException;
	PagedResult<JobDTO> getJobs(Integer page, Integer pageSizeToUse, Integer contactId);
	File getJobStatusDirectory(String cropType, String jobName) throws Exception;
}
