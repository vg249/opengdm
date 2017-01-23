package org.gobiiproject.gobiidao.resultset.access;

import org.gobiiproject.gobiidao.GobiiDaoException;

import java.sql.ResultSet;

/**
 * Created by VCalaminos on 2017-01-13.
 */
public interface RsDataSetTypeDao {

    ResultSet getDatasetTypeNames() throws GobiiDaoException;

}
