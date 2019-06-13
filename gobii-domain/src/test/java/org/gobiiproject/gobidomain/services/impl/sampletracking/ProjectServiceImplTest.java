package org.gobiiproject.gobidomain.services.impl.sampletracking;


import org.gobiiproject.gobidomain.security.UserContextLoader;
import org.gobiiproject.gobidomain.services.ProjectService;
import org.gobiiproject.gobiimodel.dto.entity.auditable.sampletracking.ProjectDTO;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.UUID;

import static junit.framework.TestCase.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring/test-config.xml"})
public class ProjectServiceImplTest {

    @Autowired
    private ProjectService<ProjectDTO> sampleTrackingProjectService = null;


    @BeforeClass
    public static void setUpClass() throws Exception {
        UserContextLoader userContextLoader = new UserContextLoader();
        userContextLoader.loadUser("USER_READER");
    }

    /**
     * Asserts whether service is able to create project with just required properties.
     * Required properties for creating projects are projectName and piContactId.
     * If the projectId field gets populated in returned, CreateProject is success else a fail.
     */
    @Test
    public void createProjectWithJustRequiredFields() {
        ProjectDTO newProject = new ProjectDTO();
        newProject.setPiContactId(1);
        newProject.setProjectName(UUID.randomUUID().toString());
        ProjectDTO createdProject = sampleTrackingProjectService.createProject(newProject);
        assertTrue(createdProject.getProjectId() > 0);
    }

    @Test
    public void createProjectWithAllFields() {
        ProjectDTO newProject = new ProjectDTO();
        newProject.setPiContactId(1);
        newProject.setProjectName(UUID.randomUUID().toString());
        newProject.setDivision("test division");
        newProject.setGenotypingPurpose("test purpose");
        newProject.setProjectCode("test code");
        newProject.setStudyName("test studyname");
        newProject.setDateSampled("01/02/2019");
        newProject.setProjectDescription("test description");
        ProjectDTO createdProject = sampleTrackingProjectService.createProject(newProject);
        assertTrue(createdProject.getProjectId() > 0);
    }



}
