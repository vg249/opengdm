package org.gobiiproject.gobidomain.services;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.headerlesscontainer.ProtocolDTO;

import java.util.List;

/**
 * Created by VCalaminos on 2016-12-14.
 */
public interface ProtocolService {


    ProtocolDTO createProtocol(ProtocolDTO protocolDTO) throws GobiiDomainException;
    ProtocolDTO replaceProtocol(Integer protocolId, ProtocolDTO protocolDTO) throws GobiiDomainException;
    ProtocolDTO getProtocolById(Integer protocolId) throws GobiiDomainException;
    List<ProtocolDTO> getProtocols() throws GobiiDomainException;
    

}
