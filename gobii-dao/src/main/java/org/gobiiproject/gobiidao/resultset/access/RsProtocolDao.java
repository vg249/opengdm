package org.gobiiproject.gobiidao.resultset.access;

import org.gobiiproject.gobiidao.GobiiDaoException;
import java.util.Map;
import java.sql.ResultSet;
/**
 * Created by VCalaminos on 2016-12-14.
 */
public interface RsProtocolDao {

    ResultSet getProtocolDetailsByProtocolId(Integer protocolId) throws GobiiDaoException;
    Integer createProtocol(Map<String, Object> parameters) throws GobiiDaoException;
    void updateProtocol(Map<String, Object> parameters) throws GobiiDaoException;
    Integer createVendorProtocol(Map<String, Object> parameters) throws GobiiDaoException;
    ResultSet getProtocolNames() throws GobiiDaoException;
    ResultSet getVendorProtocolNames() throws GobiiDaoException;
    ResultSet getVendorByProtocolVendorName(Map<String, Object> parameters) throws GobiiDaoException;

}
