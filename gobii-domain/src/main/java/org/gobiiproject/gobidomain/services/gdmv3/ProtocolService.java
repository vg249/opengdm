package org.gobiiproject.gobidomain.services.gdmv3;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.dto.gdmv3.ProtocolDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;

import java.util.List;

public interface ProtocolService {
    ProtocolDTO getProtocolById(Integer protocolId) throws GobiiDomainException;
    PagedResult<ProtocolDTO> getProtocols(Integer pageSize, Integer pageNum,
                                          Integer platformId);
    ProtocolDTO createProtocol(ProtocolDTO protocolDTO);
}
