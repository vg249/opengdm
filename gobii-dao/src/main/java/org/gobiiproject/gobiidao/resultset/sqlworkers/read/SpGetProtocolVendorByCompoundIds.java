package org.gobiiproject.gobiidao.resultset.sqlworkers.read;

import org.hibernate.jdbc.Work;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
/**
 * Created by Phil on 12/26/2016.
 */

/**
 * Created by Phil on 4/11/2016.
 */
public class SpGetProtocolVendorByCompoundIds implements Work {
    /**
     * Created by Phil on 4/7/2016.
     */
    private Map<String, Object> parameters = null;

    public SpGetProtocolVendorByCompoundIds(Map<String, Object> parameters) {
        this.parameters = parameters;
    }


    private ResultSet resultSet = null;

    public ResultSet getResultSet() {
        return resultSet;
    }

    @Override
    public void execute(Connection dbConnection) throws SQLException {

        String Sql = "select * \n" +
                "from vendor_protocol \n" +
                "where vendor_id= ? and protocol_id=?;";

        PreparedStatement preparedStatement = dbConnection.prepareStatement(Sql);
        Integer protocolId = (Integer) parameters.get("protocolId");
        Integer vendorId = (Integer) parameters.get("vendorId");

        preparedStatement.setInt(1, protocolId);
        preparedStatement.setInt(2, vendorId);

        resultSet = preparedStatement.executeQuery();

    } // execute()

}

