package org.gobiiproject.gobiidao.resultset.access;

import org.gobiiproject.gobiidao.GobiiDaoException;

import java.sql.ResultSet;
import java.util.List;

/**
 * Created by Phil on 4/7/2016.
 */
public interface RsContact {


    List<String> getContactNamesForRoleName(String roleName) throws GobiiDaoException;
}
