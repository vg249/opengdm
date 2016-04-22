package org.gobiiproject.gobiidao.resultset.access.impl;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.resultset.access.RsAnalysisDao;
import org.gobiiproject.gobiidao.resultset.core.StoredProcExec;
import org.gobiiproject.gobiidao.resultset.sqlworkers.read.SpGetAnalysisDetailsByAnalysisId;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Phil on 4/22/2016.
 */
public class RsAnalysisDaoImpl implements RsAnalysisDao {

    @Autowired
    private StoredProcExec storedProcExec = null;


    @Override
    public ResultSet getAnalysisDetailsByAnalysisId(Integer analysisId) throws GobiiDaoException {

        ResultSet returnVal = null;

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("analysisId", analysisId);
        SpGetAnalysisDetailsByAnalysisId spGetDatasetDetailsByExperimentId = new SpGetAnalysisDetailsByAnalysisId(parameters);

        storedProcExec.doWithConnection(spGetDatasetDetailsByExperimentId);

        returnVal = spGetDatasetDetailsByExperimentId.getResultSet();

        return returnVal;

    } // getAnalysisDetailsByAnalysisId()

}
