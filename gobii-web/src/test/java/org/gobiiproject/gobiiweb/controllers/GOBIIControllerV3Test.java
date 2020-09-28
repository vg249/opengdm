/**
 * ProjectsControllerTest.java
 * Unit test for Projects endpoints
 * 
 * @author Rodolfo N. Duldulao, Jr. <rnduldulaojr@gmail.com>
 * @version 1.0
 * @since 2020-03-06
 */
package org.gobiiproject.gobiiweb.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyListOf;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.gobiiproject.gobiidomain.services.gdmv3.AnalysisService;
import org.gobiiproject.gobiidomain.services.gdmv3.ContactService;
import org.gobiiproject.gobiidomain.services.gdmv3.CvService;
import org.gobiiproject.gobiidomain.services.gdmv3.DatasetService;
import org.gobiiproject.gobiidomain.services.gdmv3.ExperimentService;
import org.gobiiproject.gobiidomain.services.gdmv3.MapsetService;
import org.gobiiproject.gobiidomain.services.gdmv3.MarkerGroupService;
import org.gobiiproject.gobiidomain.services.gdmv3.OrganizationService;
import org.gobiiproject.gobiidomain.services.gdmv3.PlatformService;
import org.gobiiproject.gobiidomain.services.gdmv3.ProjectService;
import org.gobiiproject.gobiidomain.services.gdmv3.ReferenceService;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.children.CvPropertyDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.CvDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.CvTypeDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.DatasetDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.DatasetRequestDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.MapsetDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.MarkerDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.MarkerGroupDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.OrganizationDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.PlatformDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.ProjectDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.ReferenceDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.gobiiproject.gobiiweb.CropRequestAnalyzer;
import org.gobiiproject.gobiiweb.controllers.gdm.v3.GOBIIControllerV3;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import lombok.extern.slf4j.Slf4j;

@ActiveProfiles("projectsController-test")
@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(SpringJUnit4ClassRunner.class)
@PrepareForTest(CropRequestAnalyzer.class)
@ContextConfiguration(classes = GOBIIControllerV3TestConfiguration.class
// locations = { "classpath:/spring/application-config.xml" }
)
@WebAppConfiguration
@Slf4j
public class GOBIIControllerV3Test {
    @Mock
    private ProjectService projectService;

    @Mock
    private ContactService contactService;

    @Mock
    private ExperimentService experimentService;

    @Mock
    private AnalysisService analysisService;

    @Mock
    private DatasetService datasetService;

    @Mock
    private ReferenceService referenceService;

    @Mock
    private MapsetService mapsetService;

    @Mock
    private MarkerGroupService markerGroupService;

    @Mock
    private OrganizationService organizationService;

    @Mock
    private CvService cvService;

    @Mock
    private PlatformService platformService;

    @InjectMocks
    private GOBIIControllerV3 gobiiControllerV3;

    private MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
        log.info("Setting up Gobii V3 Controller test");
        MockitoAnnotations.initMocks(this);

        this.mockMvc = MockMvcBuilders.standaloneSetup(gobiiControllerV3)
                .setControllerAdvice(new GlobalControllerExceptionHandler()).build();

