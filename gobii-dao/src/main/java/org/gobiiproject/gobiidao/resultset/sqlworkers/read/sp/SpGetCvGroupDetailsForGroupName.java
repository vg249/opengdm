package org.gobiiproject.gobiidao.resultset.sqlworkers.read.sp;

import org.hibernate.jdbc.Work;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by VCalaminos on 1/17/2018.
 */
public class SpGetCvGroupDetailsForGroupName implements Work {

    private Map<String, Object> parameters = null;

    public SpGetCvGroupDetailsForGroupName(Map<String, Object> parameters) { this.parameters = parameters; }

    private ResultSet resultSet = null;

    public ResultSet getResultSet() { return resultSet; }

    @Override
    public void execute(Connection dbConnection) throws SQLException {

        String sql = "select" +
                "\n cvgroup_id," +
                "\n name," +
                "\n definition," +
                "\n type" +
                "\n from cvgroup" +
                "\n where name = ? and type = ?";

        PreparedStatement preparedStatement = dbConnection.prepareStatement(sql);
        String groupName = (String) parameters.get("groupName");
        Integer cvGroupTypeId = (Integer) parameters.get("cvGroupTypeId");

        preparedStatement.setString(1, groupName);
        preparedStatement.setInt(2, cvGroupTypeId);

        resultSet = preparedStatement.executeQuery();

    }

}
