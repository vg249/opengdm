package org.gobiiproject.gobiidao.resultset.sqlworkers.read.sp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.hibernate.jdbc.Work;

/**
 * Created by Phil on 4/7/2016.
 * Edited by Angel on 4/14/2016.
 */
public class SpGetTableDisplayNames implements Work {

    public SpGetTableDisplayNames() {
    }

    private ResultSet resultSet = null;

    public ResultSet getResultSet() {
        return resultSet;
    }

    @Override
    public void execute(Connection dbConnection) throws SQLException {

        String sql = "select display_id, " +
                "table_name,\n" +
                "display_name,\n" +
                "rank,\n" +
                "column_name \n" +
                "from display \n" +
                " order by lower(table_name), lower(column_name), rank";

        PreparedStatement preparedStatement = dbConnection.prepareStatement(sql);
//        String tableName = parameters.get("tableName").toString().toLowerCase();
//        preparedStatement.setString(1, tableName);
        resultSet = preparedStatement.executeQuery();

    } // execute()
}
