package org.gobiiproject.gobiidao.resultset.sqlworkers.read;

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

    private Map<String,Object> parameters = null;
    public SpGetDatasetDetailsByDataSetId(Map<String,Object> parameters ) {
        this.parameters = parameters;
    }


    private ResultSet resultSet = null;

    public ResultSet getResultSet() {
        return resultSet;
    }

    @Override
    public void execute(Connection dbConnection) throws SQLException {

        String sql = "select\n" +
                "\tds.dataset_id,\n" +
                "\tds.experiment_id,\n" +
                "\tds.callinganalysis_id,\n" +
                "\tds.analyses,\n" +
                "\tds.data_table,\n" +
                "\tds.data_file,\n" +
                "\tds.quality_table,\n" +
                "\tds.quality_file,\n" +
//                "\tds.scores,\n" +
                "\tds.created_by,\n" +
                "\tds.created_date,\n" +
                "\tds.modified_by,\n" +
                "\tds.modified_date,\n" +
                "\tds.status,\n" +
                "\ta.analysis_id,\n" +
                "\ta.\"name\",\n" +
                "\ta.\"name\",\n" +
                "\ta.description,\n" +
                "\ta.type_id,\n" +
                "\ta.program,\n" +
                "\ta.programversion,\n" +
                "\ta.algorithm,\n" +
                "\ta.sourcename,\n" +
                "\ta.sourceversion,\n" +
                "\ta.sourceuri,\n" +
                "\ta.reference_id,\n" +
                "\ta.status,\n" +
                "\ta.\"name\",\n" +
                "\ta.timeexecuted\n" +
                "from dataset ds\n" +
                "left outer join analysis a on (a.analysis_id=ds.callinganalysis_id) " +
                "where dataset_id=?";

        PreparedStatement preparedStatement = dbConnection.prepareCall(sql);
        preparedStatement.setInt(1, (Integer) parameters.get("dataSetId"));
        resultSet = preparedStatement.executeQuery();
    } // execute()
}
