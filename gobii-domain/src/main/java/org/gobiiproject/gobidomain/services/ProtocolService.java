package org.gobiiproject.gobidomain.services;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.dto.container.ProtocolDTO;

/**
 * Created by VCalaminos on 2016-12-14.
 */
public interface ProtocolService {


    ProtocolDTO createProtocol(ProtocolDTO protocolDTO) throws GobiiDomainException;
    ProtocolDTO replaceProtocol(Integer protocolId, ProtocolDTO protocolDTO) throws GobiiDomainException;
}
