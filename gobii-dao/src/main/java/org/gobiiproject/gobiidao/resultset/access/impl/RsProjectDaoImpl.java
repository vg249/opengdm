package org.gobiiproject.gobiidao.resultset.access.impl;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.resultset.access.RsProjectDao;
import org.gobiiproject.gobiidao.resultset.core.SpRunnerCallable;
import org.gobiiproject.gobiidao.resultset.core.StoredProcExec;
import org.gobiiproject.gobiidao.resultset.sqlworkers.modify.SpInsProject;
import org.gobiiproject.gobiidao.resultset.sqlworkers.modify.SpInsProjectProperties;
import org.gobiiproject.gobiidao.resultset.sqlworkers.modify.SpUpdProject;
import org.gobiiproject.gobiidao.resultset.sqlworkers.read.sp.*;
import org.hibernate.exception.SQLGrammarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.util.*;

/**
 * Created by Phil on 4/8/2016.
 */
public class RsProjectDaoImpl implements RsProjectDao {

    Logger LOGGER = LoggerFactory.getLogger(RsProjectDaoImpl.class);


    @Autowired
    private StoredProcExec storedProcExec = null;

    @Autowired
    private SpRunnerCallable spRunnerCallable;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultSet getProjectNamesForContactId(Integer contactId) throws GobiiDaoException {

        ResultSet returnVal = null;

        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("contactId", contactId);
            SpGetProjecttNamesByContactId spGetProjecttNamesByContactId = new SpGetProjecttNamesByContactId(parameters);

            storedProcExec.doWithConnection(spGetProjecttNamesByContactId);

            returnVal = spGetProjecttNamesByContactId.getResultSet();

        } catch (SQLGrammarException e) {

            LOGGER.error("Error retrieving project names", e.getSQL(), e.getSQLException());
            throw (new GobiiDaoException(e.getSQLException()));

        }

        return returnVal;

    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultSet getProjectDetailsForProjectId(Integer projectId) throws GobiiDaoException {

        ResultSet returnVal;

        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("projectId", projectId);
            SpGetProjectDetailsByProjectId spGetProjectDetailsByProjectId = new SpGetProjectDetailsByProjectId(parameters);
            storedProcExec.doWithConnection(spGetProjectDetailsByProjectId);
            returnVal = spGetProjectDetailsByProjectId.getResultSet();
        } catch (SQLGrammarException e) {

            LOGGER.error("Error retrieving project details", e.getSQL(), e.getSQLException());
            throw (new GobiiDaoException(e.getSQLException()));

        }


        return returnVal;

    } // getProjectDetailsForProjectId()

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultSet getPropertiesForProject(Integer projectId) throws GobiiDaoException {

        ResultSet returnVal = null;

        try {

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("projectId", projectId);
            SpGetPropertiesForProject spGetPropertiesForProject = new SpGetPropertiesForProject(parameters);
            storedProcExec.doWithConnection(spGetPropertiesForProject);
            returnVal = spGetPropertiesForProject.getResultSet();

        } catch (SQLGrammarException e) {

            LOGGER.error("Error retrieving project properties", e.getSQL(), e.getSQLException());
            throw (new GobiiDaoException(e.getSQLException()));

        }


        return returnVal;

    } // getPropertiesForProject

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultSet getProjectsByNameAndPiContact(String projectName, Integer piContactId) throws GobiiDaoException {
        ResultSet returnVal = null;

        try {

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("piContactId", piContactId);
            parameters.put("projectName", projectName);
            SpGetProjectByNameAndPIContact spGetProjectByNameAndPIContact = new SpGetProjectByNameAndPIContact(parameters);
            storedProcExec.doWithConnection(spGetProjectByNameAndPIContact);
            returnVal = spGetProjectByNameAndPIContact.getResultSet();

        } catch (SQLGrammarException e) {

            LOGGER.error("Error retrieving projects", e.getSQL(), e.getSQLException());
            throw (new GobiiDaoException(e.getSQLException()));

        }

        return returnVal;

    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Integer createProject(Map<String, Object> parameters) throws GobiiDaoException {

        Integer returnVal = null;

        try {

            spRunnerCallable.run(new SpInsProject(), parameters);
            returnVal = spRunnerCallable.getResult();

        } catch (SQLGrammarException e) {

            LOGGER.error("Error creating project with SQL " + e.getSQL(), e.getSQLException());
            throw (new GobiiDaoException(e.getSQLException()));

        }

        return returnVal;

    } // createProject

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateProject(Map<String, Object> parameters) throws GobiiDaoException {

        try {

            spRunnerCallable.run(new SpUpdProject(), parameters);

        } catch (SQLGrammarException e) {

            LOGGER.error("Error creating project with SQL " + e.getSQL(), e.getSQLException());
            throw (new GobiiDaoException(e.getSQLException()));

        }

    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Integer createUpdateProjectProperty(Map<String, Object> parameters) throws GobiiDaoException {

        Integer returnVal;

        try {
            spRunnerCallable.run(new SpInsProjectProperties(), parameters);
            returnVal = spRunnerCallable.getResult();

        } catch (SQLGrammarException e) {

            LOGGER.error("Error updating project property with SQL " + e.getSQL(), e.getSQLException());
            throw (new GobiiDaoException(e.getSQLException()));

        }

        return returnVal;

    } // createUpdateMapSetProperty

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultSet getProjectNames() throws GobiiDaoException {
        // TODO Auto-generated method stub

        ResultSet returnVal = null;

        try {
            Map<String, Object> parameters = new HashMap<>();
            SpGetProjectNames spGetProjectNames = new SpGetProjectNames();

            storedProcExec.doWithConnection(spGetProjectNames);

            returnVal = spGetProjectNames.getResultSet();

        } catch (SQLGrammarException e) {

            LOGGER.error("Error retrieving project names with SQL " + e.getSQL(), e.getSQLException());
            throw (new GobiiDaoException(e.getSQLException()));

        }

        return returnVal;

    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultSet getProjectsForLoadedDatasets() throws GobiiDaoException {

        ResultSet returnVal = null;

        try {

            Map<String, Object> parameters = new HashMap<>();
            SpGetProjectsForLoadedDatasets spGetProjectsForLoadedDatasets = new SpGetProjectsForLoadedDatasets();

            storedProcExec.doWithConnection(spGetProjectsForLoadedDatasets);

            returnVal = spGetProjectsForLoadedDatasets.getResultSet();

        } catch (SQLGrammarException e) {

            LOGGER.error("Error retrieving project names for datasets with SQL " + e.getSQL(), e.getSQLException());
            throw (new GobiiDaoException(e.getSQLException()));
        }

        return returnVal;

    }

} // RsProjectDaoImpl
