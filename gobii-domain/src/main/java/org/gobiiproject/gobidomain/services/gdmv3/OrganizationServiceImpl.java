package org.gobiiproject.gobidomain.services.gdmv3;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.gobiiproject.gobiimodel.dto.gdmv3.OrganizationDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;
import org.gobiiproject.gobiimodel.entity.Organization;
import org.gobiiproject.gobiimodel.modelmapper.ModelMapper;
import org.gobiiproject.gobiisampletrackingdao.OrganizationDao;
import org.springframework.beans.factory.annotation.Autowired;

public class OrganizationServiceImpl implements OrganizationService {

    @Autowired
    OrganizationDao organizationDao;
    

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
    
}