package org.gobiiproject.gobiidao.resultset.spworkers;

import org.hibernate.jdbc.Work;

import java.sql.*;
import java.util.Map;

/**
 * Created by Phil on 4/7/2016.
 */
public class SpGetContactNamesByRoleName implements Work {

    private Map<String,Object> parameters = null;
    public SpGetContactNamesByRoleName(Map<String,Object> parameters ) {
        this.parameters = parameters;
    }


    private ResultSet resultSet = null;

    public ResultSet getResultSet() {
        return resultSet;
    }

    @Override
    public void execute(Connection dbConnection) throws SQLException {
        String getDBUSERCursorSql = "{call get_contact_names_by_type(?,?)}";
        CallableStatement callableStatement = dbConnection.prepareCall(getDBUSERCursorSql);
        callableStatement.setString(1, (String) parameters.get("roleName"));
        callableStatement.registerOutParameter(2, Types.OTHER);
        callableStatement.executeUpdate();
        resultSet = (ResultSet) callableStatement.getObject(2);
    } // execute()
}
