package org.gobiiproject.gobiidao.resultset.access.impl;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.resultset.access.RsMarkerGroupDao;
import org.gobiiproject.gobiidao.resultset.access.RsPlatformDao;
import org.gobiiproject.gobiidao.resultset.core.StoredProcExec;
import org.gobiiproject.gobiidao.resultset.sqlworkers.read.SpGetMarkerGroupNames;
import org.gobiiproject.gobiidao.resultset.sqlworkers.read.SpGetPlatformNames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;

/**
 * Created by Angel on 4/27/2016.
 */
public class RsMarkerGroupDaoImpl implements RsMarkerGroupDao {

    Logger LOGGER = LoggerFactory.getLogger(RsMarkerGroupDaoImpl.class);

    @Autowired
    private StoredProcExec storedProcExec = null;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ResultSet getMarkerGroupNames() throws GobiiDaoException {

        ResultSet returnVal = null;

        try {

            SpGetMarkerGroupNames spGetMarkerGroupNames = new SpGetMarkerGroupNames();
            storedProcExec.doWithConnection(spGetMarkerGroupNames);
            returnVal = spGetMarkerGroupNames.getResultSet();

        } catch (Exception e) {

            LOGGER.error("Error retrieving platform names", e);
            throw (new GobiiDaoException(e));

        }


        return returnVal;

    }
}
