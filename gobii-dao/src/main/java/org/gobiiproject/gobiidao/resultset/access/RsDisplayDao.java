package org.gobiiproject.gobiidao.resultset.access;

import org.gobiiproject.gobiidao.GobiiDaoException;

import java.sql.ResultSet;

/**
 * Created by Phil on 4/13/2016.
 */
public interface RsDisplayDao {

	ResultSet getTableDisplayNames() throws GobiiDaoException;
}
