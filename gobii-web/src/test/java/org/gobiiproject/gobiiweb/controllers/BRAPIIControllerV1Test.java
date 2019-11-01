package org.gobiiproject.gobiiweb.controllers;

import org.gobiiproject.gobidomain.security.UserContextLoader;
import org.gobiiproject.gobidomain.services.DatasetBrapiService;
import org.gobiiproject.gobidomain.services.DnaRunService;
import org.gobiiproject.gobidomain.services.MarkerBrapiService;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.TestExecConfig;
import org.gobiiproject.gobiimodel.dto.entity.auditable.AnalysisBrapiDTO;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.DataSetBrapiDTO;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.DnaRunDTO;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.MarkerBrapiDTO;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring/application-config.xml"})
@WebAppConfiguration
public class BRAPIIControllerV1Test {

    //@Autowired
    //private WebApplicationContext wac;

    @Mock
    private DnaRunService dnaRunService;

    @Mock
    private MarkerBrapiService markerBrapiService;

    @Mock
    private DatasetBrapiService datasetBrapiService;

    @InjectMocks
    private BRAPIIControllerV1 brapiiControllerV1;

    private MockMvc mockMvc;

    @BeforeClass
    public static void setupClass() {

        String CONFIG_FILE_LOCATION_PROP = "cfgFqpn";
        String configFileLocation = System.getProperty(CONFIG_FILE_LOCATION_PROP);
        ConfigSettings configSettings = new ConfigSettings(configFileLocation);
        TestExecConfig testExecConfig = configSettings.getTestExecConfig();
        UserContextLoader userContextLoader = new UserContextLoader();
        userContextLoader.loadUser(testExecConfig.getLdapUserForUnitTest());

    }

    @Before
    public void setup() throws Exception {

        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(brapiiControllerV1).build();

    }

    private DnaRunDTO createMockDnaRunDTO() {

        DnaRunDTO callSetDTO = new DnaRunDTO();

        callSetDTO.setCallSetDbId(34);
        callSetDTO.setCallSetName("test-callset");
        callSetDTO.setDnaRunCode("test-code");
        callSetDTO.setGermplasmDbId(1);

        return callSetDTO;
    }

    private MarkerBrapiDTO createMockMarkerDTO() {

        MarkerBrapiDTO markerBrapiDTO = new MarkerBrapiDTO();
        List<Integer> variantSetDbId = new ArrayList<>();
        variantSetDbId.add(1);
        variantSetDbId.add(2);

        markerBrapiDTO.setVariantDbId(35);
        markerBrapiDTO.setVariantName("test-variant");
        markerBrapiDTO.setVariantSetDbId(variantSetDbId);
        markerBrapiDTO.setMapSetName("test-mapset");

        return markerBrapiDTO;
    }

    private AnalysisBrapiDTO createMockAnalysisDTO(Integer analysisId) {

        AnalysisBrapiDTO analysisBrapiDTO = new AnalysisBrapiDTO();

        analysisBrapiDTO.setAnalysisDbId(analysisId);
        analysisBrapiDTO.setAnalysisName("test-analysis-"+analysisId.toString());
        analysisBrapiDTO.setType("calling");

        return analysisBrapiDTO;
    }

    private DataSetBrapiDTO createMockDatasetDTO() {

        DataSetBrapiDTO dataSetBrapiDTO = new DataSetBrapiDTO();

        AnalysisBrapiDTO analysisBrapiDTO = createMockAnalysisDTO(1);
        AnalysisBrapiDTO analysisBrapiDTO1 = createMockAnalysisDTO(2);

        dataSetBrapiDTO.setVariantSetDbId(36);
        dataSetBrapiDTO.setVariantSetName("test-variantSet");
        dataSetBrapiDTO.setStudyDbId(1);
        dataSetBrapiDTO.setCallingAnalysisId(analysisBrapiDTO.getAnalysisDbId());
        dataSetBrapiDTO.setStudyName("test-study");

        List<AnalysisBrapiDTO> analysisBrapiDTOList = new ArrayList<>();
        analysisBrapiDTOList.add(analysisBrapiDTO1);

        dataSetBrapiDTO.setAnalyses(analysisBrapiDTOList);

        return dataSetBrapiDTO;
    }

    @Test
    public void getCallsets() throws Exception {

        List<DnaRunDTO> callsets = new ArrayList<>();

        DnaRunDTO callset1 = createMockDnaRunDTO();

        callsets.add(callset1);

        when(
                dnaRunService.getDnaRuns(
                        any(Integer.TYPE), any(Integer.TYPE), any(DnaRunDTO.class))
        ).thenReturn(callsets);

        mockMvc.perform(
                MockMvcRequestBuilders.get(
                        "/gobii-dev/brapi/v1/callsets"
                ).contextPath("/gobii-dev")).andDo(print()
        ).andExpect(MockMvcResultMatchers.status().isOk()
        ).andExpect(content().contentType(MediaType.APPLICATION_JSON)
        ).andExpect(jsonPath("$.metaData.pagination.pageSize").value(1));
    }

    @Test
    public void getCallsetById() throws Exception {

        DnaRunDTO callSetDTO = createMockDnaRunDTO();

        when (
                dnaRunService.getDnaRunById(callSetDTO.getId())
        ).thenReturn(callSetDTO);

        mockMvc.perform(
                MockMvcRequestBuilders.get(
                        "/gobii-dev/brapi/v1/callsets/{callSetDbId}", callSetDTO.getCallSetDbId()
                ).contextPath("/gobii-dev")).andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result.callSetDbId").value(callSetDTO.getCallSetDbId()))
                .andExpect(jsonPath("$.result.callSetName").value(callSetDTO.getCallSetName()));
    }

