package org.gobiiproject.gobiidao.resultset.access;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiimodel.dto.container.project.ProjectProperty;

import java.sql.ResultSet;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Phil on 4/7/2016.
 */
public interface RsProjectDao {


    ResultSet getProjectNamesForContactId(Integer contactId) throws GobiiDaoException;
    ResultSet getProjectDetailsForProjectId(Integer projectId ) throws GobiiDaoException;
    ResultSet getPropertiesForProject(Integer projectId ) throws GobiiDaoException;
    Integer createProject(Map<String,Object> parameters) throws GobiiDaoException;
    void createUpdateProperty(Map<String, Object> parameters) throws GobiiDaoException;
}
