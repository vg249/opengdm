package org.gobiiproject.gobiidao.resultset.access.impl;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.resultset.access.RsStatusDao;
import org.gobiiproject.gobiidao.resultset.core.SpRunnerCallable;
import org.gobiiproject.gobiidao.resultset.core.StoredProcExec;
import org.gobiiproject.gobiidao.resultset.sqlworkers.modify.SpInsExperiment;
import org.gobiiproject.gobiidao.resultset.sqlworkers.modify.SpInsStatus;
import org.gobiiproject.gobiidao.resultset.sqlworkers.modify.SpUpdStatus;
import org.gobiiproject.gobiidao.resultset.sqlworkers.read.SpGetStatusDetailsByJobId;
import org.gobiiproject.gobiidao.resultset.sqlworkers.read.SpGetStatuses;
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
public class RsStatusDaoImpl implements RsStatusDao {

    Logger LOGGER = LoggerFactory.getLogger(RsStatusDao.class);

    @Autowired
    private StoredProcExec storedProcExec = null;

    @Autowired
    private SpRunnerCallable spRunnerCallable;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultSet getStatusDetailsForJobId(Integer jobId) throws GobiiDaoException {

        ResultSet returnVal = null;

        try {

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("jobId", jobId);
            SpGetStatusDetailsByJobId spGetStatusDetailsByJobId = new SpGetStatusDetailsByJobId(parameters);

            storedProcExec.doWithConnection(spGetStatusDetailsByJobId);

            returnVal = spGetStatusDetailsByJobId.getResultSet();


        } catch (SQLGrammarException e) {

            LOGGER.error("Error getting job details with job ID with SQL " + e.getSQL(), e.getSQLException());
            throw (new GobiiDaoException(e.getSQLException()));

        }


        return returnVal;

    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultSet getStatuses() throws GobiiDaoException {

        ResultSet returnVal = null;

        try {

            SpGetStatuses spGetStatuses = new SpGetStatuses();
            storedProcExec.doWithConnection(spGetStatuses);
            returnVal = spGetStatuses.getResultSet();


        } catch (SQLGrammarException e) {

            LOGGER.error("Error retrieving job statuses with SQL " + e.getSQL(), e.getSQLException());
            throw (new GobiiDaoException(e.getSQLException()));
        }

        return returnVal;

    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Integer createStatus(Map<String, Object> parameters) throws GobiiDaoException {

        Integer returnVal = null;

        try {

            spRunnerCallable.run(new SpInsStatus(), parameters);
            returnVal = spRunnerCallable.getResult();

        } catch (SQLGrammarException e) {

            LOGGER.error("Error creating job with SQL " + e.getSQL(), e.getSQLException());
            throw (new GobiiDaoException(e.getSQLException()));

        }

        return  returnVal;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateStatus(Map<String, Object> parameters) throws GobiiDaoException {

        try {

            spRunnerCallable.run(new SpUpdStatus(), parameters);


        } catch (SQLGrammarException e) {

            LOGGER.error("Error updating job with SQL " + e.getSQL(), e.getSQLException());
            throw (new GobiiDaoException(e.getSQLException()));

        }


    }

}
