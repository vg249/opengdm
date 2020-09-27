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

import org.gobiiproject.gobiidomain.services.gdmv3.*;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.config.Roles;
import org.gobiiproject.gobiimodel.dto.children.CvPropertyDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.AnalysisDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.ContactDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.CvDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.CvTypeDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.DatasetDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.DatasetRequestDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.ExperimentDTO;
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
import org.hamcrest.core.StringContains;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
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
        ProjectDTO dto = new ProjectDTO();
        dto.setId(1);
        dto.setModifiedBy(1);
        dto.setProjectName("test-project");

        List<CvPropertyDTO> propDtoList = createMockPropDTOList();
        dto.setProperties(propDtoList);
        return dto;
    }

    private CvPropertyDTO createMockPropDTO() {
        // mock DTO
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
        propDtoList.add(createMockPropDTO());
        return propDtoList;
    }


    @Test
    public void testListAnalysis() throws Exception {
        AnalysisDTO mockAnalysisDTO = new AnalysisDTO();
        List<AnalysisDTO> mockList = new ArrayList<>();
        mockList.add(mockAnalysisDTO);
        PagedResult<AnalysisDTO> mockPayload = new PagedResult<>();
        mockPayload.setResult(mockList);
        mockPayload.setCurrentPageNum(0);
        mockPayload.setCurrentPageSize(1);

        when(analysisService.getAnalyses(eq(0), eq(1000))).thenReturn(mockPayload);

        mockMvc.perform(MockMvcRequestBuilders.get("/gobii-dev/gobii/v3/analyses").contextPath("/gobii-dev"))
                .andDo(print()).andExpect(MockMvcResultMatchers.status().isOk());
        verify(analysisService, times(1)).getAnalyses(eq(0), eq(1000));
    }

    @Test
    public void testCreateAnalysisSimple() throws Exception {

        String jsonRequest = "{\"analysisName\": \"test-name\", \"analysisTypeId\": \"93\" }";
        when(analysisService.createAnalysis(any(AnalysisDTO.class), any(String.class))).thenReturn(new AnalysisDTO());

        when(projectService.getDefaultProjectEditor() // TODO: Refactor where this editor info is called
        ).thenReturn("test-user");

        mockMvc.perform(MockMvcRequestBuilders.post("/gobii-dev/gobii/v3/analyses")
                .contentType(MediaType.APPLICATION_JSON).content(jsonRequest).contextPath("/gobii-dev")).andDo(print())
                .andExpect(MockMvcResultMatchers.status().isCreated());
        verify(analysisService, times(1)).createAnalysis(any(AnalysisDTO.class), eq("test-user"));
    }

    @Test
    public void testCreateAnalysisType() throws Exception {
        String jsonRequest = "{\"analysisTypeName\" : \"testType\"}";

        when(analysisService.createAnalysisType(any(CvTypeDTO.class), any(String.class))).thenReturn(new CvTypeDTO());

        when(projectService.getDefaultProjectEditor() // TODO: Refactor where this editor info is called
        ).thenReturn("test-user");

        mockMvc.perform(MockMvcRequestBuilders.post("/gobii-dev/gobii/v3/analyses/types")
                .contentType(MediaType.APPLICATION_JSON).content(jsonRequest).contextPath("/gobii-dev")).andDo(print())
                .andExpect(MockMvcResultMatchers.status().isCreated());

    }

    @Test
    public void testListAnalysisTypes() throws Exception {
        List<CvTypeDTO> analysisTypes = new ArrayList<>();
        Integer page = 0;
        Integer pageSize = 1000;
        PagedResult<CvTypeDTO> result = new PagedResult<>();
        result.setResult(analysisTypes);

        when(analysisService.getAnalysisTypes(page, pageSize)).thenReturn(result);

        mockMvc.perform(MockMvcRequestBuilders.get("/gobii-dev/gobii/v3/analyses/types").contextPath("/gobii-dev"))
                .andDo(print()).andExpect(MockMvcResultMatchers.status().isOk());

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
        String requestJson = ow.writeValueAsString(patchData);
        Integer testId = 123;

        AnalysisDTO mockDTO = new AnalysisDTO();

        when(analysisService.updateAnalysis(eq(testId), any(AnalysisDTO.class), eq("test-user"))).thenReturn(mockDTO);

        when(projectService.getDefaultProjectEditor() // TODO: Refactor where this editor info is called
        ).thenReturn("test-user");

        mockMvc.perform(MockMvcRequestBuilders.patch("/gobii-dev/gobii/v3/analyses/123")
                .contentType(MediaType.APPLICATION_JSON).content(requestJson).contextPath("/gobii-dev")).andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(analysisService, times(1)).updateAnalysis(eq(testId), any(AnalysisDTO.class), eq("test-user"));
        verify(projectService, times(1)).getDefaultProjectEditor();
    }

    @Test
    public void getAnalysisByIdTest() throws Exception {
        AnalysisDTO mockDTO = new AnalysisDTO();
        Integer mockId = 123;
        when(analysisService.getAnalysis(mockId)).thenReturn(mockDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/gobii-dev/gobii/v3/analyses/123").contextPath("/gobii-dev"))
                .andDo(print()).andExpect(MockMvcResultMatchers.status().isOk());

        verify(analysisService, times(1)).getAnalysis(eq(123));

    }

    @Test
    public void deleteAnalysisByIdTest() throws Exception {
        Integer analysisId = 123;
        doNothing().when(analysisService).deleteAnalysis(analysisId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/gobii-dev/gobii/v3/analyses/123").contextPath("/gobii-dev"))
                .andDo(print()).andExpect(MockMvcResultMatchers.status().isNoContent());
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
        String requestJson = ow.writeValueAsString(request);

        when(projectService.getDefaultProjectEditor()).thenReturn("test-user");
        when(datasetService.createDataset(any(DatasetRequestDTO.class), eq("test-user"))).thenReturn(new DatasetDTO());

        mockMvc.perform(MockMvcRequestBuilders.post("/gobii-dev/gobii/v3/datasets").contextPath("/gobii-dev")
                .contentType(MediaType.APPLICATION_JSON).content(requestJson)).andDo(print())
                .andExpect(MockMvcResultMatchers.status().isCreated());
        verify(datasetService, times(1)).createDataset(any(DatasetRequestDTO.class), eq("test-user"));

    }

    @Test
    public void testCreateDatasetWithAnalysisIds() throws Exception {
        assert datasetService != null;
        String requestJson = "{\"datasetName\" : \"test-name\", \"experimentId\" : \"1\", \"callingAnalysisId\" : \"1\", \"analysisIds\" : [ \"2\", \"3\", \"4\" ]}";

        when(projectService.getDefaultProjectEditor()).thenReturn("test-user");

        when(datasetService.createDataset(any(DatasetRequestDTO.class), eq("test-user"))).thenReturn(new DatasetDTO());

        mockMvc.perform(MockMvcRequestBuilders.post("/gobii-dev/gobii/v3/datasets").contextPath("/gobii-dev")
                .contentType(MediaType.APPLICATION_JSON).content(requestJson).characterEncoding("UTF-8")).andDo(print())
                .andExpect(MockMvcResultMatchers.status().isCreated());

        verify(datasetService, times(1)).createDataset(any(DatasetRequestDTO.class), eq("test-user"));
    }

    @Test
    public void testDatasetListing() throws Exception {
        when(datasetService.getDatasets(0, 1000, null, null)).thenReturn(new PagedResult<DatasetDTO>());

        mockMvc.perform(MockMvcRequestBuilders.get("/gobii-dev/gobii/v3/datasets").contextPath("/gobii-dev"))
                .andDo(print()).andExpect(MockMvcResultMatchers.status().isOk());

        verify(datasetService, times(1)).getDatasets(0, 1000, null, null);

    }

    @Test
    public void testDatasetListingWithQuery() throws Exception {
        when(datasetService.getDatasets(5, 100, 1, 2)).thenReturn(new PagedResult<DatasetDTO>());

        mockMvc.perform(MockMvcRequestBuilders
                .get("/gobii-dev/gobii/v3/datasets?page=5&pageSize=100&experimentId=1&datasetTypeId=2")
                .contextPath("/gobii-dev")).andDo(print()).andExpect(MockMvcResultMatchers.status().isOk());

        verify(datasetService, times(1)).getDatasets(5, 100, 1, 2);

    }

    @Test
    public void testDatasetGetById() throws Exception {
        Integer target = 112;
        when(datasetService.getDataset(target)).thenReturn(new DatasetDTO());

        mockMvc.perform(MockMvcRequestBuilders.get("/gobii-dev/gobii/v3/datasets/112").contextPath("/gobii-dev"))
                .andDo(print()).andExpect(MockMvcResultMatchers.status().isOk());

        verify(datasetService, times(1)).getDataset(target);
    }

    @Test
    public void testDatasetByIdUpdate() throws Exception {
        Integer target = 112;
        when(datasetService.updateDataset(eq(target), any(DatasetRequestDTO.class), eq("test-user")))
                .thenReturn(new DatasetDTO());

        when(projectService.getDefaultProjectEditor()).thenReturn("test-user");

        String requestJson = "{\"datasetName\" : \"test-name-edited\", \"experimentId\" : \"1\", \"callingAnalysisId\" : \"1\", \"analysisIds\" : [ \"2\", \"3\", \"4\" ]}";

        mockMvc.perform(MockMvcRequestBuilders.patch("/gobii-dev/gobii/v3/datasets/112")
                .contentType(MediaType.APPLICATION_JSON).content(requestJson).contextPath("/gobii-dev")).andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testDatasetDeleteById() throws Exception {
        Integer datasetId = 112;
        doNothing().when(datasetService).deleteDataset(datasetId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/gobii-dev/gobii/v3/datasets/112").contextPath("/gobii-dev"))
                .andDo(print()).andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(datasetService, times(1)).deleteDataset(datasetId);
    }

    @Test
    public void testListDatasetTypes() throws Exception {

        when(datasetService.getDatasetTypes(0, 1000)).thenReturn(new PagedResult<CvTypeDTO>());

        mockMvc.perform(MockMvcRequestBuilders.get("/gobii-dev/gobii/v3/datasets/types").contextPath("/gobii-dev"))
                .andDo(print()).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testAddDatasetType() throws Exception {
        when(datasetService.createDatasetType("test-name", "datasetTypeDescription", "user"))
                .thenReturn(new CvTypeDTO());

        when(projectService.getDefaultProjectEditor()).thenReturn("user");

        String requestJson = "{\"typeName\" : \"test-name\", \"typeDescription\": \"datasetTypeDescription\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/gobii-dev/gobii/v3/datasets/types").contextPath("/gobii-dev")
                .contentType(MediaType.APPLICATION_JSON).content(requestJson)).andDo(print())
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void testGetReferences() throws Exception {
        when(referenceService.getReferences(0, 1000)).thenReturn(new PagedResult<ReferenceDTO>());
        mockMvc.perform(MockMvcRequestBuilders.get("/gobii-dev/gobii/v3/references").contextPath("/gobii-dev"))
                .andDo(print()).andExpect(MockMvcResultMatchers.status().isOk());

        verify(referenceService, times(1)).getReferences(0, 1000);

    }

    @Test
    public void testGetMapsets() throws Exception {
        when(mapsetService.getMapsets(0, 1000, null)).thenReturn(new PagedResult<MapsetDTO>());
        mockMvc.perform(MockMvcRequestBuilders.get("/gobii-dev/gobii/v3/mapsets").contextPath("/gobii-dev"))
                .andDo(print()).andExpect(MockMvcResultMatchers.status().isOk());

        verify(mapsetService, times(1)).getMapsets(0, 1000, null);
    }

    @Test
    public void testCreateSimpleMapset() throws Exception {
        String requestJson = "{\"mapsetName\": \"test-mapset\", \"mapsetDescription\": \"test-description\", \"mapsetTypeId\": \"10\", \"referenceId\": \"12\"}";

        when(projectService.getDefaultProjectEditor()).thenReturn("test-user");

        when(mapsetService.createMapset(any(MapsetDTO.class), eq("test-user"))).thenReturn(new MapsetDTO());

        mockMvc.perform(MockMvcRequestBuilders.post("/gobii-dev/gobii/v3/mapsets")
                .contentType(MediaType.APPLICATION_JSON).content(requestJson).contextPath("/gobii-dev")).andDo(print())
                .andExpect(MockMvcResultMatchers.status().isCreated());

        verify(mapsetService, times(1)).createMapset(any(MapsetDTO.class), eq("test-user"));
    }

    @Test
    public void testCreateSimpleMapsetMissingMapsetTypeId() throws Exception {
        String requestJson = "{\"mapsetName\": \"test-mapset\", \"mapsetDescription\": \"test-description\", \"referenceId\": \"12\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/gobii-dev/gobii/v3/mapsets")
                .contentType(MediaType.APPLICATION_JSON).content(requestJson).contextPath("/gobii-dev")).andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void testGetMapsetById() throws Exception {
        when(mapsetService.getMapset(122)).thenReturn(new MapsetDTO());
        mockMvc.perform(MockMvcRequestBuilders.get("/gobii-dev/gobii/v3/mapsets/122").contextPath("/gobii-dev"))
                .andDo(print()).andExpect(MockMvcResultMatchers.status().isOk());
        verify(mapsetService, times(1)).getMapset(122);
    }

    @Test
    public void testGetMapsetByIdNotFound() throws Exception {
        when(mapsetService.getMapset(122)).thenThrow(new GobiiException(GobiiStatusLevel.ERROR,
                GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST, "Mapset not found"));
        mockMvc.perform(MockMvcRequestBuilders.get("/gobii-dev/gobii/v3/mapsets/122").contextPath("/gobii-dev"))
                .andDo(print()).andExpect(MockMvcResultMatchers.status().isNotFound());
        verify(mapsetService, times(1)).getMapset(122);
    }

    @Test
    public void testPatchSimpleMapset() throws Exception {
        String requestJson = "{\"mapsetName\": \"test-mapset\", \"mapsetDescription\": \"test-description\"}";

        when(projectService.getDefaultProjectEditor()).thenReturn("test-user");

        when(mapsetService.updateMapset(eq(122), any(MapsetDTO.class), eq("test-user"))).thenReturn(new MapsetDTO());

        mockMvc.perform(MockMvcRequestBuilders.patch("/gobii-dev/gobii/v3/mapsets/122")
                .contentType(MediaType.APPLICATION_JSON).content(requestJson).contextPath("/gobii-dev")).andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(mapsetService, times(1)).updateMapset(eq(122), any(MapsetDTO.class), eq("test-user"));
    }

    @Test
    public void testDeleteMapset() throws Exception {
        Integer mapsetId = 122;
        doNothing().when(mapsetService).deleteMapset(mapsetId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/gobii-dev/gobii/v3/mapsets/122").contextPath("/gobii-dev"))
                .andDo(print()).andExpect(MockMvcResultMatchers.status().isNoContent());
        verify(mapsetService, times(1)).deleteMapset(122);
    }

    @Test
    public void createMapsetTypeTest() throws Exception {
        String requestJson = "{\"typeName\": \"test-name\", \"typeDescription\": \"test-desc\"}";
        when(mapsetService.createMapsetType("test-name", "test-desc", "user")).thenReturn(new CvTypeDTO());

        when(projectService.getDefaultProjectEditor()).thenReturn("user");

        mockMvc.perform(MockMvcRequestBuilders.post("/gobii-dev/gobii/v3/mapsets/types")
                .contentType(MediaType.APPLICATION_JSON).content(requestJson).contextPath("/gobii-dev")).andDo(print())
                .andExpect(MockMvcResultMatchers.status().isCreated());

        verify(mapsetService, times(1)).createMapsetType("test-name", "test-desc", "user");
    }

    @Test
    public void getMapsetTypes() throws Exception {
        when(mapsetService.getMapsetTypes(0, 1000)).thenReturn(new PagedResult<CvTypeDTO>());

        mockMvc.perform(MockMvcRequestBuilders.get("/gobii-dev/gobii/v3/mapsets/types").contextPath("/gobii-dev"))
                .andDo(print()).andExpect(MockMvcResultMatchers.status().isOk());
        verify(mapsetService, times(1)).getMapsetTypes(0, 1000);
    }

    @Test
    public void getOrganizationsList() throws Exception {
        when(organizationService.getOrganizations(0, 1000)).thenReturn(new PagedResult<OrganizationDTO>());

        mockMvc.perform(MockMvcRequestBuilders.get("/gobii-dev/gobii/v3/organizations").contextPath("/gobii-dev"))
                .andDo(print()).andExpect(MockMvcResultMatchers.status().isOk());
        verify(organizationService, times(1)).getOrganizations(0, 1000);
    }

    @Test
    public void getOrganizationById() throws Exception {
        Integer organizationId = 122;
        when(organizationService.getOrganization(organizationId)).thenReturn(new OrganizationDTO());

        mockMvc.perform(MockMvcRequestBuilders.get("/gobii-dev/gobii/v3/organizations/122").contextPath("/gobii-dev"))
                .andDo(print()).andExpect(MockMvcResultMatchers.status().isOk());

        verify(organizationService, times(1)).getOrganization(122);
    }

    @Test
    public void testCreateOrganization() throws Exception {
        String requestJson = "{\"organizationName\": \"test-org\", \"organizationAddress\": \"organization-address\", \"organizationWebsite\": \"https://website.com\"}";

        when(organizationService.createOrganization(any(OrganizationDTO.class), eq("test-user")))
                .thenReturn(new OrganizationDTO());

        when(projectService.getDefaultProjectEditor()).thenReturn("test-user");

        mockMvc.perform(MockMvcRequestBuilders.post("/gobii-dev/gobii/v3/organizations")
                .contentType(MediaType.APPLICATION_JSON).content(requestJson).contextPath("/gobii-dev")).andDo(print())
                .andExpect(MockMvcResultMatchers.status().isCreated());

        verify(organizationService, times(1)).createOrganization(any(OrganizationDTO.class), eq("test-user"));
    }

    @Test
    public void testUpdateOrganization() throws Exception {
        String requestJson = "{\"organizationName\": \"test-org\", \"organizationAddress\": \"organization-address\", \"organizationWebsite\": \"https://website.com\"}";

        when(organizationService.updateOrganization(eq(123), any(OrganizationDTO.class), eq("test-user")))
                .thenReturn(new OrganizationDTO());

        when(projectService.getDefaultProjectEditor()).thenReturn("test-user");

        mockMvc.perform(MockMvcRequestBuilders.patch("/gobii-dev/gobii/v3/organizations/123")
                .contentType(MediaType.APPLICATION_JSON).content(requestJson).contextPath("/gobii-dev")).andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(organizationService, times(1)).updateOrganization(eq(123), any(OrganizationDTO.class), eq("test-user"));
    }

    @Test
    public void testDeleteOrganization() throws Exception {
        doNothing().when(organizationService).deleteOrganization(123);
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/gobii-dev/gobii/v3/organizations/123").contextPath("/gobii-dev"))
                .andDo(print()).andExpect(MockMvcResultMatchers.status().isNoContent());
        verify(organizationService, times(1)).deleteOrganization(123);
    }

    // --- Cv tests

    @Test
    public void testCreateCv() throws Exception {
        String requestJson = "{\"cvName\": \"test-cv\", \"cvDescription\": \"test-desc\", \"cvGroupId\" : \"16\"}";

        when(cvService.createCv(any(CvDTO.class))).thenReturn(new CvDTO());

        mockMvc.perform(MockMvcRequestBuilders.post("/gobii-dev/gobii/v3/cvs").contextPath("/gobii-dev")
                .contentType(MediaType.APPLICATION_JSON).content(requestJson)).andDo(print())
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void testUpdateCv() throws Exception {
        String requestJson = "{\"cvName\": \"updated-cv-name\"}";

        when(cvService.updateCv(eq(123), any(CvDTO.class))).thenReturn(new CvDTO());

        mockMvc.perform(MockMvcRequestBuilders.patch("/gobii-dev/gobii/v3/cvs/123").content(requestJson)
                .contentType(MediaType.APPLICATION_JSON).contextPath("/gobii-dev")).andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(cvService, times(1)).updateCv(eq(123), any(CvDTO.class));
    }

    @Test
    public void testListCvsDefault() throws Exception {

        when(cvService.getCvs(0, 1000, null, null)).thenReturn(new PagedResult<CvDTO>());

        mockMvc.perform(MockMvcRequestBuilders.get("/gobii-dev/gobii/v3/cvs").contextPath("/gobii-dev")).andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(cvService, times(1)).getCvs(0, 1000, null, null);

    }

    @Test
    public void testGetCvById() throws Exception {
        when(cvService.getCv(123)).thenReturn(new CvDTO());

        mockMvc.perform(MockMvcRequestBuilders.get("/gobii-dev/gobii/v3/cvs/123").contextPath("/gobii-dev"))
                .andDo(print()).andExpect(MockMvcResultMatchers.status().isOk());

        verify(cvService, times(1)).getCv(123);
    }

    @Test
    public void testGetCvProps() throws Exception {

        when(cvService.getCvProperties(0, 1000)).thenReturn(new PagedResult<CvPropertyDTO>());
        mockMvc.perform(MockMvcRequestBuilders.get("/gobii-dev/gobii/v3/cvs/properties").contextPath("/gobii-dev"))
                .andDo(print()).andExpect(MockMvcResultMatchers.status().isOk());

        verify(cvService, times(1)).getCvProperties(0, 1000);
    }

    @Test
    public void testAddCvProps() throws Exception {
        String requestJson = "{\"propertyName\": \"test-prop\", \"propertyDescription\": \"test-desc\"}";
        CvPropertyDTO mockDTO = new CvPropertyDTO();
        mockDTO.setPropertyGroupType(1);
        when(cvService.addCvProperty(any(CvPropertyDTO.class))).thenReturn(new CvPropertyDTO());

        mockMvc.perform(MockMvcRequestBuilders.post("/gobii-dev/gobii/v3/cvs/properties").contextPath("/gobii-dev")
                .contentType(MediaType.APPLICATION_JSON).content(requestJson)).andDo(print())
                .andExpect(MockMvcResultMatchers.status().isCreated());

        verify(cvService, times(1)).addCvProperty(any(CvPropertyDTO.class));
    }

    @Test
    public void testDeleteCv() throws Exception {
        doNothing().when(cvService).deleteCv(123);

        mockMvc.perform(MockMvcRequestBuilders.delete("/gobii-dev/gobii/v3/cvs/123").contextPath("/gobii-dev"))
                .andDo(print()).andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(cvService, times(1)).deleteCv(123);
    }

    // --Platforms
    @Test
    public void testCreatePlatform() throws Exception {
        String requestJson = "{\"platformName\": \"test-platform-name\", \"platformTypeId\": \"7\"}";

        when(projectService.getDefaultProjectEditor()).thenReturn("test-user");

        when(platformService.createPlatform(any(PlatformDTO.class), eq("test-user"))).thenReturn(new PlatformDTO());

        mockMvc.perform(MockMvcRequestBuilders.post("/gobii-dev/gobii/v3/platforms")
                .contentType(MediaType.APPLICATION_JSON).content(requestJson).contextPath("/gobii-dev")).andDo(print())
                .andExpect(MockMvcResultMatchers.status().isCreated());

        verify(platformService, times(1)).createPlatform(any(PlatformDTO.class), eq("test-user"));

    }

    @Test
    public void testListPlatforms() throws Exception {
        when(platformService.getPlatforms(0, 1000, null)).thenReturn(new PagedResult<PlatformDTO>());

        mockMvc.perform(MockMvcRequestBuilders.get("/gobii-dev/gobii/v3/platforms").contextPath("/gobii-dev"))
                .andDo(print()).andExpect(MockMvcResultMatchers.status().isOk());

        verify(platformService, times(1)).getPlatforms(0, 1000, null);
    }

    @Test
    public void testGetPlatform() throws Exception {
        when(platformService.getPlatform(123)).thenReturn(new PlatformDTO());

        mockMvc.perform(MockMvcRequestBuilders.get("/gobii-dev/gobii/v3/platforms/123").contextPath("/gobii-dev"))
                .andDo(print()).andExpect(MockMvcResultMatchers.status().isOk());

        verify(platformService, times(1)).getPlatform(123);

    }

    @Test
    public void testUpdatePlatform() throws Exception {
        when(projectService.getDefaultProjectEditor()).thenReturn("test-user");

        when(platformService.updatePlatform(eq(123), any(PlatformDTO.class), eq("test-user")))
                .thenReturn(new PlatformDTO());

        String requestJson = "{\"platformName\": \"updated-platform-name\", \"platformTypeId\": \"12\"}";

        mockMvc.perform(MockMvcRequestBuilders.patch("/gobii-dev/gobii/v3/platforms/123").contextPath("/gobii-dev")
                .contentType(MediaType.APPLICATION_JSON).content(requestJson)).andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(platformService, times(1)).updatePlatform(eq(123), any(PlatformDTO.class), eq("test-user"));

    }

    @Test
    public void testDeletePlatform() throws Exception {
        doNothing().when(platformService).deletePlatform(123);

        mockMvc.perform(MockMvcRequestBuilders.delete("/gobii-dev/gobii/v3/platforms/123").contextPath("/gobii-dev"))
                .andDo(print()).andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(platformService, times(1)).deletePlatform(123);
    }

    @Test
    public void testCreatePlatformType() throws Exception {
        String requestJson = "{\"typeName\": \"new-platform-type\", \"typeDescription\": \"12\"}";

        when(platformService.createPlatformType(any(CvTypeDTO.class))).thenReturn(new CvTypeDTO());

        mockMvc.perform(MockMvcRequestBuilders.post("/gobii-dev/gobii/v3/platforms/types")
                .contentType(MediaType.APPLICATION_JSON).content(requestJson).contextPath("/gobii-dev")).andDo(print())
                .andExpect(MockMvcResultMatchers.status().isCreated());

        verify(platformService, times(1)).createPlatformType(any(CvTypeDTO.class));
    }

    @Test
    public void testListPlatformTypes() throws Exception {
        when(platformService.getPlatformTypes(0, 1000)).thenReturn(new PagedResult<CvTypeDTO>());

        mockMvc.perform(MockMvcRequestBuilders.get("/gobii-dev/gobii/v3/platforms/types").contextPath("/gobii-dev"))
                .andDo(print()).andExpect(MockMvcResultMatchers.status().isOk());

        verify(platformService, times(1)).getPlatformTypes(0, 1000);
    }

    // -- References
    @Test
    public void testCreateGenomeReference() throws Exception {
        when(referenceService.createReference(any(ReferenceDTO.class), eq("test-user"))).thenReturn(new ReferenceDTO());

        when(projectService.getDefaultProjectEditor()).thenReturn("test-user");

        String requestJson = "{\"referenceName\": \"test-ref\", \"version\": \"vtest\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/gobii-dev/gobii/v3/references")
                .contentType(MediaType.APPLICATION_JSON).content(requestJson).contextPath("/gobii-dev")).andDo(print())
                .andExpect(MockMvcResultMatchers.status().isCreated());

        verify(referenceService, times(1)).createReference(any(ReferenceDTO.class), eq("test-user"));
    }

    @Test
    public void testGetReference() throws Exception {
        when(referenceService.getReference(123)).thenReturn(new ReferenceDTO());

        mockMvc.perform(MockMvcRequestBuilders.get("/gobii-dev/gobii/v3/references/123").contextPath("/gobii-dev"))
                .andDo(print()).andExpect(MockMvcResultMatchers.status().isOk());

        verify(referenceService, times(1)).getReference(123);
    }

    @Test
    public void testUpdateReference() throws Exception {
        when(projectService.getDefaultProjectEditor()).thenReturn("test-user");

        when(referenceService.updateReference(eq(123), any(ReferenceDTO.class), eq("test-user")))
                .thenReturn(new ReferenceDTO());

        String requestJson = "{\"referenceName\": \"test-ref-update\", \"version\": \"vtest1\"}";
        mockMvc.perform(MockMvcRequestBuilders.patch("/gobii-dev/gobii/v3/references/123")
                .contentType(MediaType.APPLICATION_JSON).content(requestJson).contextPath("/gobii-dev")).andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(referenceService, times(1)).updateReference(eq(123), any(ReferenceDTO.class), eq("test-user"));
    }

    @Test
    public void testDeleteReference() throws Exception {
        doNothing().when(referenceService).deleteReference(123);

        mockMvc.perform(MockMvcRequestBuilders.delete("/gobii-dev/gobii/v3/references/123").contextPath("/gobii-dev"))
                .andDo(print()).andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(referenceService, times(1)).deleteReference(123);
    }

    @Test
    public void testCreateMarkerSet() throws Exception {
        String requestJson = "{\"markerGroupName\": \"test-marker-group\", \"germplasmGroup\": \"test-germplasm-group\"}";

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
        String requestJson = "{\"markerGroupName\": \"test-marker-group-updated\", \"germplasmGroup\": \"test-germplasm-group\"}";
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
        String requestJson = "[{\"markerName\": \"foo marker\", \"platformName\": \"foo platform\", \"favorableAlleles\": [\"A\"]},{\"markerName\": \"bar marker\",\"platformName\": \"bar platform\", \"favorableAlleles\": [\"G\"]}]";
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