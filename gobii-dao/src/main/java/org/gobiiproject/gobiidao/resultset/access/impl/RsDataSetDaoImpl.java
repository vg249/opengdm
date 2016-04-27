package org.gobiiproject.gobiidao.resultset.access.impl;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.resultset.access.RsDataSetDao;
import org.gobiiproject.gobiidao.resultset.core.SpRunnerCallable;
import org.gobiiproject.gobiidao.resultset.core.StoredProcExec;
import org.gobiiproject.gobiidao.resultset.sqlworkers.modify.SpInsAnalysis;
import org.gobiiproject.gobiidao.resultset.sqlworkers.modify.SpInsDataSet;
import org.gobiiproject.gobiidao.resultset.sqlworkers.read.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Phil on 4/8/2016.
 */
public class RsDataSetDaoImpl implements RsDataSetDao {

    Logger LOGGER = LoggerFactory.getLogger(RsDataSetDaoImpl.class);


    @Autowired
    private StoredProcExec storedProcExec = null;

    @Autowired
    private SpRunnerCallable spRunnerCallable;


    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultSet getDatasetFileNamesByExperimentId(Integer experimentId) throws GobiiDaoException {

        ResultSet returnVal = null;

        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("experimentId", experimentId);
            SpGetDatasetFileNamesByExperimentId spGetDatasetFileNamesByExperimentId = new SpGetDatasetFileNamesByExperimentId(parameters);

            storedProcExec.doWithConnection(spGetDatasetFileNamesByExperimentId);

            returnVal = spGetDatasetFileNamesByExperimentId.getResultSet();

        } catch (Exception e) {

            LOGGER.error("Error retrieving dataset file names", e);
            throw (new GobiiDaoException(e));

        }

        return returnVal;

    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultSet getDataSetDetailsByDataSetId(Integer dataSetId) throws GobiiDaoException {

        ResultSet returnVal = null;

        try {

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("dataSetId", dataSetId);
            SpGetDatasetDetailsByDataSetId spGetDatasetDetailsByExperimentId = new SpGetDatasetDetailsByDataSetId(parameters);

            storedProcExec.doWithConnection(spGetDatasetDetailsByExperimentId);

            returnVal = spGetDatasetDetailsByExperimentId.getResultSet();

        } catch (Exception e) {

            LOGGER.error("Error retrieving dataset details", e);
            throw (new GobiiDaoException(e));

        }

        return returnVal;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Integer createDataset(Map<String, Object> parameters) throws GobiiDaoException {

        Integer returnVal = null;

        try {

            if (spRunnerCallable.run(new SpInsDataSet(), parameters)) {

                returnVal = spRunnerCallable.getResult();

            } else {

                throw new GobiiDaoException(spRunnerCallable.getErrorString());

            }

        } catch (Exception e) {

            LOGGER.error("Error creating dataset", e);
            throw (new GobiiDaoException(e));

        }

        return returnVal;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Integer createUpdateParameter(Map<String, Object> parameters) throws GobiiDaoException {
        return null;
    }


} // RsProjectDaoImpl
