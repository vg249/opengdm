package org.gobiiproject.gobiidao.resultset.access.impl;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.resultset.access.RsProjectDao;
import org.gobiiproject.gobiidao.resultset.core.StoredProcExec;
import org.gobiiproject.gobiidao.resultset.sqlworkers.SpGetProjecttNamesByContactId;
import org.gobiiproject.gobiidao.resultset.sqlworkers.SpGetPropertiesForProject;
import org.gobiiproject.gobiidao.resultset.sqlworkers.SpGetProjectDetailsByProjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Phil on 4/8/2016.
 */
public class RsProjectDaoImpl implements RsProjectDao {

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

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultSet getPropertiesForProject(Integer projectId) throws GobiiDaoException {

        ResultSet returnVal = null;

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("projectId", projectId);
        SpGetPropertiesForProject spGetPropertiesForProject = new SpGetPropertiesForProject(parameters);
        storedProcExec.doWithConnection(spGetPropertiesForProject);
        returnVal = spGetPropertiesForProject.getResultSet();

        return returnVal;

    } // getPropertiesForProject

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Integer createProject(String projectName,
                                 String projectCode,
                                 String projectDescription,
                                 Integer piContact,
                                 Integer createdBy,
                                 Date createdDate,
                                 Integer modifiedby,
                                 Date modifiedDate,
                                 Integer projectStatus) throws GobiiDaoException {

        Integer returnVal = null;

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("projectName", projectName);
        parameters.put("projectCode", projectCode);
        parameters.put("projectDescription", projectDescription);
        parameters.put("piContact", piContact);
        parameters.put("createdBy", createdBy);
        parameters.put("createdDate", createdDate);
        parameters.put("modifiedby", modifiedby);
        parameters.put("modifiedDate", modifiedDate);
        parameters.put("projectStatus", projectStatus);

        return  returnVal;

    } // createProject


} // RsProjectDaoImpl
