package org.gobiiproject.gobiisampletrackingdao;

import java.util.List;

import org.gobiiproject.gobiimodel.entity.Job;

public interface JobDao {

    Job create(Job job) throws GobiiDaoException;
    Job update(Job job) throws GobiiDaoException;
    Job getById(Integer jobId);
    Job getByName(String jobName) throws GobiiDaoException;
	List<Job> getJobs(Integer page, Integer pageSize, Integer contactId);
	List<Job> getJobs(Integer page, Integer pageSize, Integer contactId, boolean loadAndExtractOnly);
	List<Job> getJobsByUsername(Integer page, Integer pageSizeToUse, String username, boolean loadAndExtractOnly);
}
