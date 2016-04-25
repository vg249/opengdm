package org.gobiiproject.gobiidao.resultset.access.impl;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.resultset.access.RsContactDao;
import org.gobiiproject.gobiidao.resultset.core.StoredProcExec;
import org.gobiiproject.gobiidao.resultset.sqlworkers.read.SpGetContactNamesByRoleName;
import org.gobiiproject.gobiidao.resultset.sqlworkers.read.SpGetContactsByRoleName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Phil on 4/7/2016.
 */
public class RsContactDaoSpImpl implements RsContactDao {

    Logger LOGGER = LoggerFactory.getLogger(RsContactDaoSpImpl.class);

    @Autowired
    private StoredProcExec storedProcExec = null;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultSet getContactNamesForRoleName(String roleName) throws GobiiDaoException {

        ResultSet returnVal = null;

        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("roleName", roleName);
            SpGetContactNamesByRoleName spGetContactNamesByRoleName = new SpGetContactNamesByRoleName(parameters);

            storedProcExec.doWithConnection(spGetContactNamesByRoleName);

            returnVal = spGetContactNamesByRoleName.getResultSet();
        } catch (Exception e) {

            LOGGER.error("Error retrieving contact names", e);
            throw (new GobiiDaoException(e));

        }

        return returnVal;

    } // getContactNamesForRoleName

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultSet getContactsForRoleName(String roleName) throws GobiiDaoException {

        ResultSet returnVal = null;

        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("roleName", roleName);
            SpGetContactsByRoleName spGetContactsByRoleName = new SpGetContactsByRoleName(parameters);

            storedProcExec.doWithConnection(spGetContactsByRoleName);

            returnVal = spGetContactsByRoleName.getResultSet();

        } catch (Exception e) {

            LOGGER.error("Error retrieving contacts", e);
            throw (new GobiiDaoException(e));
        }

        return returnVal;
    }
}
