package org.gobiiproject.gobiidao.resultset.access;

import org.gobiiproject.gobiidao.GobiiDaoException;

import java.sql.ResultSet;

/**
 * Created by Phil on 4/21/2016.
 */
public interface RsDataSetDao {

    ResultSet getDatasetFileNamesByExperimentId(Integer experimentId) throws GobiiDaoException;
    ResultSet getDataSetDetailsByDataSetId(Integer projectId) throws GobiiDaoException;
}
