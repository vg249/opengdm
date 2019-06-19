package org.gobiiproject.gobiidao.resultset.access;

import org.gobiiproject.gobiidao.GobiiDaoException;

import java.sql.ResultSet;
import java.util.Map;

/**
 * Created by Phil on 4/21/2016.
 */
public interface RsSampleDao {

    ResultSet getSampleDetailsByExternalCode(String externalCode) throws GobiiDaoException;

}
