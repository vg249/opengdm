package org.gobiiproject.gobiidao.resultset.sqlworkers;

import org.hibernate.jdbc.Work;

import java.sql.*;
import java.util.Map;

/**
 * Created by Phil on 4/7/2016.
 */
public class SpInsProject implements Work {

    private Map<String,Object> parameters = null;
    public SpInsProject(Map<String,Object> parameters ) {
        this.parameters = parameters;
    }


    public Integer projectId = null;
    public Integer getProjectId() {
        return projectId;
    }

    @Override
    public void execute(Connection dbConnection) throws SQLException {

        CallableStatement callableStatement = dbConnection.prepareCall("{call createproject(?,?,?,?,?,?,?,?,?)}");

        callableStatement.setString(1,(String) parameters.get("projectName"));
        callableStatement.setString(2,(String) parameters.get("projectCode"));
        callableStatement.setString(3,(String) parameters.get("projectDescription"));
        callableStatement.setInt(4,(Integer) parameters.get("piContact"));
        callableStatement.setInt(5,(Integer) parameters.get("createdBy"));
        callableStatement.setDate(6,(Date) parameters.get("createdDate"));
        callableStatement.setInt(7,(Integer) parameters.get("modifiedby"));
        callableStatement.setDate(8,(Date) parameters.get("modifiedDate"));
        callableStatement.setInt(9,(Integer) parameters.get("projectStatus"));

        callableStatement.registerOutParameter(10, Types.INTEGER);

        projectId = callableStatement.executeUpdate();

    } // execute()
}
