package org.gobiiproject.gobiidao.resultset.access.impl;

import org.gobiiproject.gobiidao.resultset.access.RsDisplayDao;
import org.gobiiproject.gobiidao.resultset.core.StoredProcExec;
import org.gobiiproject.gobiidao.resultset.viewworkers.SpGetDisplayNamesForTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Phil on 4/8/2016.
 */
public class RsDisplayDaoImpl implements RsDisplayDao {

    @Autowired
    private StoredProcExec storedProcExec = null;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultSet getDisplayColumns(String tableName) {

        ResultSet returnVal = null;

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("tableName", tableName);
        SpGetDisplayNamesForTable spGetDisplayNamesForTable = new SpGetDisplayNamesForTable(parameters);
        storedProcExec.doWithConnection(spGetDisplayNamesForTable);
        returnVal = spGetDisplayNamesForTable.getResultSet();

        return returnVal;
    }


} // RsProjectDaoImpl
