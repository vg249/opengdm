package org.gobiiproject.gobiidao.resultset.access;

import java.sql.ResultSet;
import java.util.Map;
import org.gobiiproject.gobiidao.GobiiDaoException;

/**
 * Created by Phil on 4/21/2016.
 */
public interface RsMarkerDao {

    ResultSet getMarkerDetailsByMarkerId(Integer projectId) throws GobiiDaoException;
    ResultSet getMarkersByMarkerName(String markerName) throws GobiiDaoException;
    Integer createMarker(Map<String, Object> parameters) throws GobiiDaoException;
    void updateMarker(Map<String, Object> parameters) throws GobiiDaoException;
	ResultSet getMarkers() throws GobiiDaoException;
}
