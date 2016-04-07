// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-24
// ************************************************************************
package org.gobiiproject.gobiidao.entities.access;

import org.gobiiproject.gobiidao.entities.core.EntityDao;
import org.gobiiproject.gobiidao.entities.pojos.Marker;

import java.util.List;

/**
 * Created by Phil on 3/24/2016.
 */
public interface PingDao extends EntityDao<Marker> {
    List<String> getPingResponses(List<String> pingRequests);
}
