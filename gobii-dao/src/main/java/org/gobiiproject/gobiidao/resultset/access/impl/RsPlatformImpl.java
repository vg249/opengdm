package org.gobiiproject.gobiidao.resultset.access.impl;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.resultset.access.RsContact;
import org.gobiiproject.gobiidao.resultset.access.RsPlatform;
import org.gobiiproject.gobiidao.resultset.core.StoredProcExec;
import org.gobiiproject.gobiidao.resultset.spworkers.SpGetContactNamesByRoleName;
import org.gobiiproject.gobiidao.resultset.spworkers.SpGetContactsByRoleName;
import org.gobiiproject.gobiidao.resultset.viewworkers.SpGetPlatformNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Phil on 4/7/2016.
 */
public class RsPlatformImpl implements RsPlatform {

    @Autowired
    private StoredProcExec storedProcExec = null;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ResultSet getPlatformNames() throws GobiiDaoException {

        ResultSet returnVal = null;

        SpGetPlatformNames spGetPlatformNames = new SpGetPlatformNames();
        storedProcExec.doWithConnection(spGetPlatformNames);
        returnVal = spGetPlatformNames.getResultSet();

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
