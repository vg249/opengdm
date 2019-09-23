package org.gobiiproject.gobiidao.resultset.sqlworkers.read.sp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.hibernate.jdbc.Work;

/**
 * Created by VCalaminos on 2017-01-13.
 */
public class SpGetDatasetTypeNames implements Work {

    public SpGetDatasetTypeNames() {
    }

    private ResultSet resultSet = null;

    public ResultSet getResultSet() { return resultSet; }

    @Override
    public void execute(Connection dbConnection) throws SQLException {

        String sql = "select * from getCvtermsByCvgroupname('dataset_type')";

        PreparedStatement preparedStatement = dbConnection.prepareStatement(sql);

        resultSet = preparedStatement.executeQuery();

    } // execute()


}
