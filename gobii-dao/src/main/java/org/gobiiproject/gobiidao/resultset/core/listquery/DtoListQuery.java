package org.gobiiproject.gobiidao.resultset.core.listquery;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.resultset.core.StoredProcExec;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.hibernate.exception.SQLGrammarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Phil on 10/25/2016.
 */
public class DtoListQuery<T> {

    Logger LOGGER = LoggerFactory.getLogger(DtoListQuery.class);

    private ListStatement listStatement;
    private Class<T> dtoType;
    private StoredProcExec storedProcExec;

    public DtoListQuery(StoredProcExec storedProcExec,
                        Class<T> dtoType,
                        ListStatement listStatement) {

        this.storedProcExec = storedProcExec;
        this.dtoType = dtoType;
        this.listStatement = listStatement;

    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<T> getDtoList(Map<String, Object> jdbcParameters, Map<String, Object> sqlParameters) throws GobiiException {

        List<T> returnVal;

        try {

            DtoListFromSql<T> dtoListFromSql = new DtoListFromSql<>(dtoType, listStatement, jdbcParameters,sqlParameters);
            this.storedProcExec.doWithConnection(dtoListFromSql);
            returnVal = dtoListFromSql.getDtoList();

        }catch(SQLGrammarException e) {
            LOGGER.error("Error retrieving dto list with SQL " + e.getSQL(), e.getSQLException());
            throw (new GobiiDaoException(e.getSQLException()));

        } catch (Exception e) {

            LOGGER.error("Error retrieving dto list ", e);
            throw (new GobiiDaoException(e));

        }

        return returnVal;

    } // getDtoList()

    @Transactional(propagation = Propagation.REQUIRED)
    public List<T> getPages(Integer pageSize,Integer pageNo, String pgQueryId) throws GobiiException {

        List<T> returnVal;

        try {

            Map<String, Object> jdbcParameters = new HashMap<>();
            Map<String, Object> sqlParameters = new HashMap<>();


            DtoListFromSql<T> dtoListFromSql = new DtoListFromSql<>(dtoType, listStatement, jdbcParameters,sqlParameters);
            this.storedProcExec.doWithConnection(dtoListFromSql);
            returnVal = dtoListFromSql.getDtoList();

        }catch(SQLGrammarException e) {
            LOGGER.error("Error retrieving dto list with SQL " + e.getSQL(), e.getSQLException());
            throw (new GobiiDaoException(e.getSQLException()));

        } catch (Exception e) {

            LOGGER.error("Error retrieving dto list ", e);
            throw (new GobiiDaoException(e));

        }

        return returnVal;

    } // getDtoList()

    @Transactional(propagation = Propagation.REQUIRED)
    public ResultSet getResultSet(Map<String, Object> jdbcParameters, Map<String, Object> sqlParameters) throws GobiiException {

        ResultSet returnVal;

        try {

            ResultSetFromSql resultSetFromSql = new ResultSetFromSql(listStatement, jdbcParameters,sqlParameters);
            this.storedProcExec.doWithConnection(resultSetFromSql);
            returnVal = resultSetFromSql.getResultSet();

        }catch(SQLGrammarException e) {
            LOGGER.error("Error retrieving dto list with SQL " + e.getSQL(), e.getSQLException());
            throw (new GobiiDaoException(e.getSQLException()));

        } catch (Exception e) {

            LOGGER.error("Error retrieving dto list ", e);
            throw (new GobiiDaoException(e));

        }


        return returnVal;

    } // getDtoList()

}
