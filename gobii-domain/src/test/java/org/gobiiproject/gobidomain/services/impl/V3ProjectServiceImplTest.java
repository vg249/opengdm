/**
 * V3ProjectServiceImplTest.java
 * 
 * Unit test for V3ProjectServiceImpl
 * @author Rodolfo N. Duldulao, Jr.
 * @since 2020-03-07
 */
package org.gobiiproject.gobidomain.services.impl;

import static org.mockito.Mockito.when;

import java.util.List;

import org.gobiiproject.gobiiapimodel.payload.sampletracking.BrApiMasterListPayload;
import org.gobiiproject.gobiimodel.dto.auditable.V3ProjectDTO;
import org.gobiiproject.gobiimodel.entity.Cv;
import org.gobiiproject.gobiimodel.entity.v3.Project;
import org.gobiiproject.gobiisampletrackingdao.CvDao;
import org.gobiiproject.gobiisampletrackingdao.ProjectDao;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.web.WebAppConfiguration;
import org.gobiiproject.gobiimodel.cvnames.CvGroup;

@WebAppConfiguration
public class V3ProjectServiceImplTest {

    @Mock
    private ProjectDao v3ProjectDao;

    @Mock
    private CvDao cvDao;

    @InjectMocks
    private V3ProjectServiceImpl v3ProjectServiceImpl;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSimple() {

        assert v3ProjectDao != null ;

        //Mock Cvs
        List<Cv> mockCvList = new java.util.ArrayList<>();
        when(cvDao.getCvListByCvGroup(CvGroup.CVGROUP_PROJECT_PROP.getCvGroupName(), null))
        .thenReturn(
            mockCvList
        );

        List<Project> daoReturn = new java.util.ArrayList<>();
        Project mockEntity = new Project();
        mockEntity.setProjectName("PName");
        daoReturn.add(mockEntity);
        when(v3ProjectDao.getProjects(0,1000))
        .thenReturn(
            daoReturn
        );

        BrApiMasterListPayload<V3ProjectDTO> payload = v3ProjectServiceImpl.getProjects(0,  1000);
        assert payload.getResult().getData().size() == 1;
        assert payload.getResult().getData().get(0).getProjectName() == mockEntity.getProjectName();
        assert payload.getMetadata().getPagination().getPageSize() == 1;
    }

}