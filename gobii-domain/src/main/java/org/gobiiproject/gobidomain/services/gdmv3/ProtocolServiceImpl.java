package org.gobiiproject.gobidomain.services.gdmv3;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.list.AbstractListDecorator;
import org.codehaus.janino.Mod;
import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.gdmv3.ProtocolDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;
import org.gobiiproject.gobiimodel.entity.Protocol;
import org.gobiiproject.gobiimodel.modelmapper.ModelMapper;
import org.gobiiproject.gobiisampletrackingdao.ProtocolDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @Override
    public PagedResult<ProtocolDTO> getProtocols(Integer pageSize, Integer pageNum,
                                                 Integer platformId) {
        List<ProtocolDTO> protocolDTOs = new ArrayList<>();
        try {

            Objects.requireNonNull(pageSize, "pageSize: Required non null");
            Objects.requireNonNull(pageNum, "pageNum: Required non null");

            List<Protocol> protocols =
                protocolDao.getProtocols(pageSize, pageSize*pageNum, platformId);

            protocols.forEach(protocol -> {
                ProtocolDTO protocolDTO = new ProtocolDTO();
                ModelMapper.mapEntityToDto(protocol, protocolDTO);
                protocolDTOs.add(protocolDTO);
            });

            return PagedResult.createFrom(pageNum, protocolDTOs);
        }
        catch (GobiiException gE) {
            throw gE;
        } catch (Exception e) {
            log.error("Gobii service error", e);
            throw new GobiiDomainException(e);
        }
    }

    @Override
    public ProtocolDTO createProtocol(ProtocolDTO protocolDTO) {

        ProtocolDTO protocolDTOCreated = new ProtocolDTO();

        try {
            Protocol protocolToCreate = new Protocol();

            //Map entity values from dto
            ModelMapper.mapDtoToEntity(protocolDTO, protocolToCreate);
            Protocol protocol = protocolDao.createProtocol(protocolToCreate);

            ModelMapper.mapEntityToDto(protocol, protocolDTOCreated);

            return protocolDTOCreated;
        }
        catch (GobiiException gE) {
            throw gE;
        } catch (Exception e) {
            log.error("Gobii service error", e);
            throw new GobiiDomainException(e);
        }
    }

    @Override
    public ProtocolDTO patchProtocol(Integer protocolId, ProtocolDTO protocolDTO) {
        ProtocolDTO protocolDTOUpdtaed = new ProtocolDTO();
        try {

            Protocol protocolToBeUpdated = protocolDao.getProtocolById(protocolId);

            // Map the values to be updated. Ignore null values.
            ModelMapper.mapDtoToEntity(protocolDTO, protocolDTOUpdtaed, true);

            Protocol protocolUpdated = protocolDao.patchProtocol(protocolToBeUpdated);
            ModelMapper.mapEntityToDto(protocolUpdated, protocolDTOUpdtaed);
            return protocolDTOUpdtaed;
        }
        catch (GobiiException gE) {
            throw gE;
        } catch (Exception e) {
            log.error("Gobii service error", e);
            throw new GobiiDomainException(e);
        }
    }

    @Override
    public void deleteProtocol(Integer protocolId) {
        try {
            Protocol protocol = protocolDao.getProtocolById(protocolId);
            protocolDao.deleteProtocol(protocol);
        }
        catch (GobiiException gE) {
            throw gE;
        } catch (Exception e) {
            log.error("Gobii service error", e);
            throw new GobiiDomainException(e);
        }
    }

}
