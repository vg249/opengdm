package sampletracking;


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

import java.util.List;
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
        UserContextLoader userContextLoader = new UserContextLoader(
                "classpath:/spring/test-config.xml");
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
        newProject.getProperties().put("division", "test division");
        newProject.getProperties().put("genotyping_purpose", "test purpose");
        newProject.getProperties().put("study_name","test studyname");
        newProject.getProperties().put("date_sampled", "01/02/2019");
        newProject.setProjectCode("test code");
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

    /**
     * Asserts whether service is able to get the project information given the ID of a newly created Project.
     * If the returned project ID is null or not equal to the given ID, the test will fail. Otherwise, it will be successful.
     */
    @Test
    public void getProjectWithExistingId() {

        ProjectDTO newProject = new ProjectDTO();
        newProject.setPiContactId(1);
        newProject.setProjectName(UUID.randomUUID().toString());
        newProject = sampleTrackingProjectService.createProject(newProject);
        assertTrue(newProject.getProjectId() > 0);

        // get the ID of the newly created Project
        Integer projectId = newProject.getProjectId();

        ProjectDTO projectDTO = sampleTrackingProjectService.getProjectById(projectId);

        assertTrue(projectDTO.getProjectId() > 0);
        assertTrue(projectDTO.getProjectId().equals(newProject.getProjectId()));

    }

    /**
     * Asserts failure to get project information with invalid project ID.
     * Asserts whether the exception thrown is GobiiException for Entity does not exists.
     */
    @Test
    public void getProjectWithNonExistingId() {

        ProjectDTO projectDTO = new ProjectDTO();

        try {
            // non-existing ID
            Integer projectId = 0;

            projectDTO = sampleTrackingProjectService.getProjectById(projectId);

        } catch (GobiiException gE) {

            assertTrue(gE.getGobiiValidationStatusType() == GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST);

            assertFalse(projectDTO.getProjectId() > 0);
        }

    }

}
