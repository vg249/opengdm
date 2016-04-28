package org.gobiiproject.gobiidao.resultset.access;

import org.gobiiproject.gobiidao.GobiiDaoException;

import java.sql.ResultSet;

/**
 * Created by Phil on 4/7/2016.
 */
public interface RsPlatformDao {


    ResultSet getPlatformNames() throws GobiiDaoException;
    ResultSet getPlatformDetailsByPlatformId(Integer platformId) throws GobiiDaoException;

}
