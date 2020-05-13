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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.gobiiproject.gobidomain.services.gdmv3.AnalysisService;
import org.gobiiproject.gobidomain.services.gdmv3.ContactService;
import org.gobiiproject.gobidomain.services.gdmv3.DatasetService;
import org.gobiiproject.gobidomain.services.gdmv3.ExperimentService;
import org.gobiiproject.gobidomain.services.gdmv3.MapsetService;
import org.gobiiproject.gobidomain.services.gdmv3.OrganizationService;
import org.gobiiproject.gobidomain.services.gdmv3.ProjectService;
import org.gobiiproject.gobidomain.services.gdmv3.ReferenceService;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.auditable.GobiiProjectDTO;
import org.gobiiproject.gobiimodel.dto.children.CvPropertyDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.AnalysisDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.AnalysisTypeDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.ContactDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.DatasetDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.DatasetRequestDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.DatasetTypeDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.ExperimentDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.MapsetDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.MapsetTypeDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.OrganizationDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.ReferenceDTO;
import org.gobiiproject.gobiimodel.dto.request.AnalysisTypeRequest;
import org.gobiiproject.gobiimodel.dto.request.ExperimentPatchRequest;
import org.gobiiproject.gobiimodel.dto.request.ExperimentRequest;
import org.gobiiproject.gobiimodel.dto.request.GobiiProjectPatchDTO;
import org.gobiiproject.gobiimodel.dto.request.GobiiProjectRequestDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.hamcrest.core.StringContains;
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

import lombok.extern.slf4j.Slf4j;

