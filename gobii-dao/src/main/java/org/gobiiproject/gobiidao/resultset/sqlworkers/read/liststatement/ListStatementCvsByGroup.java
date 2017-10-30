package org.gobiiproject.gobiidao.resultset.sqlworkers.read.liststatement;

import org.gobiiproject.gobiidao.resultset.core.listquery.ListSqlId;
import org.gobiiproject.gobiidao.resultset.core.listquery.ListStatement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

import static org.gobiiproject.gobiidao.resultset.core.listquery.ListSqlId.QUERY_ID_CVS_BY_GROUP;

/**

 */
public class ListStatementCvsByGroup implements ListStatement {

    @Override
    public ListSqlId getListSqlId() {
        return QUERY_ID_CVS_BY_GROUP;
    }

    @Override
    public PreparedStatement makePreparedStatement(Connection dbConnection, Map<String, Object> jdbcParamVals, Map<String, Object> sqlParamVals) throws SQLException {

        String Sql = "select cv.*, g.type as group_type from cv join cvgroup g on (cv.cvgroup_id=g.cvgroup_id) where lower(g.name)= ? order by lower(term)";
        PreparedStatement returnVal = dbConnection.prepareStatement(Sql);
        String groupName = (String) jdbcParamVals.get("groupName");
        returnVal.setString(1, groupName.toLowerCase());

        return returnVal;
    }
}
