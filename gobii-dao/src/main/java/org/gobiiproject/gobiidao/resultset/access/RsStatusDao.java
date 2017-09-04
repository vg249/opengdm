package org.gobiiproject.gobiidao.resultset.access;

import org.gobiiproject.gobiidao.GobiiDaoException;

import java.sql.ResultSet;
import java.util.Map;

/**
 * Created by VCalaminos on 9/4/2017.
 */
public interface RsStatusDao {

    ResultSet getStatusDetailsForJobId(Integer jobId) throws GobiiDaoException;

    ResultSet getStatuses() throws GobiiDaoException;

    Integer createStatus(Map<String, Object> parameters) throws GobiiDaoException;

    void updateStatus(Map<String, Object> parameters) throws GobiiDaoException;

}
