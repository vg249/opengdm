/**
 * ProjectsControllerTest.java
 * Unit test for Projects endpoints
 * 
 * @author Rodolfo N. Duldulao, Jr. <rnduldulaojr@gmail.com>
 * @version 1.0
 * @since 2020-03-06
 */
package org.gobiiproject.gobiiweb.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.util.ArrayList;
import java.util.List;

import org.gobiiproject.gobidomain.services.ProjectService;
import org.gobiiproject.gobiimodel.dto.auditable.ProjectDTO;
import org.gobiiproject.gobiiweb.DataSourceSelector;
import static org.mockito.Mockito.when;

@ActiveProfiles("projectsController-test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ProjectsControllerTestConfiguration.class
// locations = { "classpath:/spring/application-config.xml" }
)
@WebAppConfiguration
public class ProjectsControllerTest {
    static {
        // TODO: this is bad way of setting the property, is there someway to do it
        // using WebAppConfiguration?
        System.setProperty("cfgFqpn", ProjectsControllerTest.class.getResource("mockconfig.xml").getPath());
    }

    @Mock
    private ProjectService<ProjectDTO> projectService;

    @InjectMocks
    private ProjectsController projectsController;

    private MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(projectsController).build();
        assert this.projectsController.getProjectService() != null;

    }

    private ProjectDTO createMockProjectDTO() {
        ProjectDTO dto = new ProjectDTO();
        dto.setId(1);
        dto.setModifiedBy(1);
        dto.setProjectName("test-project");
        return dto;
    }
    
    @Test
    public void sampleTest() throws Exception {
        List<ProjectDTO> mockList = new ArrayList<ProjectDTO>();
        mockList.add(createMockProjectDTO());
        when(
            projectService.getProjects(1, 1000)
        ).thenReturn(
            mockList
        );

        mockMvc.perform(
            MockMvcRequestBuilders
            .get("/gobii-dev/gobii/v3/projects")
            .contextPath("/gobii-dev")
        )
        .andDo(print())
        .andExpect(MockMvcResultMatchers.status().isOk());
    }  
}