        // assert this.projectsController.getProjectService() != null.

    }

    private ProjectDTO createMockProjectDTO() {
        final ProjectDTO dto = new ProjectDTO();
        dto.setId(1);
        dto.setModifiedBy(1);
        dto.setProjectName("test-project");

        final List<CvPropertyDTO> propDtoList = createMockPropDTOList();
        dto.setProperties(propDtoList);
        return dto;
    }

    private CvPropertyDTO createMockPropDTO() {
        // mock DTO
        final CvPropertyDTO propDto = new CvPropertyDTO();
        propDto.setPropertyId(1);
        propDto.setPropertyName("test-prop");
        propDto.setPropertyGroupId(1);
        propDto.setPropertyGroupName("test-group");
        propDto.setPropertyGroupType(1);
        return propDto;
    }

    private List<CvPropertyDTO> createMockPropDTOList() {
        final List<CvPropertyDTO> propDtoList = new java.util.ArrayList<>();
        propDtoList.add(createMockPropDTO());
        return propDtoList;
    }


 
   
    @Test
    public void testGetReferences() throws Exception {
        when(referenceService.getReferences(0, 1000)).thenReturn(new PagedResult<ReferenceDTO>());
        mockMvc.perform(MockMvcRequestBuilders.get("/gobii-dev/gobii/v3/references").contextPath("/gobii-dev"))
                .andDo(print()).andExpect(MockMvcResultMatchers.status().isOk());

        verify(referenceService, times(1)).getReferences(0, 1000);

    }

 

 

    // --- Cv tests

    // --Platforms
 
    // -- References


    @Test
    public void testCreateMarkerSet() throws Exception {
        final String requestJson = "{\"markerGroupName\": \"test-marker-group\", \"germplasmGroup\": \"test-germplasm-group\"}";

        when(projectService.getDefaultProjectEditor()).thenReturn("test-user");
        when(markerGroupService.createMarkerGroup(any(MarkerGroupDTO.class), eq("test-user")))
                .thenReturn(new MarkerGroupDTO());
        mockMvc.perform(MockMvcRequestBuilders.post("/gobii-dev/gobii/v3/markergroups")
                .contentType(MediaType.APPLICATION_JSON).content(requestJson).contextPath("/gobii-dev")).andDo(print())
                .andExpect(MockMvcResultMatchers.status().isCreated());

        verify(markerGroupService, times(1)).createMarkerGroup(any(MarkerGroupDTO.class), eq("test-user"));
    }

    @Test
    public void testListMarkerGroups() throws Exception {

        when(markerGroupService.getMarkerGroups(0, 1000)).thenReturn(new PagedResult<MarkerGroupDTO>());

        mockMvc.perform(MockMvcRequestBuilders.get("/gobii-dev/gobii/v3/markergroups").contextPath("/gobii-dev"))
                .andDo(print()).andExpect(MockMvcResultMatchers.status().isOk());

        verify(markerGroupService, times(1)).getMarkerGroups(0, 1000);
    }

    @Test
    public void testGetMarkerGroupById() throws Exception {
        when(markerGroupService.getMarkerGroup(123)).thenReturn(new MarkerGroupDTO());
        mockMvc.perform(MockMvcRequestBuilders.get("/gobii-dev/gobii/v3/markergroups/123").contextPath("/gobii-dev"))
                .andDo(print()).andExpect(MockMvcResultMatchers.status().isOk());

        verify(markerGroupService, times(1)).getMarkerGroup(123);
    }

    @Test
    public void testEditMarkerGroup() throws Exception {
        final String requestJson = "{\"markerGroupName\": \"test-marker-group-updated\", \"germplasmGroup\": \"test-germplasm-group\"}";
        when(projectService.getDefaultProjectEditor()).thenReturn("test-user");
        when(markerGroupService.updateMarkerGroup(eq(123), any(MarkerGroupDTO.class), eq("test-user")))
                .thenReturn(new MarkerGroupDTO());

        mockMvc.perform(MockMvcRequestBuilders.patch("/gobii-dev/gobii/v3/markergroups/123").content(requestJson)
                .contentType(MediaType.APPLICATION_JSON).contextPath("/gobii-dev")).andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
        verify(markerGroupService, times(1)).updateMarkerGroup(eq(123), any(MarkerGroupDTO.class), eq("test-user"));

    }

    @Test
    public void testDeleteMarkerGroup() throws Exception {
        doNothing().when(markerGroupService).deleteMarkerGroup(123);

        mockMvc.perform(MockMvcRequestBuilders.delete("/gobii-dev/gobii/v3/markergroups/123").contextPath("/gobii-dev"))
                .andDo(print()).andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(markerGroupService, times(1)).deleteMarkerGroup(123);
    }

    @Test
    public void testAssignMarkersToMarkerGroup() throws Exception {
        final String requestJson = "[{\"markerName\": \"foo marker\", \"platformName\": \"foo platform\", \"favorableAlleles\": [\"A\"]},{\"markerName\": \"bar marker\",\"platformName\": \"bar platform\", \"favorableAlleles\": [\"G\"]}]";
        when(projectService.getDefaultProjectEditor()).thenReturn("test-user");
        when(markerGroupService.mapMarkers(eq(123), anyListOf(MarkerDTO.class), eq("test-user")))
                .thenReturn(new PagedResult<MarkerDTO>());
        mockMvc.perform(MockMvcRequestBuilders.post("/gobii-dev/gobii/v3/markergroups/123/markerscollection")
                .content(requestJson).contentType(MediaType.APPLICATION_JSON).contextPath("/gobii-dev")).andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(markerGroupService, times(1)).mapMarkers(eq(123), anyListOf(MarkerDTO.class), eq("test-user"));
    }

    @Test
    public void testListMarkersInMarkerGroups() throws Exception {
        when(markerGroupService.getMarkerGroupMarkers(123, 0, 1000)).thenReturn(new PagedResult<>());
        mockMvc.perform(
                MockMvcRequestBuilders.get("/gobii-dev/gobii/v3/markergroups/123/markers").contextPath("/gobii-dev"))
                .andDo(print()).andExpect(MockMvcResultMatchers.status().isOk());
        verify(markerGroupService, times(1)).getMarkerGroupMarkers(123, 0, 1000);
    }

    @Test
    public void listCvGroups() throws Exception {
        when(cvService.getCvGroups(0, 1000)).thenReturn(new PagedResult<>());
        mockMvc.perform(MockMvcRequestBuilders.get("/gobii-dev/gobii/v3/cvs/groups").contextPath("/gobii-dev"))
                .andDo(print()).andExpect(MockMvcResultMatchers.status().isOk());
        verify(cvService, times(1)).getCvGroups(0, 1000);
    }
}