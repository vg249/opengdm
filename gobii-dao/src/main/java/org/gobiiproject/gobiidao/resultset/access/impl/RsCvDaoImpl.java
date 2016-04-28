package org.gobiiproject.gobiidao.resultset.access.impl;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.resultset.access.RsCvDao;
import org.gobiiproject.gobiidao.resultset.access.RsExperimentDao;
import org.gobiiproject.gobiidao.resultset.core.StoredProcExec;
import org.gobiiproject.gobiidao.resultset.sqlworkers.read.SpGetCvGroups;
import org.gobiiproject.gobiidao.resultset.sqlworkers.read.SpGetCvTerms;
import org.gobiiproject.gobiidao.resultset.sqlworkers.read.SpGetCvTermsByGroup;
import org.gobiiproject.gobiidao.resultset.sqlworkers.read.SpGetExperimentDetailsByExperimentId;
import org.gobiiproject.gobiidao.resultset.sqlworkers.read.SpGetExperimentNamesByProjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Angel on 4/19/2016.
 */
public class RsCvDaoImpl implements RsCvDao {


    Logger LOGGER = LoggerFactory.getLogger(RsExperimentDao.class);

    @Autowired
    private StoredProcExec storedProcExec = null;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultSet getCvTermsByGroup(String groupName) throws GobiiDaoException {

        ResultSet returnVal = null;

        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("groupName", groupName);
            SpGetCvTermsByGroup spGetCvTermsByGroup = new SpGetCvTermsByGroup(parameters);

            storedProcExec.doWithConnection(spGetCvTermsByGroup);

            returnVal = spGetCvTermsByGroup.getResultSet();
        } catch (Exception e) {

            LOGGER.error("Error retrieving CV terms by group", e);
            throw (new GobiiDaoException(e));

        }


        return returnVal;
    }

	@Override
    @Transactional(propagation = Propagation.REQUIRED)
	public ResultSet getCvGroups() throws GobiiDaoException {
		// TODO Auto-generated method stub
		 ResultSet returnVal = null;

	        try {
	            Map<String, Object> parameters = new HashMap<>();
	            SpGetCvGroups spGetCvGroups = new SpGetCvGroups();

	            storedProcExec.doWithConnection(spGetCvGroups);

	            returnVal = spGetCvGroups.getResultSet();
	        } catch (Exception e) {

	            LOGGER.error("Error retrieving CV groups", e);
	            throw (new GobiiDaoException(e));

	        }


	        return returnVal;
	}

	@Override
    @Transactional(propagation = Propagation.REQUIRED)
	public ResultSet getCvNames() throws GobiiDaoException {
		// TODO Auto-generated method stub
		 ResultSet returnVal = null;

	        try {
	            Map<String, Object> parameters = new HashMap<>();
	            SpGetCvTerms spGetCvTerms = new SpGetCvTerms();

	            storedProcExec.doWithConnection(spGetCvTerms);

	            returnVal = spGetCvTerms.getResultSet();
	        } catch (Exception e) {

	            LOGGER.error("Error retrieving CV groups", e);
	            throw (new GobiiDaoException(e));

	        }


	        return returnVal;
	}


} // RsProjectDaoImpl
