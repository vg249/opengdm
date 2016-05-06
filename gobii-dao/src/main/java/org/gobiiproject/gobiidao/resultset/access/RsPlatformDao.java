package org.gobiiproject.gobiidao.resultset.access;

import org.gobiiproject.gobiidao.GobiiDaoException;

import java.sql.ResultSet;
import java.util.Map;

/**
 * Created by Phil on 4/7/2016.
 */
public interface RsPlatformDao {


    ResultSet getPlatformNames() throws GobiiDaoException;
    ResultSet getPlatformDetailsByPlatformId(Integer platformId) throws GobiiDaoException;
    Integer createPlatform(Map<String,Object> parameters) throws GobiiDaoException;
    void updatePlatform(Map<String, Object> parameters) throws GobiiDaoException;

}
