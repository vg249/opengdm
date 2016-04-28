package org.gobiiproject.gobiidao.resultset.access.impl;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.resultset.access.RsMapSetDao;
import org.gobiiproject.gobiidao.resultset.core.StoredProcExec;
import org.gobiiproject.gobiidao.resultset.sqlworkers.read.SpGetDatasetFileNamesByExperimentId;
import org.gobiiproject.gobiidao.resultset.sqlworkers.read.SpGetMapNamesByTypeId;
import org.gobiiproject.gobiidao.resultset.sqlworkers.read.SpGetMapSetNames;
import org.gobiiproject.gobiidao.resultset.sqlworkers.read.SpGetMapsetDetailsByMapsetId;
import org.gobiiproject.gobiidao.resultset.sqlworkers.read.SpGetPlatformDetailsByPlatformId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Phil on 4/7/2016.
 */
public class RsMapSetDaoImpl implements RsMapSetDao {

    Logger LOGGER = LoggerFactory.getLogger(RsMapSetDaoImpl.class);


    @Autowired
    private StoredProcExec storedProcExec = null;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ResultSet getMapNames() throws GobiiDaoException {
        ResultSet returnVal = null;

        try {
            SpGetMapSetNames spGetMapSetNames = new SpGetMapSetNames();
            storedProcExec.doWithConnection(spGetMapSetNames);
            returnVal = spGetMapSetNames.getResultSet();
        } catch (Exception e) {

            LOGGER.error("Error retrieving mapset names", e);
            throw (new GobiiDaoException(e));

        }


        return returnVal;

    }

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ResultSet getMapNamesByTypeId(int typeId) throws GobiiDaoException {
		// TODO Auto-generated method stub

        ResultSet returnVal = null;

        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("typeId", typeId);
            SpGetMapNamesByTypeId spGetMapNamesByTypeId = new SpGetMapNamesByTypeId(parameters);

            storedProcExec.doWithConnection(spGetMapNamesByTypeId);

            returnVal = spGetMapNamesByTypeId.getResultSet();

        } catch (Exception e) {

            LOGGER.error("Error retrieving map names", e);
            throw (new GobiiDaoException(e));

        }

        return returnVal;
	}

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultSet getMapsetDetailsByMapsetId(int mapSetId) throws GobiiDaoException {
        ResultSet returnVal = null;

        try {

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("mapSetId", mapSetId);
            SpGetMapsetDetailsByMapsetId spGetMapsetDetailsByMapsetId= new SpGetMapsetDetailsByMapsetId(parameters);

            storedProcExec.doWithConnection(spGetMapsetDetailsByMapsetId);

            returnVal = spGetMapsetDetailsByMapsetId.getResultSet();

        } catch (Exception e) {

            LOGGER.error("Error retrieving mapset details", e);
            throw (new GobiiDaoException(e));

        }

        return returnVal;
    }
}
