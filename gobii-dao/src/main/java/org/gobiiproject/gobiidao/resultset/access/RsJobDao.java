package org.gobiiproject.gobiidao.resultset.access;

import java.util.Map;
import org.gobiiproject.gobiidao.GobiiDaoException;

/**
 * Created by VCalaminos on 9/4/2017.
 */
public interface RsJobDao {

    Integer createJobWithCvTerms(Map<String, Object> parameters) throws GobiiDaoException;

    void updateJobWithCvTerms(Map<String, Object> parameters) throws GobiiDaoException;

}
