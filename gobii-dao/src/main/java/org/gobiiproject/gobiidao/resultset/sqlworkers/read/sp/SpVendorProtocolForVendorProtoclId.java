package org.gobiiproject.gobiidao.resultset.sqlworkers.read.sp;

import org.hibernate.jdbc.Work;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by Phil on 4/11/2016.
 */
public class SpVendorProtocolForVendorProtoclId implements Work {
    /**
     * Created by Phil on 4/7/2016.
     */
    private Map<String, Object> parameters = null;

    public SpVendorProtocolForVendorProtoclId(Map<String, Object> parameters) {
        this.parameters = parameters;
    }


    private ResultSet resultSet = null;

    public ResultSet getResultSet() {
        return resultSet;
    }

    @Override
    public void execute(Connection dbConnection) throws SQLException {

        String sql = "select * from vendor_protocol where vendor_protocol_id = ?;";

        PreparedStatement preparedStatement = dbConnection.prepareStatement(sql);
        Integer organizationId = (Integer) parameters.get("vendorProtocolId");

        preparedStatement.setInt(1, organizationId);

        resultSet = preparedStatement.executeQuery();

    } // execute()

}
