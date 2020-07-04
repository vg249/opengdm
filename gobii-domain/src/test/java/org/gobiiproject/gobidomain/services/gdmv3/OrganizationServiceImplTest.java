package org.gobiiproject.gobidomain.services.gdmv3;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.gobiiproject.gobiimodel.dto.gdmv3.OrganizationDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;
import org.gobiiproject.gobiimodel.entity.Organization;
import org.gobiiproject.gobiisampletrackingdao.ContactDao;
import org.gobiiproject.gobiisampletrackingdao.CvDao;
import org.gobiiproject.gobiisampletrackingdao.OrganizationDao;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.web.WebAppConfiguration;

@WebAppConfiguration
public class OrganizationServiceImplTest {
    
    @Mock
    private OrganizationDao organizationDao;

    @Mock
    private CvDao cvDao;

    @Mock
    private ContactDao contactDao;

    @InjectMocks
    private OrganizationServiceImpl organizationServiceImpl;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetOrganizationsOk() throws Exception {
        List<Organization> mockList = new ArrayList<>();
        mockList.add(new Organization());

        when(organizationDao.getOrganizations(0, 1000)).thenReturn(mockList);
        PagedResult<OrganizationDTO> result = organizationServiceImpl.getOrganizations(0, 1000);
        assertTrue( result.getCurrentPageNum() == 0);
        assertTrue( result.getCurrentPageSize() == 1);
        assertTrue( result.getResult().size() == 1);
    }

    @Test
    public void testGetOrganizationOk() throws Exception {
        when(organizationDao.getOrganization(200)).thenReturn(new Organization());
        organizationServiceImpl.getOrganization(200);
        verify(organizationDao, times(1)).getOrganization(200);
    }
}