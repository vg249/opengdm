package org.gobiiproject.gobiidao.resultset.sqlworkers.read.sp;

import org.hibernate.jdbc.Work;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by Phil on 4/7/2016.
 */
public class SpGetPlatformNames implements Work {

    private Map<String, Object> parameters = null;

    public SpGetPlatformNames() {
    }


    private ResultSet resultSet = null;

    public ResultSet getResultSet() {
        return resultSet;
    }

    @Override

    public void execute(Connection dbConnection) throws SQLException {

        String sql = "select platform_id, name from platform order by lower(name)";

        PreparedStatement preparedStatement = dbConnection.prepareStatement(sql);

        resultSet = preparedStatement.executeQuery();

    } // execute()
}
