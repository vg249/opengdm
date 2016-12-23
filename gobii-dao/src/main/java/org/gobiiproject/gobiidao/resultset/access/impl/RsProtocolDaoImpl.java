package org.gobiiproject.gobiidao.resultset.access.impl;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.resultset.access.RsProtocolDao;

import org.gobiiproject.gobiidao.resultset.core.SpRunnerCallable;
import org.gobiiproject.gobiidao.resultset.core.StoredProcExec;
import org.gobiiproject.gobiidao.resultset.sqlworkers.modify.SpInsProtocol;
import org.gobiiproject.gobiidao.resultset.sqlworkers.modify.SpInsVendorProtocol;
import org.gobiiproject.gobiidao.resultset.sqlworkers.modify.SpUpdProtocol;
import org.gobiiproject.gobiidao.resultset.sqlworkers.read.SpGetProjectNames;
import org.gobiiproject.gobiidao.resultset.sqlworkers.read.SpGetProtocolDetailsByProtocolId;
import org.gobiiproject.gobiidao.resultset.sqlworkers.read.SpGetProtocolNames;
import org.gobiiproject.gobiidao.resultset.sqlworkers.read.SpGetProtocolVendorByName;
import org.gobiiproject.gobiidao.resultset.sqlworkers.read.SpGetVendorProtocolNames;
import org.hibernate.exception.SQLGrammarException;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.util.Map;
import java.util.HashMap;

/**
 * Created by VCalaminos on 2016-12-14.
 */
public class RsProtocolDaoImpl implements RsProtocolDao {

    Logger LOGGER = LoggerFactory.getLogger(RsProtocolDao.class);

    @Autowired
    private StoredProcExec storedProcExec = null;

    @Autowired
    private SpRunnerCallable spRunnerCallable;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Integer createProtocol(Map<String, Object> paramaters) throws GobiiDaoException {
        Integer returnVal = null;

        try {
            spRunnerCallable.run(new SpInsProtocol(), paramaters);
            returnVal = spRunnerCallable.getResult();
        } catch (SQLGrammarException e) {
            LOGGER.error("Error creating dataset with SQL " + e.getSQL(), e.getSQLException());
            throw (new GobiiDaoException(e.getSQLException()));
        }

        return returnVal;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateProtocol(Map<String, Object> parameters) throws GobiiDaoException {

        try {

            spRunnerCallable.run(new SpUpdProtocol(), parameters);

        } catch (SQLGrammarException e) {
            LOGGER.error("Error updating protocol with SQL " + e.getSQL(), e.getSQLException());
            throw (new GobiiDaoException(e.getSQLException()));
        }

    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultSet getProtocolDetailsByProtocolId(Integer protocolId) throws GobiiDaoException {

        ResultSet returnVal = null;

        try {

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("protocolId", protocolId);
            SpGetProtocolDetailsByProtocolId spGetProtocolDetailsByProtocolId = new SpGetProtocolDetailsByProtocolId(parameters);

            storedProcExec.doWithConnection(spGetProtocolDetailsByProtocolId);

            returnVal = spGetProtocolDetailsByProtocolId.getResultSet();

        } catch (Exception e) {

            LOGGER.error("Error retrieving protocol details", e);
            throw (new GobiiDaoException(e));

        }

        return returnVal;
    }


    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultSet getProtocolNames() throws GobiiDaoException {

        ResultSet returnVal;

        try {
            SpGetProtocolNames spGetProtocolNames = new SpGetProtocolNames();

            storedProcExec.doWithConnection(spGetProtocolNames);

            returnVal = spGetProtocolNames.getResultSet();

        } catch (SQLGrammarException e) {

            LOGGER.error("Error retrieving protocol names with SQL " + e.getSQL(), e.getSQLException());
            throw (new GobiiDaoException(e.getSQLException()));

        }

        return returnVal;

    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultSet getVendorProtocolNames() throws GobiiDaoException {

        ResultSet returnVal;

        try {
            SpGetVendorProtocolNames spGetVendorProtocolNames = new SpGetVendorProtocolNames();

            storedProcExec.doWithConnection(spGetVendorProtocolNames);

            returnVal = spGetVendorProtocolNames.getResultSet();

        } catch (SQLGrammarException e) {

            LOGGER.error("Error retrieving VendorProtocol names with SQL " + e.getSQL(), e.getSQLException());
            throw (new GobiiDaoException(e.getSQLException()));

        }

        return returnVal;

    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Integer createVendorProtocol(Map<String, Object> parameters) throws GobiiDaoException {
        Integer returnVal;

        try {
            spRunnerCallable.run(new SpInsVendorProtocol(), parameters);
            returnVal = spRunnerCallable.getResult();
        } catch (SQLGrammarException e) {
            LOGGER.error("Error creating vendor protocol record with SQL " + e.getSQL(), e.getSQLException());
            throw (new GobiiDaoException(e.getSQLException()));
        }

        return returnVal;

    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultSet getVendorByProtocolVendorName(Map<String, Object> parameters) throws GobiiDaoException {

        ResultSet returnVal;

        try {
            SpGetProtocolVendorByName spGetProtocolVendorByName = new SpGetProtocolVendorByName(parameters);

            storedProcExec.doWithConnection(spGetProtocolVendorByName);

            returnVal = spGetProtocolVendorByName.getResultSet();

        } catch (SQLGrammarException e) {

            LOGGER.error("Error retrieving VendorProtocol by name " + e.getSQL(), e.getSQLException());
            throw (new GobiiDaoException(e.getSQLException()));

        }

        return returnVal;


    }

}