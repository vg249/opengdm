// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-24
// ************************************************************************
package org.gobiiproject.gobiidao.entityaccess;

import org.gobiiproject.gobiidao.core.Dao;
import org.gobiiproject.gobiidao.entities.Marker;

import java.util.List;
import java.util.Map;

/**
 * Created by Phil on 3/24/2016.
 */
public interface PingDao extends Dao<Marker> {
    List<String> getPingResponses(List<String> pingRequests);
}
