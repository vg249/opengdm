package org.gobiiproject.gobiidao.resultset.access.impl;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.resultset.access.RsProject;
import org.gobiiproject.gobiidao.resultset.core.StoredProcExec;
import org.gobiiproject.gobiidao.resultset.core.StoredProcUtils;
import org.gobiiproject.gobiidao.resultset.spworkers.SpGetContactNamesByRoleName;
import org.gobiiproject.gobiidao.resultset.spworkers.SpGetProjecttNamesByContactId;
import org.gobiiproject.gobiidao.resultset.viewworkers.SpGetProjectDetailsByProjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Phil on 4/8/2016.
 */
public class RsProjectImpl implements RsProject {

    @Autowired
    private StoredProcExec storedProcExec = null;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultSet getProjectNamesForContactId(Integer contactId) throws GobiiDaoException {

        ResultSet returnVal = null;
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("contactId", contactId);
        SpGetProjecttNamesByContactId spGetProjecttNamesByContactId = new SpGetProjecttNamesByContactId(parameters);

        storedProcExec.doWithConnection(spGetProjecttNamesByContactId);

        returnVal = spGetProjecttNamesByContactId.getResultSet();


        return returnVal;

    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultSet getProjectDetailsForProjectId(Integer projectId) throws GobiiDaoException {
        ResultSet returnVal;

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("projectId", projectId);
        SpGetProjectDetailsByProjectId spGetProjectDetailsByProjectId = new SpGetProjectDetailsByProjectId(parameters);
        storedProcExec.doWithConnection(spGetProjectDetailsByProjectId);
        returnVal = spGetProjectDetailsByProjectId.getResultSet();

        return returnVal;

    } // getProjectDetailsForProjectId()




} // RsProjectImpl
