package org.gobiiproject.gobiidao.resultset.access;

import org.gobiiproject.gobiidao.GobiiDaoException;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

/**
 * Created by Phil on 4/7/2016.
 */
public interface RsProject {


    ResultSet getProjectNamesForContactId(Integer contactId) throws GobiiDaoException;
}
