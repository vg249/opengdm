package org.gobiiproject.gobiidao.resultset.sqlworkers.read.sp;

import org.hibernate.jdbc.Work;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by Angel on 4/19/2016.
 */
public class SpGetJobDetailsByDataSetId implements Work {

    private Map<String,Object> parameters = null;
    public SpGetJobDetailsByDataSetId(Map<String,Object> parameters ) {
        this.parameters = parameters;
    }


    private ResultSet resultSet = null;

    public ResultSet getResultSet() {
        return resultSet;
    }

    @Override
    public void execute(Connection dbConnection) throws SQLException {

        String sql = "select" +
                " j.job_id, " +
                " type.term as type_id," +
                " payloadtype.term as payload_type_id," +
                " status.term as status," +
                " j.message," +
                " j.submitted_by," +
                " j.submitted_date," +
                " j.name," +
                " d.dataset_id" +
                " from" +
                " dataset d," +
                " job j," +
                " cv type," +
                " cv payloadtype," +
                " cv status" +
                " where" +
                " j.type_id = type.cv_id" +
                " and j.payload_type_id = payloadtype.cv_id" +
                " and j.status = status.cv_id" +
                " and d.job_id = j.job_id" +
                " and d.dataset_id = ?";

        PreparedStatement preparedStatement = dbConnection.prepareCall(sql);
        preparedStatement.setInt(1, (Integer) parameters.get("dataSetId"));
        resultSet = preparedStatement.executeQuery();
    } // execute()
}
