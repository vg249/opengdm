package org.gobiiproject.gobiidao.resultset.access.impl;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.resultset.access.RsPlatformDao;
import org.gobiiproject.gobiidao.resultset.core.SpRunnerCallable;
import org.gobiiproject.gobiidao.resultset.core.StoredProcExec;
import org.gobiiproject.gobiidao.resultset.sqlworkers.modify.SpInsPlatform;
import org.gobiiproject.gobiidao.resultset.sqlworkers.read.SpGetPlatformDetailsByPlatformId;
import org.gobiiproject.gobiidao.resultset.sqlworkers.read.SpGetPlatformDetailsByPlatformId;
import org.gobiiproject.gobiidao.resultset.sqlworkers.read.SpGetPlatformNames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Phil on 4/7/2016.
 */
public class RsPlatformDaoImpl implements RsPlatformDao {

    Logger LOGGER = LoggerFactory.getLogger(RsPlatformDaoImpl.class);

    @Autowired
    private StoredProcExec storedProcExec = null;

    @Autowired
    private SpRunnerCallable spRunnerCallable;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ResultSet getPlatformNames() throws GobiiDaoException {

        ResultSet returnVal = null;

        try {

            SpGetPlatformNames spGetPlatformNames = new SpGetPlatformNames();
            storedProcExec.doWithConnection(spGetPlatformNames);
            returnVal = spGetPlatformNames.getResultSet();

        } catch (Exception e) {

            LOGGER.error("Error retrieving platform names", e);
            throw (new GobiiDaoException(e));

        }


        return returnVal;

    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultSet getPlatformDetailsByPlatformId(Integer platformId) throws GobiiDaoException {

        ResultSet returnVal = null;

        try {

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("platformId", platformId);
            SpGetPlatformDetailsByPlatformId spGetPlatformDetailsByPlatformId = new SpGetPlatformDetailsByPlatformId(parameters);

            storedProcExec.doWithConnection(spGetPlatformDetailsByPlatformId);

            returnVal = spGetPlatformDetailsByPlatformId.getResultSet();

        } catch (Exception e) {

            LOGGER.error("Error retrieving platform details", e);
            throw (new GobiiDaoException(e));

        }

        return returnVal;

    } // getPlatformDetailsByPlatformId()

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Integer createPlatform(Map<String, Object> parameters) throws GobiiDaoException {

        Integer returnVal = null;

        try {

            if (spRunnerCallable.run(new SpInsPlatform(), parameters)) {

                returnVal = spRunnerCallable.getResult();

            } else {

                throw new GobiiDaoException(spRunnerCallable.getErrorString());

            }

        } catch (Exception e) {

            LOGGER.error("Error creating platform", e);
            throw (new GobiiDaoException(e));

        }

        return returnVal;
    }

}
