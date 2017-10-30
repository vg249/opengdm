package org.gobiiproject.gobiidao.resultset.sqlworkers.read.liststatement;

import org.gobiiproject.gobiidao.resultset.core.listquery.ListSqlId;
import org.gobiiproject.gobiidao.resultset.core.listquery.ListStatement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

import static org.gobiiproject.gobiidao.resultset.core.listquery.ListSqlId.QUERY_ID_DATASET_ALL;
import static org.gobiiproject.gobiidao.resultset.core.listquery.ListSqlId.QUERY_ID_LAST_MODIFIED;

/**

 */
public class ListStatementLastModified implements ListStatement {

    @Override
    public ListSqlId getListSqlId() {
        return QUERY_ID_LAST_MODIFIED;
    }

    @Override
    public PreparedStatement makePreparedStatement(Connection dbConnection,Map<String, Object> jdbcParamVals) throws SQLException {

        String sql = "select * from protocol order by lower(name)";

        PreparedStatement returnVal = dbConnection.prepareStatement(sql);

        return returnVal;
    }
}
