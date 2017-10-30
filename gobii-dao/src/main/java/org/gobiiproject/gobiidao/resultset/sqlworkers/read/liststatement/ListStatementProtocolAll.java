package org.gobiiproject.gobiidao.resultset.sqlworkers.read.liststatement;

import org.gobiiproject.gobiidao.resultset.core.listquery.ListSqlId;
import org.gobiiproject.gobiidao.resultset.core.listquery.ListStatement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

import static org.gobiiproject.gobiidao.resultset.core.listquery.ListSqlId.QUERY_ID_DATASET_ALL;

/**

 */
public class ListStatementProtocolAll implements ListStatement {



//    @Override
//    public void execute(Connection dbConnection) throws SQLException {
//
//        String sql = "select cvgroup_id,name,definition,type from cvgroup where cvgroup_id=?";
//
//        PreparedStatement preparedStatement = dbConnection.prepareStatement(sql);
//        Integer groupId = (Integer) parameters.get("groupId");
//
//        preparedStatement.setInt(1, groupId);
//
//        resultSet = preparedStatement.executeQuery();
//
//    } // execute()

    @Override
    public ListSqlId getListSqlId() {
        return QUERY_ID_DATASET_ALL;
    }

    @Override
    public PreparedStatement makePreparedStatement(Connection dbConnection,Map<String, Object> jdbcParamVals) throws SQLException {

        String sql = "select * from protocol order by lower(name)";

        PreparedStatement returnVal = dbConnection.prepareStatement(sql);

        return returnVal;
    }
}
