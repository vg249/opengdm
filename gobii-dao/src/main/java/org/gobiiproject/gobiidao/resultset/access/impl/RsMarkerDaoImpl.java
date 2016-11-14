package org.gobiiproject.gobiidao.resultset.access.impl;

import org.apache.commons.lang.NotImplementedException;
import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.resultset.access.RsMarkerDao;
import org.gobiiproject.gobiidao.resultset.core.SpRunnerCallable;
import org.gobiiproject.gobiidao.resultset.core.StoredProcExec;
import org.gobiiproject.gobiidao.resultset.sqlworkers.modify.SpInsMarker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Phil on 4/8/2016.
 */
public class RsMarkerDaoImpl implements RsMarkerDao {

    Logger LOGGER = LoggerFactory.getLogger(RsMarkerDaoImpl.class);


    @Autowired
    private StoredProcExec storedProcExec = null;

    @Autowired
    private SpRunnerCallable spRunnerCallable;


    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultSet getMarkerDetailsByMarkerId(Integer markerId) throws GobiiDaoException {

        throw new NotImplementedException();
//        ResultSet returnVal = null;
//
//        Map<String, Object> parameters = new HashMap<>();
//        parameters.put("markerId", markerId);
//        SpGetMarkerDetailsByMarkerId spGetMarkerDetailsByExperimentId = new SpGetMarkerDetailsByMarkerId(parameters);
//
//        storedProcExec.doWithConnection(spGetMarkerDetailsByExperimentId);
//
//        returnVal = spGetMarkerDetailsByExperimentId.getResultSet();
//
//
//        return returnVal;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Integer createMarker(Map<String, Object> parameters) throws GobiiDaoException {


        Integer returnVal = null;

        if (spRunnerCallable.run(new SpInsMarker(), parameters)) {

            returnVal = spRunnerCallable.getResult();

        } else {

            throw new GobiiDaoException(spRunnerCallable.getErrorString());

        }


        return returnVal;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateMarker(Map<String, Object> parameters) throws GobiiDaoException {

        throw new NotImplementedException();
//        try {
//
//            if (!spRunnerCallable.run(new SpUpdMarker(), parameters)) {
//                throw new GobiiDaoException(spRunnerCallable.getErrorString());
//            }
//
//        } catch (Exception e) {
//
//            LOGGER.error("Error creating marker", e);
//            throw (new GobiiDaoException(e));
//        }

    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultSet getMarkers() throws GobiiDaoException {
        // TODO Auto-generated method stub

        throw new NotImplementedException();
//        ResultSet returnVal = null;
//
//        SpGetMarkerNames spGetMarkerNames = new SpGetMarkerNames();
//
//        storedProcExec.doWithConnection(spGetMarkerNames);
//
//        returnVal = spGetMarkerNames.getResultSet();
//
//
//        return returnVal;
    }


} // RsProjectDaoImpl
