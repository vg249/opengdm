package org.gobiiproject.gobiidao.resultset.sqlworkers.read.sp;

import org.hibernate.jdbc.Work;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by VCalaminos on 3/13/2018.
 */
public class SpGetProjectsForLoadedDatasets implements Work {

    public SpGetProjectsForLoadedDatasets() {
    }

    private ResultSet resultSet = null;

    public ResultSet getResultSet() { return resultSet; }

    @Override
    public void execute(Connection dbConnection) throws SQLException {

        String sql = "select\n" +
                "p.*\n" +
                "from\n" +
                "project p,\n" +
                "experiment e,\n" +
                "dataset d\n" +
                "where\n" +
                "p.project_id = e.project_id\n" +
                "and e.experiment_id = d.experiment_id\n" +
                "and d.job_id is not null";

        PreparedStatement preparedStatement = dbConnection.prepareStatement(sql);

        resultSet = preparedStatement.executeQuery();

    } // execute()

}
