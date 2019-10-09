package org.gobiiproject.gobiidao.resultset.sqlworkers.read.sp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import org.hibernate.jdbc.Work;

/**
 * Created by VCalaminos on 2/7/2017.
 */
public class SpGetDataSetsByTypeId implements Work {

    private Map<String, Object> parameters = null;

    public SpGetDataSetsByTypeId(Map<String, Object> parameters) { this.parameters = parameters; }

    private ResultSet resultSet = null;

    public ResultSet getResultSet() { return  resultSet; }

    @Override
    public void execute(Connection dbConnection) throws SQLException {

        String sql = "select *\n" +
                "from dataset where type_id = ?";
        PreparedStatement preparedStatement = dbConnection.prepareStatement(sql);

        Integer typeId = (Integer) parameters.get("typeId");
        preparedStatement.setInt(1, typeId);

        resultSet = preparedStatement.executeQuery();

    }// execute ()
}
