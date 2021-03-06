package org.gobiiproject.gobiidao.resultset.sqlworkers.read.sp;

import org.hibernate.jdbc.Work;

import java.sql.*;
import java.util.Map;

/**
 * Created by Angel on 4/26/2016.
 */
public class SpGetCvTermsByGroup implements Work {
    /**
     * Created by Angel on 4/26/2016.
     */
    private Map<String, Object> parameters = null;

    public SpGetCvTermsByGroup(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    private ResultSet resultSet = null;

    public ResultSet getResultSet() {
        return resultSet;
    }

    @Override
    public void execute(Connection dbConnection) throws SQLException {

        String Sql = "select cv_id, term, g.type as group_type from cv join cvgroup g on (cv.cvgroup_id=g.cvgroup_id) where g.name= ? order by lower(term)";
        PreparedStatement preparedStatement = dbConnection.prepareStatement(Sql);
        String groupName = (String) parameters.get("groupName");
        preparedStatement.setString(1, groupName);
        
        resultSet = preparedStatement.executeQuery();
    } // execute()

}
