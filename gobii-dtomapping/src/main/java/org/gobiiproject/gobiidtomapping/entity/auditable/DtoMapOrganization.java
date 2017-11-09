package org.gobiiproject.gobiidtomapping.entity.auditable;

import org.gobiiproject.gobiidtomapping.core.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.dto.entity.auditable.OrganizationDTO;

import java.util.List;

/**
 * Created by Angel on 5/4/2016.
 */
public interface DtoMapOrganization extends DtoMap<OrganizationDTO> {

    OrganizationDTO create(OrganizationDTO organizationDTO) throws GobiiDtoMappingException;
    OrganizationDTO replace(Integer organizationId, OrganizationDTO organizationDTO) throws GobiiDtoMappingException;
    OrganizationDTO get(Integer organizationId) throws GobiiDtoMappingException;
    List<OrganizationDTO> getList() throws GobiiDtoMappingException;

}
