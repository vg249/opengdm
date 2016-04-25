package org.gobiiproject.gobiidao.resultset.access.impl;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.resultset.access.RsDisplayDao;
import org.gobiiproject.gobiidao.resultset.core.StoredProcExec;
import org.gobiiproject.gobiidao.resultset.sqlworkers.read.SpGetTableDisplayNames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;

/**
 * Created by Phil on 4/8/2016.
 */
public class RsDisplayDaoImpl implements RsDisplayDao {

    Logger LOGGER = LoggerFactory.getLogger(RsDisplayDaoImpl.class);

    @Autowired
    private StoredProcExec storedProcExec = null;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultSet getTableDisplayNames() throws GobiiDaoException {

        ResultSet returnVal = null;

        try {

            SpGetTableDisplayNames spGetTableDisplayNames = new SpGetTableDisplayNames();
            storedProcExec.doWithConnection(spGetTableDisplayNames);
            returnVal = spGetTableDisplayNames.getResultSet();

        } catch (Exception e) {

            LOGGER.error("Error retrieving display names", e);
            throw (new GobiiDaoException(e));

        }

        return returnVal;
    }

} // RsProjectDaoImpl
