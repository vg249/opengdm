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
public class SpGetMarkersByMarkerName implements Work {

    private Map<String,Object> parameters = null;
    public SpGetMarkersByMarkerName(Map<String,Object> parameters ) {
        this.parameters = parameters;
    }


    private ResultSet resultSet = null;

    public ResultSet getResultSet() {
        return resultSet;
    }

    @Override
    public void execute(Connection dbConnection) throws SQLException {

        String sql = "select m.marker_id,\n" +
                "p.platform_id,\n" +
                "m.variant_id, \n" +
                "m.name \"marker_name\", \n" +
                "m.code, \n" +
                "m.ref, \n" +
                "m.alts, \n" +
                "m.sequence, \n" +
                "m.reference_id, \n" +
                "m.strand_id, \n" +
                "m.status, \n" +
                "p.name \"platform_name\"\n" +
                "from marker m\n" +
                "join platform p on (m.platform_id=p.platform_id)\n" +
                "where m.name = ? \n";

        PreparedStatement preparedStatement = dbConnection.prepareCall(sql);
        preparedStatement.setString(1, (String) parameters.get("markerName"));
        resultSet = preparedStatement.executeQuery();
    } // execute()
}
