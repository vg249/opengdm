package org.gobiiproject.gobiidao.resultset.sqlworkers.read.sp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import org.hibernate.jdbc.Work;

/**
 * Created by Phil on 4/29/2016.
 */
public class SpGetCvDetailsByCvId implements Work {
    /**
     * Created by Phil on 4/29/2016.
     */
    private Map<String, Object> parameters = null;

    public SpGetCvDetailsByCvId(Map<String, Object> parameters) {
        this.parameters = parameters;
    }


    private ResultSet resultSet = null;

    public ResultSet getResultSet() {
        return resultSet;
    }

    @Override
    public void execute(Connection dbConnection) throws SQLException {

        String Sql = "select *, g.type as group_type from cv c, cvgroup g where c.cvgroup_id = g.cvgroup_id and cv_id = ?";
        PreparedStatement preparedStatement = dbConnection.prepareStatement(Sql);
        Integer projectId = (Integer) parameters.get("cvId");
        preparedStatement.setInt(1, projectId);
        resultSet = preparedStatement.executeQuery();
    } // execute()

}
