package org.gobiiproject.gobiidao.resultset.sqlworkers.read;

import org.gobiiproject.gobiidao.resultset.core.ResultColumnApplicator;
import org.gobiiproject.gobiimodel.headerlesscontainer.ContactDTO;
import org.hibernate.jdbc.Work;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Phil on 4/7/2016.
 */
public class SpGetContacts implements Work {

    private Map<String, Object> parameters = null;

    public SpGetContacts() {
    }


    private ResultSet resultSet = null;

    public ResultSet getResultSet() {
        return resultSet;
    }

    @Override

    public void execute(Connection dbConnection) throws SQLException {

        String sql = "select * from contact order by lower(lastname),lower(firstname)";

        PreparedStatement preparedStatement = dbConnection.prepareStatement(sql);

        resultSet = preparedStatement.executeQuery();

        List<ContactDTO> contacts = new ArrayList<>();
        while (resultSet.next()) {
            ContactDTO currentContactDao = new ContactDTO();
            ResultColumnApplicator.applyColumnValues(resultSet, currentContactDao);
            contacts.add(currentContactDao);
        }


        String temp = "foo";

    } // execute()
}
