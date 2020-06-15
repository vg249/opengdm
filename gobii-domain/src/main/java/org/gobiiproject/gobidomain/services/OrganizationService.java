package org.gobiiproject.gobidomain.services;

import java.util.List;
import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.dto.auditable.OrganizationDTO;

/**
 * Created by Angel on 5/4/2016.
 */
public interface OrganizationService {

    OrganizationDTO createOrganization(OrganizationDTO organizationDTO) throws GobiiDomainException;
    OrganizationDTO replaceOrganization(Integer organizationId, OrganizationDTO organizationDTO) throws GobiiDomainException;
    List<OrganizationDTO> getOrganizations() throws GobiiDomainException;
    OrganizationDTO getOrganizationById(Integer organizationId) throws GobiiDomainException;

}
