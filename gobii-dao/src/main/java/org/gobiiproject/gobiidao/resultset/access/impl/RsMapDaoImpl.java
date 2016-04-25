package org.gobiiproject.gobiidao.resultset.access.impl;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.resultset.access.RsMapDao;
import org.gobiiproject.gobiidao.resultset.access.RsPlatformDao;
import org.gobiiproject.gobiidao.resultset.core.StoredProcExec;
import org.gobiiproject.gobiidao.resultset.sqlworkers.read.SpGetMapNames;
import org.gobiiproject.gobiidao.resultset.sqlworkers.read.SpGetPlatformNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;

/**
 * Created by Phil on 4/7/2016.
 */
public class RsMapDaoImpl implements RsMapDao {

    @Autowired
    private StoredProcExec storedProcExec = null;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ResultSet getMapNames() throws GobiiDaoException {

        ResultSet returnVal = null;

        SpGetMapNames spGetMapNames = new SpGetMapNames();
        storedProcExec.doWithConnection(spGetMapNames);
        returnVal = spGetMapNames.getResultSet();

        return returnVal;

    }


//    @Transactional(propagation = Propagation.REQUIRED)
//    @Override
//    public ResultSet getContactNamesForRoleName(String roleName) throws GobiiDaoException {
//
//        ResultSet returnVal = null;
//
//
//        Map<String, Object> parameters = new HashMap<>();
//        parameters.put("roleName", roleName);
//        SpGetContactNamesByRoleName spGetContactNamesByRoleName = new SpGetContactNamesByRoleName(parameters);
//
//        storedProcExec.doWithConnection(spGetContactNamesByRoleName);
//
//        returnVal = spGetContactNamesByRoleName.getResultSet();
//
//        return returnVal;
//
//    } // getContactNamesForRoleName
//
//    @Transactional(propagation = Propagation.REQUIRED)
//    @Override
//    public ResultSet getContactsForRoleName(String roleName) throws GobiiDaoException {
//
//        ResultSet returnVal = null;
//
//
//        Map<String, Object> parameters = new HashMap<>();
//        parameters.put("roleName", roleName);
//        SpGetContactsByRoleName spGetContactsByRoleName = new SpGetContactsByRoleName(parameters);
//
//        storedProcExec.doWithConnection(spGetContactsByRoleName);
//
//        returnVal = spGetContactsByRoleName.getResultSet();
//
//        return returnVal;
//    }
}
