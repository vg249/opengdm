package org.gobiiproject.gobiidao.resultset.sqlworkers.read.liststatement.discrete;

import org.gobiiproject.gobiidao.resultset.core.listquery.ListSqlId;
import org.gobiiproject.gobiidao.resultset.core.listquery.ListStatement;
import org.gobiiproject.gobiidao.resultset.core.listquery.ParameterizedSql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by VCalaminos on 11/5/2018.
 */
public class ListStatementDnaSampleNamesAll implements ListStatement {

    private final String PARAM_NAME_CALL_LIMIT = "callLimit";

    @Override
    public ListSqlId getListSqlId() { return ListSqlId.QUERY_ID_DNASAMPLE_NAMES_ALL; }

    @Override
    public PreparedStatement makePreparedStatement(Connection dbConnection, Map<String, Object> jdbcParamVals, Map<String, Object> sqlParamVals) throws SQLException {


        ParameterizedSql parameterizedSql =
                new ParameterizedSql("select dnsample_id, name " +
                        "from dnasample " +
                        "limit ?",
                        new HashMap<String, String>(){}
                );

        String sql = parameterizedSql
                .getSql();

        PreparedStatement returnVal = dbConnection.prepareStatement(sql);

        Integer callLimit = (Integer) jdbcParamVals.get(PARAM_NAME_CALL_LIMIT);
        returnVal.setInt(1, callLimit);

        return returnVal;

    }

}
