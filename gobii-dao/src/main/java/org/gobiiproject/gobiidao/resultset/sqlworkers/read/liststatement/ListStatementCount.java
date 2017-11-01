package org.gobiiproject.gobiidao.resultset.sqlworkers.read.liststatement;

import org.gobiiproject.gobiidao.resultset.core.listquery.ListSqlId;
import org.gobiiproject.gobiidao.resultset.core.listquery.ListStatement;
import org.gobiiproject.gobiidao.resultset.core.listquery.ParameterizedSql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static org.gobiiproject.gobiidao.resultset.core.listquery.ListSqlId.QUERY_ID_LAST_MODIFIED;

/**

 */
public class ListStatementCount implements ListStatement {

    private final String PARAM_NAME_TABLE = "tableName";
    private final String PARAM_NAME_TABLE_DELIMITED = ParameterizedSql.makeDelimitedParamName(PARAM_NAME_TABLE);


    @Override
    public ListSqlId getListSqlId() {
        return QUERY_ID_LAST_MODIFIED;
    }

    @Override
    public PreparedStatement makePreparedStatement(Connection dbConnection, Map<String, Object> jdbcParamVals, Map<String, Object> sqlParamVals) throws SQLException {

        ParameterizedSql parameterizedSql =
                new ParameterizedSql("select count(*) as count from "
                        + PARAM_NAME_TABLE_DELIMITED,
                        new HashMap<String, String>() {
                            {
                                put(PARAM_NAME_TABLE_DELIMITED, null);

                            }
                        });

        String sql = parameterizedSql
                .setParamValue(PARAM_NAME_TABLE_DELIMITED,sqlParamVals.get(PARAM_NAME_TABLE).toString())
                .getSql();

            PreparedStatement returnVal = dbConnection.prepareStatement(sql);

        return returnVal;
    }
}
