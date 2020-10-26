package org.gobiiproject.gobiisampletrackingdao;

import org.gobiiproject.gobiimodel.entity.Job;

public interface JobDao {

    Job create(Job job) throws GobiiDaoException;
    Job getById(Integer jobId);
}
