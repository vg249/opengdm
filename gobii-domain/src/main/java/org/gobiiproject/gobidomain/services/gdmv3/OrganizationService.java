package org.gobiiproject.gobidomain.services.gdmv3;

import org.gobiiproject.gobiimodel.dto.gdmv3.OrganizationDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;

public interface OrganizationService {

	PagedResult<OrganizationDTO> getOrganizations(Integer page, Integer pageSizetoUse) throws Exception;

	OrganizationDTO getOrganization(Integer organizationId) throws Exception;

	OrganizationDTO createOrganization(OrganizationDTO request, String createdBy) throws Exception;
    
}