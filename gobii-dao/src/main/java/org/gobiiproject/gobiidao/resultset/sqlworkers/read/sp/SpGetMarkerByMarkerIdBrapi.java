package org.gobiiproject.gobiidao.resultset.sqlworkers.read.sp;

import org.hibernate.jdbc.Work;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by VCalaminos on 7/7/2019.
 */
public class SpGetMarkerByMarkerIdBrapi implements Work {

    private Map<String, Object> parameters = null;

    public SpGetMarkerByMarkerIdBrapi(Map<String, Object> parameters) { this.parameters = parameters; }

    private ResultSet resultSet = null;

    public ResultSet getResultSet() { return resultSet; }

    @Override
    public void execute(Connection dbConnection) throws SQLException {

        String sql = "with marker as (\n" +
                "SELECT \n" +
                    "mr.marker_id,\n" +
                    "mr.platform_id,\n" +
                    "mr.variant_id,\n" +
                    "mr.name,\n" +
                    "mr.code,\n" +
                    "mr.reference_id,\n" +
                    "array_agg(datasetids) as dataset_ids,\n" +
                    "mr.dataset_marker_idx \n" +
                "FROM\n" +
                    "marker mr\n" +
                "left join\n" +
                    "(\n" +
                    "SELECT \n" +
                    "mr.marker_id, \n" +
                    "jsonb_object_keys(mr.dataset_marker_idx)::integer as datasetids\n" +
                    "FROM \n" +
                    "marker mr\n" +
                ") as mr2 \n" +
                "on mr.marker_id = mr2.marker_id\n" +
                "where mr.marker_id=?\n" +
                "GROUP BY mr.marker_id, mr.platform_id, mr.variant_id, mr.name, mr.code, mr.reference_id, mr.dataset_marker_idx\n" +
                ")\n" +
                "SELECT \n" +
                "marker.*, \n" +
                "r.name as reference_name\n" +
                "FROM \n" +
                "marker LEFT OUTER JOIN reference r\n" +
                "USING(reference_id) ";

        PreparedStatement preparedStatement = dbConnection.prepareStatement(sql);
        Integer markerId = (Integer) parameters.get("markerId");
        preparedStatement.setInt(1, markerId);
        resultSet = preparedStatement.executeQuery();
    }

}
