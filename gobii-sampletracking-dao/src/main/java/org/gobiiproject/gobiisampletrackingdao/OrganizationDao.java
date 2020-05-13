package org.gobiiproject.gobiisampletrackingdao;

import java.util.List;

import org.gobiiproject.gobiimodel.entity.Organization;

public interface OrganizationDao {

    public List<Organization> getOrganizations(Integer offset, Integer pageSize) throws Exception;

	public Organization getOrganization(Integer organizationId) throws Exception;
    
}