package org.gobiiproject.gobiidao.resultset.access;

import org.gobiiproject.gobiidao.GobiiDaoException;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

/**
 * Created by Phil on 4/7/2016.
 */
public interface RsContact {


    ResultSet getContactNamesForRoleName(String roleName) throws GobiiDaoException;
    ResultSet getContactsForRoleName(String roleName) throws GobiiDaoException;
}
