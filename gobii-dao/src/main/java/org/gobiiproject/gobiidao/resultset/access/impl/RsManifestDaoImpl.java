package org.gobiiproject.gobiidao.resultset.access.impl;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.resultset.access.RsManifestDao;
import org.gobiiproject.gobiidao.resultset.core.StoredProcExec;
import org.gobiiproject.gobiidao.resultset.sqlworkers.read.SpGetManifestNames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;

/**
 * Created by Phil on 4/7/2016.
 */
public class RsManifestDaoImpl implements RsManifestDao {

    Logger LOGGER = LoggerFactory.getLogger(RsManifestDaoImpl.class);

    @Autowired
    private StoredProcExec storedProcExec = null;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ResultSet getManifestNames() throws GobiiDaoException {

        ResultSet returnVal = null;

        try {

            SpGetManifestNames spGetManifestNames = new SpGetManifestNames();
            storedProcExec.doWithConnection(spGetManifestNames);
            returnVal = spGetManifestNames.getResultSet();

        } catch (Exception e) {

            LOGGER.error("Error retrieving manifest names", e);
            throw (new GobiiDaoException(e));

        }

        return returnVal;

    }

}
