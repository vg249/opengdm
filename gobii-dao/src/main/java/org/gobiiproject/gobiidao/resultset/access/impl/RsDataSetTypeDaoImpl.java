package org.gobiiproject.gobiidao.resultset.access.impl;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.resultset.access.RsDataSetTypeDao;
import org.gobiiproject.gobiidao.resultset.core.SpRunnerCallable;
import org.gobiiproject.gobiidao.resultset.core.StoredProcExec;
import org.gobiiproject.gobiidao.resultset.sqlworkers.read.SpGetDatasetTypeNames;
import org.hibernate.exception.SQLGrammarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;

/**
 * Created by VCalaminos on 2017-01-13.
 */
public class RsDataSetTypeDaoImpl implements RsDataSetTypeDao {

    Logger LOGGER = LoggerFactory.getLogger(RsDataSetTypeDaoImpl.class);


    @Autowired
    private StoredProcExec storedProcExec = null;

    @Autowired
    private SpRunnerCallable spRunnerCallable;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultSet getDatasetTypeNames() throws GobiiDaoException {

        ResultSet returnVal = null;

        try {

            SpGetDatasetTypeNames spGetDatasetTypeNames = new SpGetDatasetTypeNames();

            storedProcExec.doWithConnection(spGetDatasetTypeNames);

            returnVal = spGetDatasetTypeNames.getResultSet();

        } catch (SQLGrammarException e) {

            LOGGER.error("Error retrieving dataset type names with SQL " + e.getSQL(), e.getSQLException());
            throw (new GobiiDaoException(e.getSQLException()));
        }

        return returnVal;

    } // getDatasetTypeNames



}
