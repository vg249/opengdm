package org.gobiiproject.gobiidtomapping.entity.auditable.impl;

import static org.mockito.Mockito.when;

import java.beans.Transient;

import org.gobiiproject.gobiimodel.dto.auditable.V3ProjectDTO;
import org.gobiiproject.gobiimodel.entity.V3Project;
import org.gobiiproject.gobiisampletrackingdao.V3ProjectDao;
import org.gobiiproject.gobiisampletrackingdao.V3ProjectDaoImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration
public class DtoMapV3ProjectImplTest {

   
    @Test
    public void testSimple() {
        //TODO: weird Kotlin error whe using @Mock
        V3ProjectDao v3ProjectDao = Mockito.mock(V3ProjectDaoImpl.class);
        DtoMapV3ProjectImpl dtoMapImpl = new DtoMapV3ProjectImpl();
        dtoMapImpl.setV3ProjectDao(v3ProjectDao);

        assert v3ProjectDao != null;
        java.util.List<V3Project> mockList = new java.util.ArrayList<>();
        V3Project mockItem = new V3Project();
        mockItem.setProjectName("Test-Project");
        mockList.add(mockItem);
        when( v3ProjectDao.getProjects(0, 1000))
            .thenReturn(
                mockList
            );

        java.util.List<V3ProjectDTO> dtos = dtoMapImpl.getProjects(0, 1000);
        assert dtos.size() == 1;
        assert dtos.get(0).getProjectName() == mockItem.getProjectName();
    }

}