package org.gobiiproject.gobidomain.services.impl.sampletracking;


import org.gobiiproject.gobidomain.security.UserContextLoader;
import org.gobiiproject.gobidomain.services.ProjectService;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.entity.auditable.sampletracking.ProjectDTO;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.UUID;

import static junit.framework.TestCase.assertFalse;
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

    /**
     * Asserts successful project creation with all the fields.
     */
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


    /**
     * Asserts failure to create project with Duplicate project name
     * for the same PI contact Id.
     * Also asserts whether the Exception type is GobiiException for Entity already exists type.
     */
    @Test
    public void createDuplicateProject() {
        ProjectDTO newProject = new ProjectDTO();
        ProjectDTO duplicateProject = new ProjectDTO();
        try {

            newProject.setPiContactId(1);
            String projectName = UUID.randomUUID().toString();
            newProject.setProjectName(projectName);
            sampleTrackingProjectService.createProject(newProject);

            assertTrue(newProject.getProjectId() > 0);

            duplicateProject.setPiContactId(1);
            duplicateProject.setProjectName(projectName);
            sampleTrackingProjectService.createProject(duplicateProject);

        }
        catch(GobiiException gE) {

            assertTrue(gE.getGobiiValidationStatusType() == GobiiValidationStatusType.ENTITY_ALREADY_EXISTS);

            assertFalse(duplicateProject.getProjectId() > 0);

        }
    }

    /**
     * Asserts failure to create project with invalid contact Id.
     * Assert whether the exception type is GobiiException for Bad request.
     */
    @Test
    public void createProjectWithInvalidContactId() {
        ProjectDTO newProject = new ProjectDTO();

        try {
            newProject.setPiContactId(148);
            newProject.setProjectName(UUID.randomUUID().toString());
            sampleTrackingProjectService.createProject(newProject);
        }
        catch(GobiiException gE) {

            assertTrue(gE.getGobiiValidationStatusType() == GobiiValidationStatusType.BAD_REQUEST);

            assertFalse(newProject.getProjectId() > 0);
        }
    }

}
