package org.gobiiproject.gobiidao.resultset.access;

import org.gobiiproject.gobiidao.GobiiDaoException;

import java.sql.ResultSet;

/**
 * Created by Phil on 4/22/2016.
 */
public interface RsAnalysisDao {
    ResultSet getAnalysisDetailsByAnalysisId(Integer analysisId) throws GobiiDaoException;

	ResultSet getAnalysisNames();
}
