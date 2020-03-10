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
import org.gobiiproject.gobiimodel.entity.V3Project;
import org.gobiiproject.gobiisampletrackingdao.V3ProjectDao;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.web.WebAppConfiguration;

@WebAppConfiguration
public class V3ProjectServiceImplTest {

    @Mock
    private V3ProjectDao v3ProjectDao;

    @InjectMocks
    private V3ProjectServiceImpl v3ProjectServiceImpl;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSimple() {
        assert v3ProjectDao != null ;
        List<V3Project> daoReturn = new java.util.ArrayList<>();
        V3Project mockEntity = new V3Project();
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

        //TODO
    }


}