package org.gobiiproject.gobiidao.resultset.access.impl;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.resultset.access.RsDataSetDao;
import org.gobiiproject.gobiidao.resultset.core.SpRunnerCallable;
import org.gobiiproject.gobiidao.resultset.core.StoredProcExec;
import org.gobiiproject.gobiidao.resultset.sqlworkers.read.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Phil on 4/8/2016.
 */
public class RsDataSetDaoImpl implements RsDataSetDao {

    @Autowired
    private StoredProcExec storedProcExec = null;

    @Autowired
    private SpRunnerCallable spRunnerCallable;


    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultSet getDatasetFileNamesByExperimentId(Integer experimentId) throws GobiiDaoException {

        ResultSet returnVal = null;

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("experimentId", experimentId);
        SpGetDatasetFileNamesByExperimentId spGetDatasetFileNamesByExperimentId = new SpGetDatasetFileNamesByExperimentId(parameters);

        storedProcExec.doWithConnection(spGetDatasetFileNamesByExperimentId);

        returnVal = spGetDatasetFileNamesByExperimentId.getResultSet();


        return returnVal;

    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultSet getDataSetDetailsByDataSetId(Integer dataSetId) throws GobiiDaoException {

        ResultSet returnVal = null;

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("dataSetId", dataSetId);
        SpGetDatasetDetailsByDataSetId spGetDatasetDetailsByExperimentId = new SpGetDatasetDetailsByDataSetId(parameters);

        storedProcExec.doWithConnection(spGetDatasetDetailsByExperimentId);

        returnVal = spGetDatasetDetailsByExperimentId.getResultSet();

        return returnVal;

    }


//    @Transactional(propagation = Propagation.REQUIRED)
//    @Override
//    public ResultSet getProjectNamesForContactId(Integer contactId) throws GobiiDaoException {
//
//        ResultSet returnVal = null;
//        Map<String, Object> parameters = new HashMap<>();
//        parameters.put("contactId", contactId);
//        SpGetProjecttNamesByContactId spGetProjecttNamesByContactId = new SpGetProjecttNamesByContactId(parameters);
//
//        storedProcExec.doWithConnection(spGetProjecttNamesByContactId);
//
//        returnVal = spGetProjecttNamesByContactId.getResultSet();
//
//
//        return returnVal;
//
//    }
//
//    @Transactional(propagation = Propagation.REQUIRED)
//    @Override
//    public ResultSet getProjectDetailsForProjectId(Integer projectId) throws GobiiDaoException {
//        ResultSet returnVal;
//
//        Map<String, Object> parameters = new HashMap<>();
//        parameters.put("projectId", projectId);
//        SpGetProjectDetailsByProjectId spGetProjectDetailsByProjectId = new SpGetProjectDetailsByProjectId(parameters);
//        storedProcExec.doWithConnection(spGetProjectDetailsByProjectId);
//        returnVal = spGetProjectDetailsByProjectId.getResultSet();
//
//        return returnVal;
//
//    } // getProjectDetailsForProjectId()
//
//    @Transactional(propagation = Propagation.REQUIRED)
//    @Override
//    public ResultSet getPropertiesForProject(Integer projectId) throws GobiiDaoException {
//
//        ResultSet returnVal = null;
//
//        Map<String, Object> parameters = new HashMap<>();
//        parameters.put("projectId", projectId);
//        SpGetPropertiesForProject spGetPropertiesForProject = new SpGetPropertiesForProject(parameters);
//        storedProcExec.doWithConnection(spGetPropertiesForProject);
//        returnVal = spGetPropertiesForProject.getResultSet();
//
//        return returnVal;
//
//    } // getPropertiesForProject
//
//    @Transactional(propagation = Propagation.REQUIRED)
//    @Override
//    public Integer createProject(Map<String, Object> parameters) throws GobiiDaoException {
//
//        Integer returnVal = null;
//
//        if (spRunnerCallable.run(new SpInsProject(), parameters)) {
//
//            returnVal = spRunnerCallable.getResult();
//
//        } else {
//
//            throw new GobiiDaoException(spRunnerCallable.getErrorString());
//
//        }
//
//        return returnVal;
//
//    } // createProject
//
//    @Transactional(propagation = Propagation.REQUIRED)
//    @Override
//    public Integer createUpdateProperty(Map<String, Object> parameters) throws GobiiDaoException {
//
//        Integer returnVal = 0;
//
//        spRunnerCallable.run(new SpInsProjectProperties(), parameters);
//        returnVal = spRunnerCallable.getResult();
//
//        return returnVal;
//
//    } // createUpdateProperty
//

} // RsProjectDaoImpl
