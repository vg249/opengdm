package org.gobiiproject.gobiidao.resultset.access;

import org.gobiiproject.gobiidao.GobiiDaoException;

import java.sql.ResultSet;
import java.util.Map;

/**
 * Created by Angel on 4/27/2016.
 */
public interface RsMarkerGroupDao {


    ResultSet getMarkerGroupNames() throws GobiiDaoException;
    ResultSet getMarkerGroupDetailByMarkerGroupId(Integer markerGroupId) throws GobiiDaoException;
    Integer createMarkerGroup(Map<String, Object> parameters) throws GobiiDaoException;
    ResultSet getMarkersByMarkerName(String markerName ) throws  GobiiDaoException;
    void createUpdateMarkerGroupMarker(Map<String, Object> parameters) throws GobiiDaoException;

//    ResultSet getContactsForRoleName(String roleName) throws GobiiDaoException;
}
