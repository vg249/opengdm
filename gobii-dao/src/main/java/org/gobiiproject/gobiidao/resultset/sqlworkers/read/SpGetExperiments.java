package org.gobiiproject.gobiidao.resultset.sqlworkers.read;

import org.hibernate.jdbc.Work;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by Phil on 4/7/2016.
 */
public class SpGetExperiments implements Work {

    private Map<String, Object> parameters = null;

    public SpGetExperiments() {
    }


    private ResultSet resultSet = null;

    public ResultSet getResultSet() {
        return resultSet;
    }

    @Override

    public void execute(Connection dbConnection) throws SQLException {

        String sql = "select p.name \"platform_name\",e.*\n" +
                "from experiment e\n" +
                "join platform p on (e.platform_id=p.platform_id)\n" +
                "order by lower(e.name)";

        PreparedStatement preparedStatement = dbConnection.prepareStatement(sql);

        resultSet = preparedStatement.executeQuery();

    } // execute()
}
