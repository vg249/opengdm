package org.gobiiproject.gobiidao.resultset.core.listquery;

import org.apache.commons.lang.StringUtils;
import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.resultset.core.ResultColumnApplicator;
import org.gobiiproject.gobiidao.resultset.core.StoredProcExec;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.system.PagedList;
import org.hibernate.exception.SQLGrammarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Phil on 10/25/2016.
 */
public class DtoListQuery<T> {

    Logger LOGGER = LoggerFactory.getLogger(DtoListQuery.class);

    private ListStatement listStatement;
    private ListStatementPaged listStatementPaged;
    private Class<T> dtoType;
    private StoredProcExec storedProcExec;

    public DtoListQuery(StoredProcExec storedProcExec,
                        Class<T> dtoType,
                        ListStatement listStatement,
                        ListStatementPaged listStatementPaged) {

        this.storedProcExec = storedProcExec;
        this.dtoType = dtoType;
        this.listStatement = listStatement;
        this.listStatementPaged = listStatementPaged;
    }


    private List<T> makeDtoListFromResultSet(ResultSet resultSet) throws IllegalArgumentException,
            InstantiationException,
            SQLException {

        List<T> returnVal = new ArrayList<>();

        while (resultSet.next()) {
            try {
                T dto = dtoType.newInstance();
                ResultColumnApplicator.applyColumnValues(resultSet, dto);
                returnVal.add(dto);
            } catch (IllegalAccessException e) {
                throw new SQLException(e);
            } catch (InstantiationException e) {
                throw new SQLException(e);
            }
        }

        return returnVal;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<T> getDtoList(Map<String, Object> jdbcParameters, Map<String, Object> sqlParameters) throws GobiiException {

        List<T> returnVal;

        try {

            ResultSetFromSql dtoListFromSql = new ResultSetFromSql(listStatement, jdbcParameters, sqlParameters);
            this.storedProcExec.doWithConnection(dtoListFromSql);
            ResultSet resultSet = dtoListFromSql.getResultSet();
            returnVal = this.makeDtoListFromResultSet(resultSet);

        } catch (SQLGrammarException e) {
            LOGGER.error("Error retrieving dto list with SQL " + e.getSQL(), e.getSQLException());
            throw (new GobiiDaoException(e.getSQLException()));

        } catch (Exception e) {

            LOGGER.error("Error retrieving dto list ", e);
            throw (new GobiiDaoException(e));

        }

        return returnVal;

    } // getDtoList()

    @Transactional(propagation = Propagation.REQUIRED)
    public PagedList<T> getDtoListPaged(Integer pageSize, Integer pageNo, String pgQueryIdFromUser) throws GobiiException {

        PagedList<T> returnVal;

        try {

            // ideally, all query types will have a paged implementation
            if (listStatementPaged == null) {
                throw new GobiiException("There is no paged query support for query " + listStatement.getListSqlId());
            }

            String pgQueryId;
            if(StringUtils.isNotEmpty(pgQueryIdFromUser)) {
                pgQueryId = pgQueryIdFromUser;
            } else {
                pgQueryId = UUID.randomUUID().toString();
            }

            ResultSetFromSqlPaged resultSetFromSqlPaged = new ResultSetFromSqlPaged(listStatementPaged, pageSize, pageNo, pgQueryId);
            this.storedProcExec.doWithConnection(resultSetFromSqlPaged);
            ResultSet resultSet = resultSetFromSqlPaged.getResultSet();
            List<T> dtoList =  this.makeDtoListFromResultSet(resultSet);

            returnVal = new PagedList<>(
                    resultSetFromSqlPaged.getPageFrameState().getCreated(),
                    dtoList,
                    pageSize,
                    resultSetFromSqlPaged.getPageNo(),
                    resultSetFromSqlPaged.getPageFrameState().getPages().size(),
                    pgQueryId
            );

        } catch (SQLGrammarException e) {
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

            ResultSetFromSql resultSetFromSql = new ResultSetFromSql(listStatement, jdbcParameters, sqlParameters);
            this.storedProcExec.doWithConnection(resultSetFromSql);
            returnVal = resultSetFromSql.getResultSet();

        } catch (SQLGrammarException e) {
            LOGGER.error("Error retrieving dto list with SQL " + e.getSQL(), e.getSQLException());
            throw (new GobiiDaoException(e.getSQLException()));

        } catch (Exception e) {

            LOGGER.error("Error retrieving dto list ", e);
            throw (new GobiiDaoException(e));

        }


        return returnVal;

    } // getDtoList()


}
