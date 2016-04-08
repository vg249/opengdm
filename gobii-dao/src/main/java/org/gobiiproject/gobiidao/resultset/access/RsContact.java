package org.gobiiproject.gobiidao.resultset.access;

import org.gobiiproject.gobiidao.GobiiDaoException;

import java.util.List;
import java.util.Map;

/**
 * Created by Phil on 4/7/2016.
 */
public interface RsContact {


    List<Map<String,Object>> getContactNamesForRoleName(String roleName) throws GobiiDaoException;
    List<Map<String,Object>> getContactsForRoleName(String roleName) throws GobiiDaoException;
}