@ActiveProfiles("projectsController-test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
    classes = GOBIIControllerV3TestConfiguration.class
  //locations = { "classpath:/spring/application-config.xml" }
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
    private OrganizationService organizationService;

    @InjectMocks
    private GOBIIControllerV3 gobiiControllerV3;

    private MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
        log.info("Setting up Gobii V3 Controller test");
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders
            .standaloneSetup(gobiiControllerV3)
            .setControllerAdvice(new GlobalControllerExceptionHandler())
            .build();

        //assert this.projectsController.getProjectService() != null.

    }

    private GobiiProjectDTO createMockProjectDTO() {
        GobiiProjectDTO dto = new GobiiProjectDTO();
        dto.setId(1);
        dto.setModifiedBy(1);
        dto.setProjectName("test-project");
        

        List<CvPropertyDTO> propDtoList = createMockPropDTOList();
        dto.setProperties(propDtoList);
        return dto;
    }

    private CvPropertyDTO createMockPropDTO() {
        //mock DTO
        CvPropertyDTO propDto = new CvPropertyDTO();
        propDto.setPropertyId(1);
        propDto.setPropertyName("test-prop");
        propDto.setPropertyGroupId(1);
        propDto.setPropertyGroupName("test-group");
        propDto.setPropertyGroupType(1);
        return propDto;
    }

    private List<CvPropertyDTO> createMockPropDTOList() {
        List<CvPropertyDTO> propDtoList = new java.util.ArrayList<>();
        propDtoList.add( createMockPropDTO() );
        return propDtoList;
    }

    @Test
    public void simpleListTest() throws Exception {
        assert projectService != null;
        List<GobiiProjectDTO> mockList = new ArrayList<GobiiProjectDTO>();
        GobiiProjectDTO mockItem = createMockProjectDTO();
        mockList.add(mockItem);
        PagedResult<GobiiProjectDTO> mockPayload = new PagedResult<>();
        mockPayload.setResult(mockList);
        mockPayload.setCurrentPageNum(0);
        mockPayload.setCurrentPageSize(1);
        when(
            projectService.getProjects(0, 1000, null)
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
        .andExpect(jsonPath("$.result.data[0].properties[0].propertyType").value("system defined"))
        ;
        verify(projectService, times(1)).getProjects(0, 1000, null);
    }

    @Test
    public void listWithQueryTest() throws Exception {
        assert projectService != null;
        List<GobiiProjectDTO> mockList = new ArrayList<GobiiProjectDTO>();
        GobiiProjectDTO mockItem = createMockProjectDTO();
        mockList.add(mockItem);
        PagedResult<GobiiProjectDTO> mockPayload = new PagedResult<>();
        mockPayload.setResult(mockList);
        mockPayload.setCurrentPageNum(0);
        mockPayload.setCurrentPageSize(1);
        when(
            projectService.getProjects(1, 100, 2)
        ).thenReturn(
            mockPayload
        );

        mockMvc.perform(
            MockMvcRequestBuilders
            .get("/gobii-dev/gobii/v3/projects?page=1&pageSize=100&piContactId=2")
            .contextPath("/gobii-dev")
        )
        .andDo(print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.metadata.pagination.currentPage").value(0))
        .andExpect(jsonPath("$.metadata.pagination.pageSize").value(1))
        .andExpect(jsonPath("$.result.data[0].projectId").value(mockItem.getProjectId()))
        .andExpect(jsonPath("$.result.data[0].projectName").value(mockItem.getProjectName()))
        .andExpect(jsonPath("$.result.data[0].properties[0].propertyType").value("system defined"))
        ;
        verify(projectService, times(1)).getProjects(1, 100, 2);
    }
    
    @Test
    public void testCreateSimple() throws Exception {
        GobiiProjectRequestDTO mockRequest = new GobiiProjectRequestDTO();
        mockRequest.setPiContactId("1"); //need to mock contact here
        mockRequest.setProjectName("Test project");
        mockRequest.setProjectDescription("Test description");
        //this test does not include properties
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(mockRequest);

        GobiiProjectDTO mockGobiiProject = new GobiiProjectDTO();
        //let's leave it empty since it's a mock anyways
        doReturn("gadm").when(projectService).getDefaultProjectEditor();
        
		when(
            projectService.createProject( any(GobiiProjectRequestDTO.class), eq("gadm"))
        ).thenReturn(
            mockGobiiProject
        );

        mockMvc.perform(
            MockMvcRequestBuilders
            .post("/gobii-dev/gobii/v3/projects")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson)
            .contextPath("/gobii-dev")
        )
        .andDo(print())
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.metadata").doesNotExist())
        ;
    }

    @Test
    public void testCreateWithProperties() throws Exception {
        String requestJson = "{\"piContactId\" : 4,\"projectName\" : \"test\", \"projectDescription\" : \"Test description\"," +
            "\"properties\" : [ {\"propertyId\" : 4,  \"propertyValue\" : \"test-value\"} ]}";

        GobiiProjectDTO mockGobiiProject = new GobiiProjectDTO();
        //let's leave it empty since it's a mock anyways
        doReturn("gadm").when(projectService).getDefaultProjectEditor();
        when(
            projectService.createProject( any(GobiiProjectRequestDTO.class), eq("gadm") )
        ).thenReturn(
            mockGobiiProject
        );

        

        mockMvc.perform(
            MockMvcRequestBuilders
            .post("/gobii-dev/gobii/v3/projects")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson)
            .contextPath("/gobii-dev")
        )
        .andDo(print())
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.metadata").doesNotExist())
        ;
    }

    @Test
    public void testCreateWithPropertiesValidationError() throws Exception {
        String requestJson = "{\"piContactId\" : null,\"projectName\" : null, \"projectDescription\" : \"Test description\"," +
            "\"properties\" : [ {\"propertyId\" : 4, \"propertyName\" : null, \"propertyValue\" : \"test-value\"} ]}";

        mockMvc.perform(
            MockMvcRequestBuilders
            .post("/gobii-dev/gobii/v3/projects")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson)
            .contextPath("/gobii-dev")
        )
        .andDo(print())
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.error").value(StringContains.containsString("piContactId must not be empty")))
        .andExpect(jsonPath("$.error").value(StringContains.containsString("projectName must not be empty")))
        ;
    }

    @Test
    public void testPatchWithProperties() throws Exception {
        String requestJson = "{\"piContactId\" : \"4\",\"projectName\" : \"test\", \"projectDescription\" : \"Test description\"," +
            "\"properties\" : [ {\"propertyId\" : \"4\",  \"propertyValue\" : \"test-value\"} ]}";

        GobiiProjectDTO mockGobiiProject = new GobiiProjectDTO();
        //let's leave it empty since it's a mock anyways
        doReturn("gadm").when(projectService).getDefaultProjectEditor();
        when(
            projectService.patchProject( eq(84), any(GobiiProjectPatchDTO.class), eq("gadm") )
        ).thenReturn(
            mockGobiiProject
        );

        
        mockMvc.perform(
            MockMvcRequestBuilders
            .patch("/gobii-dev/gobii/v3/projects/84")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson)
            .contextPath("/gobii-dev")
        )
        .andDo(print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.metadata").doesNotExist())
        ;
    }

    //TEsts for Project Properties
    @Test
    public void testListProjectProperties() throws Exception {

        List<CvPropertyDTO> mockList = createMockPropDTOList();
        PagedResult<CvPropertyDTO> mockPayload = new PagedResult<>();
        mockPayload.setResult(mockList);
        mockPayload.setCurrentPageNum(0);
        mockPayload.setCurrentPageSize(1);
        when(
            projectService.getProjectProperties(0, 1000)
        ).thenReturn(
            mockPayload
        );

        mockMvc.perform(
            MockMvcRequestBuilders
            .get("/gobii-dev/gobii/v3/projects/properties")
            .contextPath("/gobii-dev")
        )
        .andDo(print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.metadata.pagination.currentPage").value(0))
        .andExpect(jsonPath("$.metadata.pagination.pageSize").value(1))
        .andExpect(jsonPath("$.result.data[0].propertyId").value(1))
        .andExpect(jsonPath("$.result.data[0].propertyName").value("test-prop"))
        .andExpect(jsonPath("$.result.data[0].propertyType").value("system defined"))
        ;
        verify(projectService, times(1)).getProjectProperties(0, 1000);
    }

    @Test
    public void testGetProject() throws Exception {

        GobiiProjectDTO mockGobiiProject = new GobiiProjectDTO(); //let's leave it empty since it's a mock anyways
        when(
            projectService.getProject( eq(123) )
        ).thenReturn(
            mockGobiiProject
        );

        
        mockMvc.perform(
            MockMvcRequestBuilders
            .get("/gobii-dev/gobii/v3/projects/123")
            .contextPath("/gobii-dev")
        )
        .andDo(print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.metadata").doesNotExist())
        ;
        verify(projectService, times(1)).getProject(123);
    }

    @Test
    public void testGetProjectNotFound() throws Exception {

        when(
            projectService.getProject( eq(123) )
        ).thenReturn(
            null
        );
     
        mockMvc.perform(
            MockMvcRequestBuilders
            .get("/gobii-dev/gobii/v3/projects/123")
            .contextPath("/gobii-dev")
        )
        .andDo(print())
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        ;
        verify(projectService, times(1)).getProject(123);
    }

    @Test
    public void testDelete() throws Exception {
        
        doNothing().when(
            projectService
        ).deleteProject(eq(84));
       
        
        mockMvc.perform(
            MockMvcRequestBuilders
            .delete("/gobii-dev/gobii/v3/projects/84")
            .contextPath("/gobii-dev")
        )
        .andDo(print())
        .andExpect(MockMvcResultMatchers.status().is(204))
        ;

        verify(projectService, times(1)).deleteProject(eq(84));
    }

    @Test
    public void testDelete404() throws Exception {
        Exception exc = new NullPointerException("test");
        doThrow(
            exc
        ).when(
            projectService
        ).deleteProject(eq(84));
       
        
        mockMvc.perform(
            MockMvcRequestBuilders
            .delete("/gobii-dev/gobii/v3/projects/84")
            .contextPath("/gobii-dev")
        )
        .andDo(print())
        .andExpect(MockMvcResultMatchers.status().is(404))
        .andExpect(jsonPath("$.error").value(StringContains.containsString("Resource not found")))
        ;

        verify(projectService, times(1)).deleteProject(eq(84));
    }

    @Test
    public void testDelete409() throws Exception {
        Exception exc = new GobiiException(GobiiStatusLevel.ERROR, GobiiValidationStatusType.FOREIGN_KEY_VIOLATION, "test");
        doThrow(
            exc
        ).when(
            projectService
        ).deleteProject(eq(84));
       
        
        mockMvc.perform(
            MockMvcRequestBuilders
            .delete("/gobii-dev/gobii/v3/projects/84")
            .contextPath("/gobii-dev")
        )
        .andDo(print())
        .andExpect(MockMvcResultMatchers.status().is(409))
        .andExpect(jsonPath("$.error").value(StringContains.containsString("test")))
        ;

        verify(projectService, times(1)).deleteProject(eq(84));
    }

    @Test
    public void testGetContacts() throws Exception {
        assert contactService != null;
        List<ContactDTO> mockList = new ArrayList<ContactDTO>();
        ContactDTO mockItem = new ContactDTO();
        mockItem.setPiContactId(111);
        mockItem.setPiContactFirstName("test");
        mockList.add(mockItem);
        PagedResult<ContactDTO> mockPayload = new PagedResult<>();
        mockPayload.setResult(mockList);
        mockPayload.setCurrentPageNum(0);
        mockPayload.setCurrentPageSize(1);
        when(
            contactService.getContacts(0, 1000, null)
        ).thenReturn(
            mockPayload
        );

        mockMvc.perform(
            MockMvcRequestBuilders
            .get("/gobii-dev/gobii/v3/contacts")
            .contextPath("/gobii-dev")
        )
        .andDo(print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.metadata.pagination.currentPage").value(0))
        .andExpect(jsonPath("$.metadata.pagination.pageSize").value(1))
        .andExpect(jsonPath("$.result.data[0].piContactId").value(mockItem.getPiContactId()))
        .andExpect(jsonPath("$.result.data[0].piContactFirstName").value(mockItem.getPiContactFirstName()))
        ;
        verify(contactService, times(1)).getContacts(0, 1000, null);
    }

    @Test
    public void testGetExperimentsSimple() throws Exception {
        assert experimentService != null;
        List<ExperimentDTO> mockList = new ArrayList<>();
        ExperimentDTO mockItem = new ExperimentDTO();

        mockList.add(mockItem);
        PagedResult<ExperimentDTO> mockPayload = new PagedResult<>();
        mockPayload.setResult(mockList);
        mockPayload.setCurrentPageNum(0);
        mockPayload.setCurrentPageSize(1);
        when(
            experimentService.getExperiments(0, 1000, null)
        ).thenReturn(
            mockPayload
        );

        mockMvc.perform(
            MockMvcRequestBuilders
            .get("/gobii-dev/gobii/v3/experiments")
            .contextPath("/gobii-dev")
        )
        .andDo(print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.metadata.pagination.currentPage").value(0))
        .andExpect(jsonPath("$.metadata.pagination.pageSize").value(1))
        .andExpect(jsonPath("$.result.data[0].experimentId").value(mockItem.getExperimentId()))
        ;
        verify(experimentService, times(1)).getExperiments(0, 1000, null);
    }

    @Test
    public void testGetExperimentById() throws Exception {
        ExperimentDTO mockItem = new ExperimentDTO();
        when(
            experimentService.getExperiment(123)
        ).thenReturn(
            mockItem
        );
        mockMvc.perform(
            MockMvcRequestBuilders
            .get("/gobii-dev/gobii/v3/experiments/123")
            .contextPath("/gobii-dev")
        )
        .andDo(print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.result.experimentName").hasJsonPath())
        ;
        verify(experimentService, times(1)).getExperiment(123);

    }

    @Test
    public void testGetExperimentById404() throws Exception {
        when(
            experimentService.getExperiment(123)
        ).thenThrow(
            new NullPointerException()
        );
        mockMvc.perform(
            MockMvcRequestBuilders
            .get("/gobii-dev/gobii/v3/experiments/123")
            .contextPath("/gobii-dev")
        )
        .andDo(print())
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        ;
        verify(experimentService, times(1)).getExperiment(123);

    }

    @Test
    public void testCreateExperimentsSimple() throws Exception {
        String jsonRequest = "{\"projectId\" : \"7\", \"experimentName\" : \"fooExperiment\", \"vendorProtocolId\" : \"4\"}";
        when(
            experimentService.createExperiment( any( ExperimentRequest.class), eq("test-user" ))
        ).thenReturn(
            new ExperimentDTO()
        );
        when(
            projectService.getDefaultProjectEditor() //TODO: Refactor where this editor info is called
        ).thenReturn("test-user");

        mockMvc.perform(
            MockMvcRequestBuilders
            .post("/gobii-dev/gobii/v3/experiments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonRequest)
            .contextPath("/gobii-dev")
        )
        .andDo(print())
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.metadata").doesNotExist())
        ;
        verify(experimentService, times(1)).createExperiment( any( ExperimentRequest.class ), eq("test-user"));
    }

    @Test
    public void testUpdateExperimentSimple() throws Exception {
        String jsonRequest = "{\"projectId\" : \"7\", \"experimentName\" : \"fooExperiment\", \"vendorProtocolId\" : \"4\"}";
        when(
            experimentService.updateExperiment(eq(123),  any( ExperimentPatchRequest.class), eq("test-user"))
        ).thenReturn(
            new ExperimentDTO()
        );
        when(
            projectService.getDefaultProjectEditor() //TODO: replace this with AuthenticationService
        ).thenReturn("test-user");

        mockMvc.perform(
            MockMvcRequestBuilders
            .patch("/gobii-dev/gobii/v3/experiments/123")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonRequest)
            .contextPath("/gobii-dev")
        )
        .andDo(print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        ;
        verify(experimentService, times(1)).updateExperiment(eq(123),  any( ExperimentPatchRequest.class ), eq("test-user"));
    }

    @Test
    public void testDeleteExperiment() throws Exception {
        
        doNothing().when(experimentService).deleteExperiment(eq(123));

        mockMvc.perform(
            MockMvcRequestBuilders
            .delete("/gobii-dev/gobii/v3/experiments/123")
            .contextPath("/gobii-dev")
        )
        .andDo(print())
        .andExpect(MockMvcResultMatchers.status().isNoContent())
        ;
        verify(experimentService, times(1)).deleteExperiment(eq(123));
    }

    @Test
    public void testDeleteExperimentException409() throws Exception {
        Exception exc = new GobiiException(GobiiStatusLevel.ERROR, GobiiValidationStatusType.FOREIGN_KEY_VIOLATION, "test");
        doThrow(exc).when(experimentService).deleteExperiment(eq(123));

        mockMvc.perform(
            MockMvcRequestBuilders
            .delete("/gobii-dev/gobii/v3/experiments/123")
            .contextPath("/gobii-dev")
        )
        .andDo(print())
        .andExpect(MockMvcResultMatchers.status().is(409))
        .andExpect(jsonPath("$.error").value(StringContains.containsString("test")))
        ;
        verify(experimentService, times(1)).deleteExperiment(eq(123));
    }

    @Test
    public void testListAnalysis() throws Exception {
        AnalysisDTO mockAnalysisDTO  = new AnalysisDTO();
        List<AnalysisDTO> mockList = new ArrayList<>();
        mockList.add(mockAnalysisDTO);
        PagedResult<AnalysisDTO> mockPayload = new PagedResult<>();
        mockPayload.setResult(mockList);
        mockPayload.setCurrentPageNum(0);
        mockPayload.setCurrentPageSize(1);

        when(
            analysisService.getAnalyses(eq(0), eq(1000))
        ).thenReturn(
            mockPayload
        );

        mockMvc.perform(
            MockMvcRequestBuilders
            .get("/gobii-dev/gobii/v3/analyses")
            .contextPath("/gobii-dev")
        )
        .andDo(print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        ;
        verify(analysisService, times(1)).getAnalyses(eq(0), eq(1000));
    }
    
    @Test
    public void testCreateAnalysisSimple() throws Exception {
        
        String jsonRequest = "{\"analysisName\": \"test-name\", \"analysisTypeId\": \"93\" }";
        when(
            analysisService.createAnalysis(any(AnalysisDTO.class), any(String.class))
        ).thenReturn(
            new AnalysisDTO()
        );

        when(
            projectService.getDefaultProjectEditor() //TODO: Refactor where this editor info is called
        ).thenReturn("test-user");

        mockMvc.perform(
            MockMvcRequestBuilders
            .post("/gobii-dev/gobii/v3/analyses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonRequest)
            .contextPath("/gobii-dev")
        )
        .andDo(print())
        .andExpect(MockMvcResultMatchers.status().isCreated())
        ;
        verify(analysisService, times(1)).createAnalysis(any(AnalysisDTO.class), eq("test-user"));
    }

    @Test
    public void testCreateAnalysisType() throws Exception {
        String jsonRequest = "{\"analysisTypeName\" : \"testType\"}";

        when(
            analysisService.createAnalysisType(any(AnalysisTypeRequest.class), any(String.class))
        ).thenReturn(
            new AnalysisTypeDTO()
        );

        when(
            projectService.getDefaultProjectEditor() //TODO: Refactor where this editor info is called
        ).thenReturn("test-user");

        mockMvc.perform(
            MockMvcRequestBuilders
            .post("/gobii-dev/gobii/v3/analyses/types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonRequest)
            .contextPath("/gobii-dev")
        )
        .andDo(print())
        .andExpect(MockMvcResultMatchers.status().isCreated())
        ;
        
    }

    @Test
    public void testListAnalysisTypes() throws Exception {
        List<AnalysisTypeDTO> analysisTypes = new ArrayList<>();
        Integer page = 0;
        Integer pageSize = 1000;
        PagedResult<AnalysisTypeDTO> result = new PagedResult<>();
        result.setResult(analysisTypes);

        when(
            analysisService.getAnalysisTypes(page, pageSize)
        ).thenReturn(
            result
        );

        mockMvc.perform(
            MockMvcRequestBuilders
            .get("/gobii-dev/gobii/v3/analyses/types")
            .contextPath("/gobii-dev")
        )
        .andDo(print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        ;

        verify(analysisService, times(1)).getAnalysisTypes(0, 1000);
    }


    @Test
    public void testPatchAnalysisById() throws Exception {

        AnalysisDTO patchData = new AnalysisDTO();
        patchData.setAnalysisName("New Name");
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(patchData);
        Integer testId = 123;

        AnalysisDTO mockDTO = new AnalysisDTO();

        when(
            analysisService.updateAnalysis(eq(testId), any(AnalysisDTO.class), eq("test-user"))
        ).thenReturn(
            mockDTO
        );

        when(
            projectService.getDefaultProjectEditor() //TODO: Refactor where this editor info is called
        ).thenReturn("test-user");

        mockMvc.perform(
            MockMvcRequestBuilders
            .patch("/gobii-dev/gobii/v3/analyses/123")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson)
            .contextPath("/gobii-dev")
        )
        .andDo(print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        ;

        verify(analysisService, times(1)).updateAnalysis(eq(testId), any(AnalysisDTO.class), eq("test-user"));
        verify(projectService, times(1)).getDefaultProjectEditor();
    }

    @Test
    public void getAnalysisByIdTest() throws Exception {
        AnalysisDTO mockDTO = new AnalysisDTO();
        Integer mockId = 123;
        when(
            analysisService.getAnalysis(mockId)
        ).thenReturn(
            mockDTO
        );

        mockMvc.perform(
            MockMvcRequestBuilders
            .get("/gobii-dev/gobii/v3/analyses/123")
            .contextPath("/gobii-dev")
        )
        .andDo(print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        ;

        verify(analysisService, times(1)).getAnalysis(eq(123));

    }

    @Test
    public void deleteAnalysisByIdTest() throws Exception {
        Integer analysisId = 123;
        doNothing().when(analysisService).deleteAnalysis(analysisId);

        mockMvc.perform(
            MockMvcRequestBuilders
            .delete("/gobii-dev/gobii/v3/analyses/123")
            .contextPath("/gobii-dev")
        )
        .andDo(print())
        .andExpect(MockMvcResultMatchers.status().isNoContent());
        ;
        verify(analysisService, times(1)).deleteAnalysis(123);

      
    }

    @Test 
    public void createDatasetSimpleTest() throws Exception {
        assert datasetService != null;
        DatasetRequestDTO request = new DatasetRequestDTO();
        request.setDatasetName("test-name");
        request.setExperimentId(1);
        request.setCallingAnalysisId(1);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(request);

        when(
            projectService.getDefaultProjectEditor()
        ).thenReturn(
            "test-user"
        );
        when(
            datasetService.createDataset(any(DatasetRequestDTO.class), eq("test-user"))
        ).thenReturn(
            new DatasetDTO()
        );
        
        mockMvc.perform(
            MockMvcRequestBuilders
            .post("/gobii-dev/gobii/v3/datasets")
            .contextPath("/gobii-dev")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson)
        )
        .andDo(print())
        .andExpect(MockMvcResultMatchers.status().isCreated())
        ;
        verify( datasetService, times(1)).createDataset(any(DatasetRequestDTO.class), eq("test-user"));


    }

    @Test
    public void testCreateDatasetWithAnalysisIds() throws Exception {
        assert datasetService != null;
        String requestJson = "{\"datasetName\" : \"test-name\", \"experimentId\" : \"1\", \"callingAnalysisId\" : \"1\", \"analysisIds\" : [ \"2\", \"3\", \"4\" ]}";

        when(
            projectService.getDefaultProjectEditor()
        ).thenReturn(
            "test-user"
        );

        when(
            datasetService.createDataset(any(DatasetRequestDTO.class), eq("test-user"))
        ).thenReturn(
            new DatasetDTO()
        );
        
        mockMvc.perform(
            MockMvcRequestBuilders
            .post("/gobii-dev/gobii/v3/datasets")
            .contextPath("/gobii-dev")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson)
            .characterEncoding("UTF-8")
        )
        .andDo(print())
        .andExpect(MockMvcResultMatchers.status().isCreated())
        ;

        verify( datasetService, times(1)).createDataset(any(DatasetRequestDTO.class), eq("test-user"));
    }


    @Test
    public void testDatasetListing() throws Exception {
        when(
            datasetService.getDatasets(0, 1000, null, null)
        ).thenReturn(
            new PagedResult<DatasetDTO>()
        );


        mockMvc.perform(
            MockMvcRequestBuilders
            .get("/gobii-dev/gobii/v3/datasets")
            .contextPath("/gobii-dev")
        )
        .andDo(print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        ;

        verify(datasetService, times(1)).getDatasets(0, 1000, null, null);

    }


    @Test
    public void testDatasetListingWithQuery() throws Exception {
        when(
            datasetService.getDatasets(5, 100, 1, 2)
        ).thenReturn(
            new PagedResult<DatasetDTO>()
        );


        mockMvc.perform(
            MockMvcRequestBuilders
            .get("/gobii-dev/gobii/v3/datasets?page=5&pageSize=100&experimentId=1&datasetTypeId=2")
            .contextPath("/gobii-dev")
        )
        .andDo(print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        ;

        verify(datasetService, times(1)).getDatasets(5, 100, 1, 2);

    }


    @Test
    public void testDatasetGetById() throws Exception {
        Integer target = 112;
        when(
            datasetService.getDataset(target)
        ).thenReturn(
            new DatasetDTO()
        );

        mockMvc.perform(
            MockMvcRequestBuilders
            .get("/gobii-dev/gobii/v3/datasets/112")
            .contextPath("/gobii-dev")
        )
        .andDo(print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        ;

        verify(datasetService, times(1)).getDataset(target);
    }

    @Test
    public void testDatasetByIdUpdate() throws Exception {
        Integer target = 112;
        when(
            datasetService.updateDataset(
                eq(target),
                any(DatasetRequestDTO.class),
                eq("test-user")
            )
        ).thenReturn(
            new DatasetDTO()
        );

        when(
            projectService.getDefaultProjectEditor()
        ).thenReturn("test-user");

        String requestJson = "{\"datasetName\" : \"test-name-edited\", \"experimentId\" : \"1\", \"callingAnalysisId\" : \"1\", \"analysisIds\" : [ \"2\", \"3\", \"4\" ]}";

        mockMvc.perform(
            MockMvcRequestBuilders
            .patch("/gobii-dev/gobii/v3/datasets/112")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson)
            .contextPath("/gobii-dev")
        )
        .andDo(print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        ;
    }

    @Test
    public void testDatasetDeleteById() throws Exception {
        Integer datasetId = 112;
        doNothing().when(datasetService).deleteDataset(datasetId);

        mockMvc.perform(
            MockMvcRequestBuilders
            .delete("/gobii-dev/gobii/v3/datasets/112")
            .contextPath("/gobii-dev")
        )
        .andDo(print())
        .andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(datasetService, times(1)).deleteDataset(datasetId);
    }

    @Test
    public void testListDatasetTypes() throws Exception {

        when(
            datasetService.getDatasetTypes(0, 1000)
        ).thenReturn(
            new PagedResult<DatasetTypeDTO>()
        );

        mockMvc.perform(
            MockMvcRequestBuilders
            .get("/gobii-dev/gobii/v3/datasets/types")
            .contextPath("/gobii-dev")
        )
        .andDo(print())
        .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testAddDatasetType() throws Exception {
        when(
            datasetService.createDatasetType("test-name", "datasetTypeDescription", "user")
        ).thenReturn(
            new DatasetTypeDTO()
        );

        when(
            projectService.getDefaultProjectEditor()
        ).thenReturn("user");


        String requestJson = "{\"datatsetTypeName\" : \"test-name\", \"datasetTypeDescription\": \"datasetTypeDescription\"}";
        mockMvc.perform(
            MockMvcRequestBuilders
            .post("/gobii-dev/gobii/v3/datasets/types")
            .contextPath("/gobii-dev")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson)
        )
        .andDo(print())
        .andExpect(MockMvcResultMatchers.status().isCreated())
        ;
    }


    @Test
    public void testGetReferences() throws Exception {
        when(
            referenceService.getReferences(0, 1000)
        ).thenReturn(
            new PagedResult<ReferenceDTO>()
        );
        mockMvc.perform(
            MockMvcRequestBuilders
            .get("/gobii-dev/gobii/v3/references")
            .contextPath("/gobii-dev")
        )
        .andDo(print())
        .andExpect(MockMvcResultMatchers.status().isOk());

        verify(referenceService, times(1)).getReferences(0, 1000);

    }

    @Test
    public void testGetMapsets() throws Exception {
        when(
            mapsetService.getMapsets(0, 1000, null)
        ).thenReturn(
            new PagedResult<MapsetDTO>()
        );
        mockMvc.perform(
            MockMvcRequestBuilders
            .get("/gobii-dev/gobii/v3/mapsets")
            .contextPath("/gobii-dev")
        )
        .andDo(print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        ;

        verify(mapsetService, times(1)).getMapsets(0, 1000, null);
    }

    @Test
    public void testCreateSimpleMapset() throws Exception {
        String requestJson = "{\"mapsetName\": \"test-mapset\", \"mapsetDescription\": \"test-description\", \"mapsetTypeId\": \"10\", \"referenceId\": \"12\"}";

        when(
            projectService.getDefaultProjectEditor()
        ).thenReturn("test-user");

        when(
            mapsetService.createMapset(any(MapsetDTO.class), eq("test-user"))
        ).thenReturn(
            new MapsetDTO()
        );

        mockMvc.perform(
            MockMvcRequestBuilders
            .post("/gobii-dev/gobii/v3/mapsets")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson)
            .contextPath("/gobii-dev")
        )
        .andDo(print())
        .andExpect(MockMvcResultMatchers.status().isCreated())
        ;

        verify(mapsetService, times(1)).createMapset(any(MapsetDTO.class), eq("test-user"));
    }

    @Test
    public void testCreateSimpleMapsetMissingMapsetTypeId() throws Exception {
        String requestJson = "{\"mapsetName\": \"test-mapset\", \"mapsetDescription\": \"test-description\", \"referenceId\": \"12\"}";

        mockMvc.perform(
            MockMvcRequestBuilders
            .post("/gobii-dev/gobii/v3/mapsets")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson)
            .contextPath("/gobii-dev")
        )
        .andDo(print())
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        ;
    }

    @Test
    public void testGetMapsetById() throws Exception {
        when(
            mapsetService.getMapset(122)
        ).thenReturn(
            new MapsetDTO()
        );
        mockMvc.perform(
            MockMvcRequestBuilders
            .get("/gobii-dev/gobii/v3/mapsets/122")
            .contextPath("/gobii-dev")
        )
        .andDo(print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        ;
        verify(mapsetService, times(1)).getMapset(122);
    }

    @Test
    public void testGetMapsetByIdNotFound() throws Exception {
        when(
            mapsetService.getMapset(122)
        ).thenThrow(
            new GobiiException(GobiiStatusLevel.ERROR, GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST, "Mapset not found")
        );
        mockMvc.perform(
            MockMvcRequestBuilders
            .get("/gobii-dev/gobii/v3/mapsets/122")
            .contextPath("/gobii-dev")
        )
        .andDo(print())
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        ;
        verify(mapsetService, times(1)).getMapset(122);
    }

    @Test
    public void testPatchSimpleMapset() throws Exception {
        String requestJson = "{\"mapsetName\": \"test-mapset\", \"mapsetDescription\": \"test-description\"}";

        when(
            projectService.getDefaultProjectEditor()   
        ).thenReturn("test-user");

        when(
            mapsetService.updateMapset(eq(122), any(MapsetDTO.class), eq("test-user"))
        ).thenReturn(
            new MapsetDTO()
        );

        mockMvc.perform(
            MockMvcRequestBuilders
            .patch("/gobii-dev/gobii/v3/mapsets/122")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson)
            .contextPath("/gobii-dev")
        )
        .andDo(print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        ;

        verify(mapsetService, times(1)).updateMapset(eq(122), any(MapsetDTO.class), eq("test-user"));
    }


    @Test
    public void testDeleteMapset() throws Exception {
        Integer mapsetId = 122;
        doNothing().when(mapsetService).deleteMapset(mapsetId);

        mockMvc.perform(
            MockMvcRequestBuilders
            .delete("/gobii-dev/gobii/v3/mapsets/122")
            .contextPath("/gobii-dev")
        )
        .andDo(print())
        .andExpect(MockMvcResultMatchers.status().isNoContent())
        ;
        verify(mapsetService, times(1)).deleteMapset(122);
    }

    @Test
    public void createMapsetTypeTest() throws Exception {
        String requestJson = "{\"mapsetTypeName\": \"test-name\", \"mapsetTypeDescription\": \"test-desc\"}";
        when(
            mapsetService.createMapsetType("test-name", "test-desc", "user")
        ).thenReturn(
            new MapsetTypeDTO()
        );

        when(
            projectService.getDefaultProjectEditor()
        ).thenReturn("user");

        mockMvc.perform(
            MockMvcRequestBuilders
            .post("/gobii-dev/gobii/v3/mapsets/types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson)
            .contextPath("/gobii-dev")
        )
        .andDo(print())
        .andExpect(MockMvcResultMatchers.status().isCreated())
        ;

        verify(mapsetService, times(1)).createMapsetType("test-name", "test-desc", "user");
    }

    @Test
    public void getMapsetTypes() throws Exception {
        when(
            mapsetService.getMapsetTypes(0, 1000)
        ).thenReturn(
            new PagedResult<MapsetTypeDTO>()
        );

        mockMvc.perform(
            MockMvcRequestBuilders
            .get("/gobii-dev/gobii/v3/mapsets/types")
            .contextPath("/gobii-dev")
        )
        .andDo(print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        ;
        verify(mapsetService, times(1)).getMapsetTypes(0, 1000);
    }

    @Test
    public void getOrganizationsList() throws Exception {
        when(
            organizationService.getOrganizations(0, 1000)
        ).thenReturn(
            new PagedResult<OrganizationDTO>()
        );

        mockMvc.perform(
            MockMvcRequestBuilders
            .get("/gobii-dev/gobii/v3/organizations")
            .contextPath("/gobii-dev")
        )
        .andDo(print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        ;
        verify(organizationService, times(1)).getOrganizations(0, 1000);
    }

    @Test
    public void getOrganizationById() throws Exception {
        Integer organizationId = 122;
        when(
            organizationService.getOrganization(organizationId)
        ).thenReturn(
            new OrganizationDTO()
        );

        mockMvc.perform(
            MockMvcRequestBuilders
            .get("/gobii-dev/gobii/v3/organizations/122")
            .contextPath("/gobii-dev")
        )
        .andDo(print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        ;

        verify(organizationService, times(1)).getOrganization(122);
    }
    
}