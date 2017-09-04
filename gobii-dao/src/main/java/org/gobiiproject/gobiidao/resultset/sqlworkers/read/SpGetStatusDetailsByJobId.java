package org.gobiiproject.gobiidao.resultset.sqlworkers.read;

import org.hibernate.jdbc.Work;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by VCalaminos on 9/4/2017.
 */
public class SpGetStatusDetailsByJobId implements Work {

    private Map<String, Object> parameters = null;

    public SpGetStatusDetailsByJobId(Map<String, Object> parameters) { this.parameters = parameters; }

    private ResultSet resultSet = null;

    public ResultSet getResultSet() { return resultSet; }

    @Override
    public void execute(Connection dbConnection) throws SQLException {

        String sql = "select *\n" +
                "from job\n" +
                "where job_id = ?";
        PreparedStatement preparedStatement = dbConnection.prepareStatement(sql);
        Integer jobId = (Integer) parameters.get("jobId");
        preparedStatement.setInt(1, jobId);
        resultSet = preparedStatement.executeQuery();

    }

}
