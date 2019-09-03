package org.gobiiproject.gobiisampletrackingdao;

import org.gobiiproject.gobiimodel.entity.Project;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static junit.framework.TestCase.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring/test-config.xml"})
public class ProjectDaoTest {

    @Autowired
    private ProjectDao projectDao;

    @Test
    public void getProjectWithExistingId() {


        // get the ID of the newly created Project
        Project project = projectDao.getProjectById(1);

        assertTrue(project.getProjectId() > 0);
        assertTrue(project.getProjectId().equals(1));


    }



}
