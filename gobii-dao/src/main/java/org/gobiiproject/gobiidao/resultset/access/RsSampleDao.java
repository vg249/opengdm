package org.gobiiproject.gobiidao.resultset.access;

import java.sql.ResultSet;
import org.gobiiproject.gobiidao.GobiiDaoException;

/**
 * Created by Phil on 4/21/2016.
 */
public interface RsSampleDao {

    ResultSet getSampleDetailsByExternalCode(String externalCode) throws GobiiDaoException;

}
