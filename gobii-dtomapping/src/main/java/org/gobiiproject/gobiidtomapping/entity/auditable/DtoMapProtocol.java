package org.gobiiproject.gobiidtomapping.entity.auditable;

import org.gobiiproject.gobiidtomapping.core.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.entity.auditable.OrganizationDTO;
import org.gobiiproject.gobiimodel.dto.entity.auditable.ProtocolDTO;
import org.gobiiproject.gobiimodel.dto.entity.children.VendorProtocolDTO;

import java.util.List;

/**
 * Created by VCalaminos on 2016-12-14.
 */
public interface DtoMapProtocol extends DtoMap<ProtocolDTO> {

    ProtocolDTO create(ProtocolDTO protocolDTO) throws GobiiDtoMappingException;
    ProtocolDTO replace(Integer protocolId, ProtocolDTO protocolDTO) throws GobiiDtoMappingException;
    ProtocolDTO get(Integer protocolId) throws GobiiDtoMappingException;
    List<ProtocolDTO> getList() throws GobiiDtoMappingException;

    OrganizationDTO addVendotrToProtocol(Integer protocolId, OrganizationDTO organizationDTO) throws GobiiDtoMappingException;
    OrganizationDTO getVendorForProtocolByName(String vendorProtocolName) throws GobiiDtoMappingException;    List<OrganizationDTO> getVendorsForProtocolByProtocolId(Integer protocolId) throws GobiiDtoMappingException;
    void addVendorProtocolsToOrganization(OrganizationDTO organizationDTO)  throws GobiiException;
    void addVendorProtocolsToProtocol(ProtocolDTO protocolDTO)  throws GobiiException;
    OrganizationDTO updateOrReplaceVendotrByProtocolId(Integer protocolId, OrganizationDTO organizationDTO) throws GobiiDtoMappingException;
    ProtocolDTO getProtocolsByExperimentId(Integer experimentId)  throws GobiiDtoMappingException;
    VendorProtocolDTO getVendorProtocolByVendorProtocolId(Integer vendorProtocolId) throws GobiiDtoMappingException;
}
