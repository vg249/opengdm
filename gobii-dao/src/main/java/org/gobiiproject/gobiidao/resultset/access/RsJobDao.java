package org.gobiiproject.gobiidao.resultset.access;

import org.gobiiproject.gobiidao.GobiiDaoException;

import java.sql.ResultSet;
import java.util.Map;

/**
 * Created by VCalaminos on 9/4/2017.
 */
public interface RsJobDao {

    Integer createJobWithCvTerms(Map<String, Object> parameters) throws GobiiDaoException;

    void updateJobWithCvTerms(Map<String, Object> parameters) throws GobiiDaoException;

}
