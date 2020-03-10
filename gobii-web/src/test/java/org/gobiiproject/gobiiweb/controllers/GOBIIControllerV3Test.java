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
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.ArrayList;
import java.util.List;

import org.gobiiproject.gobidomain.services.ProjectService;
import org.gobiiproject.gobidomain.services.GobiiProjectService;
import org.gobiiproject.gobiiapimodel.payload.sampletracking.BrApiMasterListPayload;
import org.gobiiproject.gobiimodel.dto.auditable.ProjectDTO;
import org.gobiiproject.gobiimodel.dto.auditable.GobiiProjectDTO;

import static org.mockito.Mockito.when;

@ActiveProfiles("projectsController-test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
    classes = GOBIIControllerV3TestConfiguration.class
  //locations = { "classpath:/spring/application-config.xml" }
)
@WebAppConfiguration
public class GOBIIControllerV3Test {

    @Mock
    private GobiiProjectService projectService;

    @InjectMocks
    private GOBIIControllerV3 gobiiControllerV3;

    private MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(gobiiControllerV3).build();
        //assert this.projectsController.getProjectService() != null;

    }

    private GobiiProjectDTO createMockProjectDTO() {
        GobiiProjectDTO dto = new GobiiProjectDTO();
        dto.setId(1);
        dto.setModifiedBy(1);
        dto.setProjectName("test-project");
        return dto;
    }

    @Test
    public void simpleTest() throws Exception {
        assert projectService != null;
        List<GobiiProjectDTO> mockList = new ArrayList<GobiiProjectDTO>();
        GobiiProjectDTO mockItem = createMockProjectDTO();
        mockList.add(mockItem);
        BrApiMasterListPayload<GobiiProjectDTO> mockPayload = new BrApiMasterListPayload<>(mockList, 1, 0);
        when(
            projectService.getProjects(0, 1000)
        ).thenReturn(
            mockPayload
        );

        mockMvc.perform(
            MockMvcRequestBuilders
            .get("/gobii-dev/gobii/v3/projects")
            .contextPath("/gobii-dev")
        )
        .andDo(print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.metadata.pagination.currentPage").value(0))
        .andExpect(jsonPath("$.metadata.pagination.pageSize").value(1))
        .andExpect(jsonPath("$.result.data[0].projectId").value(mockItem.getProjectId()))
        .andExpect(jsonPath("$.result.data[0].projectName").value(mockItem.getProjectName()))
        ;

    }
    
    
}