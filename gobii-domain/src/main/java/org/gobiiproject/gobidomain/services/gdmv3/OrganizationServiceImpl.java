package org.gobiiproject.gobidomain.services.gdmv3;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.gdmv3.OrganizationDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;
import org.gobiiproject.gobiimodel.entity.Contact;
import org.gobiiproject.gobiimodel.entity.Cv;
import org.gobiiproject.gobiimodel.entity.Organization;
import org.gobiiproject.gobiimodel.modelmapper.ModelMapper;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.gobiiproject.gobiimodel.utils.LineUtils;
import org.gobiiproject.gobiisampletrackingdao.ContactDao;
import org.gobiiproject.gobiisampletrackingdao.CvDao;
import org.gobiiproject.gobiisampletrackingdao.OrganizationDao;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OrganizationServiceImpl implements OrganizationService {

    @Autowired
    OrganizationDao organizationDao;
    

    @Autowired
    CvDao cvDao;

    @Autowired
    ContactDao contactDao;
    

    @Transactional
    @Override
    public PagedResult<OrganizationDTO> getOrganizations(Integer page, Integer pageSizetoUse) throws Exception {
        Integer offset = page * pageSizetoUse;
        List<Organization> orgList = organizationDao.getOrganizations(offset, pageSizetoUse);
        List<OrganizationDTO> orgDTOs = new ArrayList<>();

        orgList.forEach(organization -> {
            OrganizationDTO organizationDTO = new OrganizationDTO();
            ModelMapper.mapEntityToDto(organization, organizationDTO);
            orgDTOs.add(organizationDTO);
        });

        PagedResult<OrganizationDTO> pagedResult = new PagedResult<>();
        pagedResult.setCurrentPageNum(page);
        pagedResult.setCurrentPageSize(orgDTOs.size());
        pagedResult.setResult(orgDTOs);

        return pagedResult;   
    }

    @Transactional
    @Override
    public OrganizationDTO getOrganization(Integer organizationId) throws Exception {
        Organization organization = this.loadOrganization(organizationId);
        OrganizationDTO organizationDTO = new OrganizationDTO();
        ModelMapper.mapEntityToDto(organization, organizationDTO);
        return organizationDTO;
    }

    @Transactional
    @Override
    public OrganizationDTO createOrganization(OrganizationDTO request, String createdBy) throws Exception {
        Organization organization = new Organization();
        organization.setName(request.getOrganizationName());
        organization.setAddress(request.getOrganizationAddress());
        organization.setWebsite(request.getOrganizationWebsite());

         // Get the Cv for status, new row
        Cv cv = cvDao.getNewStatus();
        organization.setStatus(cv); //TODO:  use status cv?

        //audit
        Contact creator = contactDao.getContactByUsername(createdBy);
        if (creator != null)
            organization.setCreatedBy(creator.getContactId());
        organization.setCreatedDate(new java.util.Date());
        organization = organizationDao.createOrganization(organization);

        OrganizationDTO createdOrganization = new OrganizationDTO();
        ModelMapper.mapEntityToDto(organization, createdOrganization);
        return createdOrganization;
    }

    @Transactional
    @Override
    public OrganizationDTO updateOrganization(Integer organizationId, OrganizationDTO request, String updatedBy) throws Exception {
        Organization organization = this.loadOrganization(organizationId);
        if (request.getOrganizationName() != null) {
            //name cannot be empty
            if (LineUtils.isNullOrEmpty(request.getOrganizationName())) throw new GobiiException(GobiiStatusLevel.ERROR, GobiiValidationStatusType.BAD_REQUEST, "Organization Name cannot be empty");
            organization.setName(request.getOrganizationName());
        }

        if (request.getOrganizationAddress() != null) {
            organization.setAddress(request.getOrganizationAddress());
        }

        if (request.getOrganizationWebsite() != null) {
            organization.setWebsite(request.getOrganizationWebsite());
        }
        //audit items
        //audit
        Contact creator = contactDao.getContactByUsername(updatedBy);
        if (creator != null)
            organization.setModifiedBy(creator.getContactId());
        organization.setModifiedDate(new java.util.Date());

        organization = organizationDao.updateOrganization(organization);
        OrganizationDTO organizationDTO = new OrganizationDTO();
        ModelMapper.mapEntityToDto(organization, organizationDTO);
        return organizationDTO;

    }

    @Transactional
    @Override
    public void deleteOrganization(Integer organizationId) throws Exception {
        Organization organization = this.loadOrganization(organizationId);
        organizationDao.deleteOrganization(organization);
    }

    private Organization loadOrganization(Integer organizationId) throws Exception {
        Organization organization = organizationDao.getOrganization(organizationId);
        if (organization == null) {
            throw new GobiiException(GobiiStatusLevel.ERROR, GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                    "Not found");
        }
        return organization;
    }
    
}