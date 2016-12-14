package org.gobiiproject.gobiidtomapping;

import org.gobiiproject.gobiimodel.dto.container.ProtocolDTO;

/**
 * Created by VCalaminos on 2016-12-14.
 */
public interface DtoMapProtocol {

    ProtocolDTO getProtocolDetails(Integer protocolId) throws Exception;
    ProtocolDTO createProtocol(ProtocolDTO protocolDTO) throws GobiiDtoMappingException;
    ProtocolDTO replaceProtocol(Integer protocolId, ProtocolDTO protocolDTO) throws GobiiDtoMappingException;
}
