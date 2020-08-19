package org.gobiiproject.gobidomain.services.gdmv3;

import lombok.extern.slf4j.Slf4j;
import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.gdmv3.ProtocolDTO;
import org.gobiiproject.gobiimodel.entity.Protocol;
import org.gobiiproject.gobiimodel.modelmapper.ModelMapper;
import org.gobiiproject.gobiisampletrackingdao.ProtocolDao;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class ProtocolServiceImpl implements ProtocolService {

    @Autowired
    private ProtocolDao protocolDao;

    @Override
    public ProtocolDTO getProtocolById(Integer protocolId) throws GobiiDomainException {
        ProtocolDTO returnVal = new ProtocolDTO();
        try {
            Protocol protocol = protocolDao.getProtocolById(protocolId);
            ModelMapper.mapEntityToDto(protocol, returnVal);
            return returnVal;
        }
        catch (GobiiException gE) {
            throw gE;
        } catch (Exception e) {
            log.error("Gobii service error", e);
            throw new GobiiDomainException(e);
        }
    }

}
