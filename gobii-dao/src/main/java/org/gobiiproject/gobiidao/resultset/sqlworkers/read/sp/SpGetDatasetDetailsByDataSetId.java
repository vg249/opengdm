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
public class SpGetDatasetDetailsByDataSetId implements Work {

    private Map<String, Object> parameters = null;

    public SpGetDatasetDetailsByDataSetId(Map<String, Object> parameters) {
        this.parameters = parameters;
    }


    private ResultSet resultSet = null;

    public ResultSet getResultSet() {
        return resultSet;
    }

    @Override
    public void execute(Connection dbConnection) throws SQLException {

        String sql = "select ds.*, " +
                "j.status \"jobstatusid\"," +
                "case " +
                "when j.status is not null " +
                "then (select cv.term from cv where cvgroup_id = (select cvgroup_id from cvgroup where name='job_status' and type=1) and cv.cv_id=j.status) " +
                "else 'Unsubmitted' " +
                "end " +
                "as jobstatusname, " +
                "j.submitted_date as jobsubmitteddate " +
                "from dataset ds " +
                "left outer join job j on (ds.job_id=j.job_id) " +
                "where dataset_id=? " +
                "order by lower(ds.name)";

        PreparedStatement preparedStatement = dbConnection.prepareCall(sql);
        preparedStatement.setInt(1, (Integer) parameters.get("dataSetId"));
        resultSet = preparedStatement.executeQuery();
    } // execute()
}
