package org.gobiiproject.gobiidao.resultset.access;

import java.sql.ResultSet;
import java.util.Map;
import org.gobiiproject.gobiidao.GobiiDaoException;

/**
 * Created by Phil on 4/7/2016.
 */
public interface RsOrganizationDao {


    ResultSet getOrganizationNames() throws GobiiDaoException;
    ResultSet getOrganizationDetailsByOrganizationId(Integer organizationId) throws GobiiDaoException;
    Integer createOrganization(Map<String,Object> parameters) throws GobiiDaoException;
    void updateOrganization(Map<String, Object> parameters) throws GobiiDaoException;
}
