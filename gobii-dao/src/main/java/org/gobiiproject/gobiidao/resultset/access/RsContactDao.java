package org.gobiiproject.gobiidao.resultset.access;

import java.sql.ResultSet;
import java.util.Map;
import org.gobiiproject.gobiidao.GobiiDaoException;

/**
 * Created by Angel on 4/7/2016.
 */
public interface RsContactDao {

    ResultSet getAllContactNames() throws GobiiDaoException;
    ResultSet getContactNamesForRoleName(String roleName) throws GobiiDaoException;
    ResultSet getContactDetailsByContactId(Integer contact) throws GobiiDaoException;
    ResultSet getContactDetailsByEmail(String email) throws GobiiDaoException;
    Integer createContact(Map<String,Object> parameters) throws GobiiDaoException;
    void updateContact(Map<String,Object> parameters) throws GobiiDaoException;
    ResultSet getContactDetailsByUsername(String username) throws GobiiDaoException;
}
