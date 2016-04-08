package org.gobiiproject.gobiidao.resultset.access.impl;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.resultset.access.RsContact;
import org.gobiiproject.gobiidao.resultset.core.StoredProcExec;
import org.gobiiproject.gobiidao.resultset.spworkers.SpGetContactsByRoleName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Phil on 4/7/2016.
 */
public class RsContactSpImpl implements RsContact {

    @Autowired
    private StoredProcExec storedProcExec = null;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public List<String> getContactNamesForRoleName(String roleName) throws GobiiDaoException {

        List<String> returnVal = new ArrayList<>();

        try {

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("roleName", roleName);
            SpGetContactsByRoleName spGetContactsByRoleName = new SpGetContactsByRoleName(parameters);

            storedProcExec.doWithConnection(spGetContactsByRoleName);

            ResultSet resultSet = spGetContactsByRoleName.getResultSet();
            while (resultSet.next()) {
                String lastName = resultSet.getString("lastname");
                String firstName = resultSet.getString("firstname");
                returnVal.add(lastName + "," + firstName);

            }
        } catch (SQLException e) {
            throw( new GobiiDaoException(e) );
        }

        return returnVal;
    }
}
