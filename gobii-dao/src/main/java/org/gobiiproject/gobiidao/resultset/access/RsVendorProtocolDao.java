package org.gobiiproject.gobiidao.resultset.access;

import org.gobiiproject.gobiidao.GobiiDaoException;
import java.util.Map;

/**
 * Created by VCalaminos on 2016-12-12.
 */
public interface RsVendorProtocolDao {


    Integer createVendorProtocol(Map<String, Object> parameters) throws GobiiDaoException;
    void updateVendorProtocol(Map<String, Object> parameters) throws GobiiDaoException;

}
