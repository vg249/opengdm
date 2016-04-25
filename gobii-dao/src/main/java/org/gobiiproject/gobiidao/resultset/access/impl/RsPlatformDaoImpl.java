package org.gobiiproject.gobiidao.resultset.access.impl;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.resultset.access.RsPlatformDao;
import org.gobiiproject.gobiidao.resultset.core.StoredProcExec;
import org.gobiiproject.gobiidao.resultset.sqlworkers.read.SpGetPlatformNames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;

/**
 * Created by Phil on 4/7/2016.
 */
public class RsPlatformDaoImpl implements RsPlatformDao {

    Logger LOGGER = LoggerFactory.getLogger(RsPlatformDaoImpl.class);

    @Autowired
    private StoredProcExec storedProcExec = null;

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
}
