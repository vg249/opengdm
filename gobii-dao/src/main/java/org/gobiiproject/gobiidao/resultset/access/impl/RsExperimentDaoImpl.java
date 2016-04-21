package org.gobiiproject.gobiidao.resultset.access.impl;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.resultset.access.RsExperimentDao;
import org.gobiiproject.gobiidao.resultset.access.RsProjectDao;
import org.gobiiproject.gobiidao.resultset.core.StoredProcExec;
import org.gobiiproject.gobiidao.resultset.sqlworkers.read.SpGetProjecttNamesByContactId;
import org.gobiiproject.gobiidao.resultset.sqlworkers.read.SpGetPropertiesForProject;
import org.gobiiproject.gobiidao.resultset.sqlworkers.SpGetExperimentDetailsByExperimentId;
import org.gobiiproject.gobiidao.resultset.sqlworkers.SpGetExperimentNamesByProjectId;
import org.gobiiproject.gobiidao.resultset.sqlworkers.read.SpGetProjectDetailsByProjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Angel on 4/19/2016.
 */
public class RsExperimentDaoImpl implements RsExperimentDao {

    @Autowired
    private StoredProcExec storedProcExec = null;

    @Transactional(propagation = Propagation.REQUIRED)
	@Override
	public ResultSet getExperimentNamesByProjectId(Integer projectId) throws GobiiDaoException {

      ResultSet returnVal = null;
      Map<String, Object> parameters = new HashMap<>();
      parameters.put("projectId", projectId);
      SpGetExperimentNamesByProjectId spGetExperimentNamesByProjectId = new SpGetExperimentNamesByProjectId(parameters);

      storedProcExec.doWithConnection(spGetExperimentNamesByProjectId);

      returnVal = spGetExperimentNamesByProjectId.getResultSet();


      return returnVal;
	}


    @Transactional(propagation = Propagation.REQUIRED)
	@Override
	public ResultSet getExperimentDetailsForExperimentId(int experimentId) {
		// TODO Auto-generated method stub
		 ResultSet returnVal = null;

	        Map<String, Object> parameters = new HashMap<>();
	        parameters.put("experimentId", experimentId);
	        SpGetExperimentDetailsByExperimentId spGetExperimentDetailsByExperimentId = new SpGetExperimentDetailsByExperimentId(parameters);
	        storedProcExec.doWithConnection(spGetExperimentDetailsByExperimentId);
	        returnVal = spGetExperimentDetailsByExperimentId.getResultSet();

	        return returnVal;
	}


} // RsProjectDaoImpl
