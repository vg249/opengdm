package org.gobiiproject.gobiidao.resultset.access.impl;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.resultset.access.RsMapSetDao;
import org.gobiiproject.gobiidao.resultset.core.StoredProcExec;
import org.gobiiproject.gobiidao.resultset.sqlworkers.read.SpGetMapSetNames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;

/**
 * Created by Phil on 4/7/2016.
 */
public class RsMapSetDaoImpl implements RsMapSetDao {

    Logger LOGGER = LoggerFactory.getLogger(RsMapSetDaoImpl.class);


    @Autowired
    private StoredProcExec storedProcExec = null;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ResultSet getMapNames() throws GobiiDaoException {

        ResultSet returnVal = null;

        try {
            SpGetMapSetNames spGetMapSetNames = new SpGetMapSetNames();
            storedProcExec.doWithConnection(spGetMapSetNames);
            returnVal = spGetMapSetNames.getResultSet();
        } catch (Exception e) {

            LOGGER.error("Error retrieving mapset names", e);
            throw (new GobiiDaoException(e));

        }


        return returnVal;

    }
}
