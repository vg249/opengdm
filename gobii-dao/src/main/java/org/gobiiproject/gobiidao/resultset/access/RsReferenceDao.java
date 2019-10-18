package org.gobiiproject.gobiidao.resultset.access;

import java.sql.ResultSet;
import java.util.Map;
import org.gobiiproject.gobiidao.GobiiDaoException;

/**
 * Created by Phil on 4/7/2016.
 */
public interface RsReferenceDao {


    ResultSet getReferenceNames() throws GobiiDaoException;
    ResultSet getReferenceDetailsByReferenceId(Integer referenceId) throws GobiiDaoException;
    Integer createReference( Map<String, Object> parameters) throws GobiiDaoException;
    void updateReference(Map<String, Object> parameters) throws GobiiDaoException;
}
