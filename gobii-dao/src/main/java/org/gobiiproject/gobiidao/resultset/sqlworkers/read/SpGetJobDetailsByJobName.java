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
public class SpGetJobDetailsByJobName implements Work {

    private Map<String, Object> parameters = null;

    public SpGetJobDetailsByJobName(Map<String, Object> parameters) { this.parameters = parameters; }

    private ResultSet resultSet = null;

    public ResultSet getResultSet() { return resultSet; }

    @Override
    public void execute(Connection dbConnection) throws SQLException {

        String sql = "select job_id\n" +
                "job_id,\n" +
                "type.term as type_id,\n" +
                "payloadtype.term as payload_type_id,\n" +
                "status.term as status,\n" +
                "j.message,\n" +
                "j.submitted_by,\n" +
                "j.submitted_date,\n" +
                "j.name\n"+
                "from job j,\n" +
                "cv type,\n" +
                "cv payloadtype,\n" +
                "cv status\n" +
                "where j.type_id = type.cv_id\n" +
                "and j.payload_type_id = payloadtype.cv_id\n" +
                "and j.status = status.cv_id\n" +
                "and j.name = ?";
        PreparedStatement preparedStatement = dbConnection.prepareStatement(sql);
        String jobName = (String) parameters.get("jobName");
        preparedStatement.setString(1, jobName);
        resultSet = preparedStatement.executeQuery();

    }

}
