package org.gobiiproject.gobiidao.resultset.spworkers;

import org.hibernate.jdbc.Work;

import java.sql.*;
import java.util.Map;

/**
 * Created by Phil on 4/7/2016.
 */
public class SpGetProjecttNamesByContactId implements Work {

    private Map<String,Object> parameters = null;
    public SpGetProjecttNamesByContactId(Map<String,Object> parameters ) {
        this.parameters = parameters;
    }


    private ResultSet resultSet = null;

    public ResultSet getResultSet() {
        return resultSet;
    }

    @Override
    public void execute(Connection dbConnection) throws SQLException {
        String getDBUSERCursorSql = "{call get_project_names_by_pi(?,?)}";
        CallableStatement callableStatement = dbConnection.prepareCall(getDBUSERCursorSql);
        callableStatement.setInt(1, (Integer) parameters.get("contactId"));
        callableStatement.registerOutParameter(2, Types.OTHER);
        callableStatement.executeUpdate();
        resultSet = (ResultSet) callableStatement.getObject(2);
    } // execute()
}
