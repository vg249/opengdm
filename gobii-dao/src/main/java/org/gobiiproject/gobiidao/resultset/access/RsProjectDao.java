package org.gobiiproject.gobiidao.resultset.access;

import java.sql.ResultSet;
import java.util.Map;
import org.gobiiproject.gobiidao.GobiiDaoException;

/**
 * Created by Phil on 4/7/2016.
 */
public interface RsProjectDao {


    ResultSet getProjectNamesForContactId(Integer contactId) throws GobiiDaoException;
    ResultSet getProjectDetailsForProjectId(Integer projectId ) throws GobiiDaoException;
    ResultSet getPropertiesForProject(Integer projectId ) throws GobiiDaoException;
    ResultSet getProjectsByNameAndPiContact(String projectName, Integer piContactId ) throws GobiiDaoException;
    Integer createProject(Map<String,Object> parameters) throws GobiiDaoException;
    void updateProject(Map<String,Object> parameters) throws GobiiDaoException;
    Integer createUpdateProjectProperty(Map<String, Object> parameters) throws GobiiDaoException;
	ResultSet getProjectNames()throws GobiiDaoException;
	ResultSet getProjectsForLoadedDatasets() throws GobiiDaoException;
}
