package org.gobiiproject.gobiidao.resultset.sqlworkers.read.sp;

import org.hibernate.jdbc.Work;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by Angel on 4/26/2016.
 */
public class SpGetCvGroupsByTypeId implements Work {

    private Map<String, Object> parameters = null;

    public SpGetCvGroupsByTypeId(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    private ResultSet resultSet = null;

    public ResultSet getResultSet() {
        return resultSet;
    }

    @Override
    public void execute(Connection dbConnection) throws SQLException {

        String sql = "select cvgroup_id,name,definition,type\n" +
                "from cvgroup \n" +
                "where type = ?";

        PreparedStatement preparedStatement = dbConnection.prepareStatement(sql);
        Integer groupType = (Integer) parameters.get("groupType");
        preparedStatement.setInt(1, groupType);
        resultSet = preparedStatement.executeQuery();

    } // execute()
}
