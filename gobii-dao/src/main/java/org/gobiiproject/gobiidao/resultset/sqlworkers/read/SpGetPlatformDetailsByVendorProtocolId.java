package org.gobiiproject.gobiidao.resultset.sqlworkers.read;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import org.hibernate.jdbc.Work;

/**
 * Created by Angel on 4/19/2016.
 */
public class SpGetPlatformDetailsByVendorProtocolId implements Work {

    private Map<String,Object> parameters = null;
    public SpGetPlatformDetailsByVendorProtocolId(Map<String,Object> parameters ) {
        this.parameters = parameters;
    }


    private ResultSet resultSet = null;

    public ResultSet getResultSet() {
        return resultSet;
    }

    @Override
    public void execute(Connection dbConnection) throws SQLException {

        String sql =  "select pl.platform_id,\n" +
                "pl.name,\n" +
                "pl.code,\n" +
                "pl.description,\n" +
                "pl.created_by,\n" +
                "pl.created_date,\n" +
                "pl.modified_by,\n" +
                "pl.modified_date,\n" +
                "pl.status,\n" +
                "pl.type_id\n" +
                "from platform pl,\n" +
                "vendor_protocol vp,\n"+
                "protocol p \n"+
                "where pl.platform_id=p.platform_id \n" +
                "and p.protocol_id = vp.protocol_id \n" +
                "and vp.vendor_protocol_id = ?";

        PreparedStatement preparedStatement = dbConnection.prepareCall(sql);
        preparedStatement.setInt(1, (Integer) parameters.get("vendorProtocolId"));
        resultSet = preparedStatement.executeQuery();

    } // execute()
}
