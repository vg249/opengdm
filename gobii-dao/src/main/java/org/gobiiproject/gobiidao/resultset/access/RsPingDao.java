package org.gobiiproject.gobiidao.resultset.access;

import java.util.List;
import java.util.Map;
import org.gobiiproject.gobiidao.GobiiDaoException;

/**
 * Created by Angel on 4/27/2016.
 */
public interface RsPingDao {


    Map<String,String> getPingResponses(List<String> pingRequests) throws GobiiDaoException;

}
