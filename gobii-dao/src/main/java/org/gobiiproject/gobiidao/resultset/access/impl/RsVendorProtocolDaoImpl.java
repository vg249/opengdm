package org.gobiiproject.gobiidao.resultset.access.impl;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.resultset.core.SpRunnerCallable;
import org.gobiiproject.gobiidao.resultset.core.StoredProcExec;
import org.gobiiproject.gobiidao.resultset.sqlworkers.modify.SpInsVendorProtocol;
import org.gobiiproject.gobiidao.resultset.sqlworkers.modify.SpUpdVendorProtocol;
import org.hibernate.exception.SQLGrammarException;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.gobiiproject.gobiidao.resultset.access.RsVendorProtocolDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
/**
 * Created by VCalaminos on 2016-12-12.
 */
public class RsVendorProtocolDaoImpl implements RsVendorProtocolDao {

    Logger LOGGER = LoggerFactory.getLogger(RsVendorProtocolDao.class);

    @Autowired
    private StoredProcExec storedProcExec = null;

    @Autowired
    private SpRunnerCallable spRunnerCallable;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Integer createVendorProtocol(Map<String, Object> paramaters) throws GobiiDaoException{
        Integer returnVal = null;

        try {

            spRunnerCallable.run(new SpInsVendorProtocol(), paramaters);
            returnVal = spRunnerCallable.getResult();
        } catch (SQLGrammarException e){
            LOGGER.error("Error creating dataset with SQL " + e.getSQL(), e.getSQLException());
            throw (new GobiiDaoException(e.getSQLException()));
        }

        return returnVal;

    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateVendorProtocol(Map<String, Object> parameters) throws GobiiDaoException {

        try {
            spRunnerCallable.run(new SpUpdVendorProtocol(), parameters);

        } catch (SQLGrammarException e) {
            LOGGER.error("Error updating vendor protocol with SQL " + e.getSQL(), e.getSQLException());
            throw (new GobiiDaoException(e.getSQLException()));

        }
    }

}
