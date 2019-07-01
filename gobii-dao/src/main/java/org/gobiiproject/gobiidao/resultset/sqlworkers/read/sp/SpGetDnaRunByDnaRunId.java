package org.gobiiproject.gobiidao.resultset.sqlworkers.read.sp;

import org.hibernate.jdbc.Work;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by VCalaminos on 6/25/2019.
 */
public class SpGetDnaRunByDnaRunId implements Work {

    private Map<String, Object> parameters = null;

    public SpGetDnaRunByDnaRunId(Map<String, Object> parameters) { this.parameters = parameters; }

    private ResultSet resultSet = null;

    public ResultSet getResultSet() { return resultSet; }

    @Override
    public void execute(Connection dbConnection) throws SQLException {

        String sql = "with dnarun as(\n" +
                "SELECT\n" +
                    "dr.dnarun_id,\n" +
                    "dr.experiment_id,\n" +
                    "dr.dnasample_id,\n" +
                    "array_agg(datasetids) as dataset_ids,\n" +
                    "dr.name,\n" +
                    "dr.code\n," +
                    "dr.dataset_dnarun_idx\n" +
                "FROM\n" +
                "(\n" +
                    "SELECT\n" +
                        "dr.dnarun_id,\n" +
                        "dr.experiment_id,\n" +
                        "dr.dnasample_id,\n" +
                        "dr.name,\n" +
                        "dr.code,\n" +
                        "jsonb_object_keys(dr.dataset_dnarun_idx)::integer as datasetids\n," +
                        "dr.dataset_dnarun_idx\n" +
                    "FROM\n" +
                        "dnarun dr\n" +
                    "WHERE dr.dnarun_id=?\n" +
                ") as dr\n" +
                "GROUP BY dr.dnarun_id, dr.experiment_id, dr.dnasample_id, dr.name, dr.code, dr.dataset_dnarun_idx)\n" +
                "SELECT\n" +
                "   dnarun.*,\n" +
                "   s.name as sample_name,\n" +
                "   g.germplasm_id as germplasm_id,\n" +
                "   g.name as germplasm_name,\n" +
                "   g.external_code as germplasm_external_code\n" +
                "FROM\n" +
                    "dnarun, dnasample s, germplasm g\n" +
                "WHERE\n" +
                    "dnarun.dnasample_id = s.dnasample_id\n" +
                    "and g.germplasm_id = s.germplasm_id";

        PreparedStatement preparedStatement = dbConnection.prepareStatement(sql);
        Integer dnaRunId = (Integer) parameters.get("dnaRunId");
        preparedStatement.setInt(1, dnaRunId);
        resultSet = preparedStatement.executeQuery();
    }
}
