package org.gobiiproject.gobiisampletrackingdao;

import org.gobiiproject.gobiimodel.dto.children.CvPropertyDTO;
import org.gobiiproject.gobiimodel.entity.Project;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring/test-config.xml"})
public class ProjectDaoImplTest {

    @Autowired
    private ProjectDao v3ProjectDao;


    @Before
    public void init() {
        //TODO: insert mock data
    }

    @Test
    @Transactional
    public void testSimpleQuery() {
       assert v3ProjectDao != null;
       assert v3ProjectDao.getProjects(0, 1000) != null;
       //assert v3ProjectDao.getProjects(0, 1000).size() > 0;
    }

    @Test
    @Transactional
    public void testCreateProject() throws Exception {
        Project project = v3ProjectDao.createProject("4", "test", "", null);
        assert project != null;
        assert project.getProjectId() > 0;
    }

    @Test
    @Transactional
    public void testCreateProjectWithProperties() throws Exception {
        java.util.List<CvPropertyDTO> cvProps = new java.util.ArrayList<>();

        CvPropertyDTO prop1 = new CvPropertyDTO();
        prop1.setPropertyId(4);
        prop1.setPropertyValue("test-value");
        cvProps.add(prop1);

        Project project = v3ProjectDao.createProject("4", "test", "test", cvProps);
        assert project != null;
        assert project.getProjectId() > 0;
        assert project.getProperties().size() == 1;
    }

}



