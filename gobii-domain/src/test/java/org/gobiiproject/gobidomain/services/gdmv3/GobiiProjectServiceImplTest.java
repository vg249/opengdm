/**
 * V3ProjectServiceImplTest.java
 * 
 * Unit test for V3ProjectServiceImpl
 * @author Rodolfo N. Duldulao, Jr.
 * @since 2020-03-07
 */
package org.gobiiproject.gobidomain.services.gdmv3;

import static org.mockito.Mockito.when;

import java.util.List;

import org.gobiiproject.gobiimodel.cvnames.CvGroup;
import org.gobiiproject.gobiimodel.dto.auditable.GobiiProjectDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;
import org.gobiiproject.gobiimodel.entity.Cv;
import org.gobiiproject.gobiimodel.entity.Project;
import org.gobiiproject.gobiisampletrackingdao.ContactDao;
import org.gobiiproject.gobiisampletrackingdao.CvDao;
import org.gobiiproject.gobiisampletrackingdao.ProjectDao;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.web.WebAppConfiguration;

@WebAppConfiguration
public class GobiiProjectServiceImplTest {

    @Mock
    private ProjectDao projectDao;

    @Mock
    private CvDao cvDao;

    @Mock
    private ContactDao contactDao;

    @InjectMocks
    private ProjectServiceImpl v3ProjectServiceImpl;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSimple() {

        assert projectDao != null ;

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
        when(projectDao.getProjects(0,1000, null))
        .thenReturn(
            daoReturn
        );

        PagedResult<GobiiProjectDTO> payload = v3ProjectServiceImpl.getProjects(0,  1000, null);
        assert payload.getResult().size() == 1 ;
        assert payload.getCurrentPageNum() == 0;
        assert payload.getCurrentPageSize() == 1;
    }

    //TODO: create unit tests for the other service methods.

}