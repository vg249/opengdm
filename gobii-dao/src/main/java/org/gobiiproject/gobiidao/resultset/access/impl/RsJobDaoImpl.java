package org.gobiiproject.gobiidao.resultset.access.impl;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.resultset.access.RsJobDao;
import org.gobiiproject.gobiidao.resultset.core.SpRunnerCallable;
import org.gobiiproject.gobiidao.resultset.core.StoredProcExec;
import org.gobiiproject.gobiidao.resultset.sqlworkers.modify.SpInsJobByCvTerms;
import org.gobiiproject.gobiidao.resultset.sqlworkers.modify.SpUpdJobByCvTerms;
import org.gobiiproject.gobiidao.resultset.sqlworkers.read.SpGetJobDetailsByDataSetId;
import org.gobiiproject.gobiidao.resultset.sqlworkers.read.SpGetJobDetailsByJobName;
import org.gobiiproject.gobiidao.resultset.sqlworkers.read.SpGetJobs;
import org.hibernate.exception.SQLGrammarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by VCalaminos on 9/4/2017.
 */
public class RsJobDaoImpl implements RsJobDao {

    Logger LOGGER = LoggerFactory.getLogger(RsJobDao.class);

    @Autowired
    private StoredProcExec storedProcExec = null;

    @Autowired
    private SpRunnerCallable spRunnerCallable;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultSet getJobDetailsForJobName(String jobName) throws GobiiDaoException {

        ResultSet returnVal = null;

        try {

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("jobName", jobName);
            SpGetJobDetailsByJobName spGetJobDetailsByJobName = new SpGetJobDetailsByJobName(parameters);

            storedProcExec.doWithConnection(spGetJobDetailsByJobName);

            returnVal = spGetJobDetailsByJobName.getResultSet();


        } catch (SQLGrammarException e) {

            LOGGER.error("Error getting job details with job ID with SQL " + e.getSQL(), e.getSQLException());
            throw (new GobiiDaoException(e.getSQLException()));

        }


        return returnVal;

    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultSet getJobs() throws GobiiDaoException {

        ResultSet returnVal = null;

        try {

            SpGetJobs spGetJobs = new SpGetJobs();
            storedProcExec.doWithConnection(spGetJobs);
            returnVal = spGetJobs.getResultSet();


        } catch (SQLGrammarException e) {

            LOGGER.error("Error retrieving job statuses with SQL " + e.getSQL(), e.getSQLException());
            throw (new GobiiDaoException(e.getSQLException()));
        }

        return returnVal;

    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Integer createJobWithCvTerms(Map<String, Object> parameters) throws GobiiDaoException {

        Integer returnVal = null;

        try {

            spRunnerCallable.run(new SpInsJobByCvTerms(), parameters);
            returnVal = spRunnerCallable.getResult();

        } catch (SQLGrammarException e) {

            LOGGER.error("Error creating job with SQL " + e.getSQL(), e.getSQLException());
            throw (new GobiiDaoException(e.getSQLException()));

        }

        return  returnVal;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateJobWithCvTerms(Map<String, Object> parameters) throws GobiiDaoException {

        try {

            spRunnerCallable.run(new SpUpdJobByCvTerms(), parameters);


        } catch (SQLGrammarException e) {

            LOGGER.error("Error updating job with SQL " + e.getSQL(), e.getSQLException());
            throw (new GobiiDaoException(e.getSQLException()));

        }


    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultSet getJobDetailsByDatasetId(Integer dataSetId) throws GobiiDaoException {

        ResultSet returnVal = null;

        try {

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("dataSetId", dataSetId);
            SpGetJobDetailsByDataSetId spGetJobDetailsByDataSetId = new SpGetJobDetailsByDataSetId(parameters);

            storedProcExec.doWithConnection(spGetJobDetailsByDataSetId);

            returnVal = spGetJobDetailsByDataSetId.getResultSet();

        } catch (SQLGrammarException e) {

            LOGGER.error("Error retrieving dataset details", e.getSQL(), e.getSQLException());
            throw (new GobiiDaoException(e.getSQLException()));

        }

        return returnVal;
    }

}
