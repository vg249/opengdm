package org.gobiiproject.gobiisampletrackingdao;

import org.gobiiproject.gobiimodel.entity.Job;
import org.gobiiproject.gobiimodel.entity.LoaderTemplate;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import java.util.Objects;

public class JobDaoImpl implements JobDao {

    Logger LOGGER = LoggerFactory.getLogger(JobDaoImpl.class);

    @PersistenceContext
    protected EntityManager em;

    /**
     * Dao implementation to create a job entry
     * @param job entity to create
     * @return Created job entity with generated id
     */
    @Override
    public Job create(Job job) throws GobiiDaoException {
        try {
            em.persist(job);
            em.flush();
            em.refresh(job, Utils.getHints(em, "graph.job"));
            return job;
        }
        catch (PersistenceException pe) {
            throw new GobiiDaoException("Unable to create job");
        }
    }

    @Override
    public Job getById(Integer jobId) throws GobiiDaoException {
        Job job;
        try {
            job = em.find(
                Job.class,
                jobId,
                Utils.getHints(em, "graph.job"));
        }
        catch (IllegalArgumentException e) {
           throw new GobiiDaoException(
               GobiiStatusLevel.ERROR,
               GobiiValidationStatusType.BAD_REQUEST,
               "Invalid Request or system error");
        }

        if(Objects.isNull(job)) {
            throw new GobiiDaoException(
                GobiiStatusLevel.ERROR,
                GobiiValidationStatusType.BAD_REQUEST,
                "Entity does not exist");
        }
        return job;
    }


}