    @Test
    public void getVariants() throws Exception {

        List<MarkerBrapiDTO> variants = new ArrayList<>();

        MarkerBrapiDTO variant1 = createMockMarkerDTO();

        variants.add(variant1);

        when (
                markerBrapiService.getMarkers(
                        any(Integer.TYPE), any(Integer.TYPE) ,any(Integer.TYPE), any(MarkerBrapiDTO.class)
                )
        ).thenReturn(variants);

        mockMvc.perform(
                MockMvcRequestBuilders.get(
                        "/gobii-dev/brapi/v1/variants").contextPath("/gobii-dev")).andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.metadata.pagination.pageSize").value(1));
    }

    @Test
    public void getVariantById() throws Exception {

        MarkerBrapiDTO variantDTO = createMockMarkerDTO();

        when (
                markerBrapiService.getMarkerById(variantDTO.getId())
        ).thenReturn(variantDTO);

        mockMvc.perform(
                MockMvcRequestBuilders.get(
                        "/gobii-dev/brapi/v1/variants/{variantDbId}", variantDTO.getVariantDbId())
                        .contextPath("/gobii-dev")).andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result.variantDbId").value(variantDTO.getVariantDbId()))
                .andExpect(jsonPath("$.result.variantName").value(variantDTO.getVariantName()));
    }

    @Test
    public void getVariantSets() throws Exception {

        List<DataSetBrapiDTO> variantSets = new ArrayList<>();

        DataSetBrapiDTO dataSetBrapiDTO = createMockDatasetDTO();

        variantSets.add(dataSetBrapiDTO);

        when (datasetBrapiService.getDatasets(
                        any(Integer.TYPE), any(Integer.TYPE), any(DataSetBrapiDTO.class)))
                .thenReturn(variantSets);

        mockMvc.perform(
                MockMvcRequestBuilders.get(
                        "/gobii-dev/brapi/v1/variantsets")
                        .contextPath("/gobii-dev")).andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.metadata.pagination.pageSize").value(1));
    }

    @Test
    public void getVariantSetById() throws Exception {

        DataSetBrapiDTO variantSetDTO = createMockDatasetDTO();

        when (
                datasetBrapiService.getDatasetById(variantSetDTO.getId())
        ).thenReturn(variantSetDTO);

        mockMvc.perform(
                MockMvcRequestBuilders.get(
                        "/gobii-dev/brapi/v1/variantsets/{variantSetDbId}",
                        variantSetDTO.getVariantSetDbId()).contextPath("/gobii-dev")).andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result.variantSetDbId").value(variantSetDTO.getVariantSetDbId()))
                .andExpect(jsonPath("$.result.variantSetName").value(variantSetDTO.getVariantSetName()));
    }

    @Test
    public void getVariantsByVariantSetId() throws Exception {

        List<MarkerBrapiDTO> variants = new ArrayList<>();

        MarkerBrapiDTO variant1 = createMockMarkerDTO();

        DataSetBrapiDTO dataSetBrapiDTO = createMockDatasetDTO();

        List<Integer> dataSetDbIdArr = new ArrayList<>();
        dataSetDbIdArr.add(dataSetBrapiDTO.getVariantSetDbId());

        variant1.setVariantSetDbId(dataSetDbIdArr);
        variants.add(variant1);

        when(
                markerBrapiService.getMarkers(
                        any(Integer.TYPE), any(Integer.TYPE) , any(Integer.TYPE), any(MarkerBrapiDTO.class)
                )
        ).thenReturn(variants);

        mockMvc.perform(
                MockMvcRequestBuilders.get(
                        "/gobii-dev/brapi/v1/variantsets/{variantSetDbId}/variants", dataSetBrapiDTO.getVariantSetDbId()
                ).contextPath("/gobii-dev")).andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.metadata.pagination.pageSize").value(1))
                .andExpect(
                        jsonPath("$.result.data[0].variantSetDbId[0]")
                                .value(dataSetBrapiDTO.getVariantSetDbId()));
    }

    @Test
    public void getCallSetsByVariantSetId() throws Exception {

        List<DnaRunDTO> callsets = new ArrayList<>();

        DnaRunDTO callset1 = createMockDnaRunDTO();

        DataSetBrapiDTO dataSetBrapiDTO = createMockDatasetDTO();

        List<Integer> dataSetDbIdArr = new ArrayList<>();
        dataSetDbIdArr.add(dataSetBrapiDTO.getVariantSetDbId());

        callset1.setVariantSetIds(dataSetDbIdArr);
        callsets.add(callset1);

        when(
                dnaRunService.getDnaRuns(
                        any(Integer.TYPE), any(Integer.TYPE), any(DnaRunDTO.class)
                )
        ).thenReturn(callsets);

        mockMvc.perform(
                MockMvcRequestBuilders.get(
                        "/gobii-dev/brapi/v1/variantsets/{variantSetDbId}/callsets",
                        dataSetBrapiDTO.getVariantSetDbId()
                ).contextPath("/gobii-dev")).andDo(print()
        ).andExpect(MockMvcResultMatchers.status().isOk()
        ).andExpect(content().contentType(MediaType.APPLICATION_JSON)
        ).andExpect(jsonPath("$.metaData.pagination.pageSize").value(1)
        ).andExpect(jsonPath("$.result.data[0].variantSetIds[0]").value(dataSetBrapiDTO.getVariantSetDbId()));

    }

}
