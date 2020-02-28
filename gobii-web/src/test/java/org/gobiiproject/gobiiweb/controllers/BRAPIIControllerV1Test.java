package org.gobiiproject.gobiiweb.controllers;

import org.apache.commons.lang.RandomStringUtils;
import org.gobiiproject.gobidomain.services.brapi.SamplesBrapiService;
import org.gobiiproject.gobidomain.services.brapi.VariantSetsBrapiService;
import org.gobiiproject.gobiimodel.dto.auditable.AnalysisBrapiDTO;
import org.gobiiproject.gobiimodel.dto.auditable.VariantSetDTO;
import org.gobiiproject.gobiimodel.dto.noaudit.DataSetBrapiDTO;
import org.gobiiproject.gobiimodel.dto.noaudit.CallSetBrapiDTO;
import org.gobiiproject.gobiimodel.dto.noaudit.MarkerBrapiDTO;
import org.gobiiproject.gobiimodel.dto.noaudit.SamplesBrapiDTO;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring/application-config.xml"})
@WebAppConfiguration
public class BRAPIIControllerV1Test {

    @Mock
    private SamplesBrapiService samplesBrapiService;

    @Mock
    private VariantSetsBrapiService variantSetsBrapiService;

    @InjectMocks
    private BRAPIIControllerV2 brapiiControllerV2;

    private MockMvc mockMvc;

    Random random = new Random();

    @Before
    public void setup() throws Exception {

        MockitoAnnotations.initMocks(this);

        this.mockMvc = MockMvcBuilders.standaloneSetup(brapiiControllerV2).build();

    }

    private CallSetBrapiDTO createMockDnaRunDTO() {

        CallSetBrapiDTO callSetBrapiDTO = new CallSetBrapiDTO();

        callSetBrapiDTO.setCallSetDbId(34);
        callSetBrapiDTO.setCallSetName("test-callset");
        callSetBrapiDTO.setGermplasmDbId(1);

        return callSetBrapiDTO;
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

        //List<CallSetBrapiDTO> callsets = new ArrayList<>();

        //CallSetBrapiDTO callset1 = createMockDnaRunDTO();

        //callsets.add(callset1);

        //when(
        //        dnaRunService.getDnaRuns(
        //                any(Integer.TYPE), any(Integer.TYPE), any(CallSetBrapiDTO.class))
        //).thenReturn(callsets);

        //mockMvc.perform(
        //        MockMvcRequestBuilders.get(
        //                "/gobii-dev/brapi/v1/callsets"
        //        ).contextPath("/gobii-dev")).andDo(print()
        //).andExpect(MockMvcResultMatchers.status().isOk()
        //).andExpect(content().contentType(MediaType.APPLICATION_JSON)
        //).andExpect(jsonPath("$.metadata.pagination.pageSize").value(1));
    }

    @Test
    public void getCallsetById() throws Exception {

        //CallSetBrapiDTO callSetBrapiDTO = createMockDnaRunDTO();

        //when (
        //        dnaRunService.getDnaRunById(callSetBrapiDTO.getId())
        //).thenReturn(callSetBrapiDTO);

        //mockMvc.perform(
        //        MockMvcRequestBuilders.get(
        //                "/gobii-dev/brapi/v1/callsets/{callSetDbId}", callSetBrapiDTO.getCallSetDbId()
        //        ).contextPath("/gobii-dev")).andDo(print())
        //        .andExpect(MockMvcResultMatchers.status().isOk())
        //        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        //        .andExpect(jsonPath("$.result.callSetDbId").value(callSetBrapiDTO.getCallSetDbId()))
        //        .andExpect(jsonPath("$.result.callSetName").value(callSetBrapiDTO.getCallSetName()));
    }

    @Test
    public void getVariants() throws Exception {

        //List<MarkerBrapiDTO> variants = new ArrayList<>();

        //MarkerBrapiDTO variant1 = createMockMarkerDTO();

        //variants.add(variant1);

        //when (
        //        markerBrapiService.getMarkers(
        //                any(Integer.TYPE), any(Integer.TYPE) ,any(Integer.TYPE), any(MarkerBrapiDTO.class)
        //        )
        //).thenReturn(variants);

        //mockMvc.perform(
        //        MockMvcRequestBuilders.get(
        //                "/gobii-dev/brapi/v1/variants").contextPath("/gobii-dev")).andDo(print())
        //        .andExpect(MockMvcResultMatchers.status().isOk())
        //        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        //        .andExpect(jsonPath("$.metadata.pagination.pageSize").value(1));
    }

    @Test
    public void getVariantById() throws Exception {

        //MarkerBrapiDTO variantDTO = createMockMarkerDTO();

        //variantDTO.setVariantDbId(random.nextInt(10));

        //when (
        //        markerBrapiService.getMarkerById(variantDTO.getId())
        //).thenReturn(variantDTO);

        //mockMvc.perform(
        //        MockMvcRequestBuilders.get(
        //                "/gobii-dev/brapi/v1/variants/{variantDbId}", variantDTO.getVariantDbId())
        //                .contextPath("/gobii-dev")).andDo(print())
        //        .andExpect(MockMvcResultMatchers.status().isOk())
        //        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        //        .andExpect(jsonPath("$.result.variantDbId").value(variantDTO.getVariantDbId()))
        //        .andExpect(jsonPath("$.result.variantName").value(variantDTO.getVariantName()));
    }

    @Test
    public void getVariantSets() throws Exception {

    }

    @Test
    public void getVariantSetById() throws Exception {

        //DataSetBrapiDTO variantSetDTO = createMockDatasetDTO();

        //when (
        //        datasetBrapiService.getDatasetById(variantSetDTO.getId())
        //).thenReturn(variantSetDTO);

        //mockMvc.perform(
        //        MockMvcRequestBuilders.get(
        //                "/gobii-dev/brapi/v1/variantsets/{variantSetDbId}",
        //                variantSetDTO.getVariantSetDbId()).contextPath("/gobii-dev")).andDo(print())
        //        .andExpect(MockMvcResultMatchers.status().isOk())
        //        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        //        .andExpect(jsonPath("$.result.variantSetDbId").value(variantSetDTO.getVariantSetDbId()))
        //        .andExpect(jsonPath("$.result.variantSetName").value(variantSetDTO.getVariantSetName()));
    }

    @Test
    public void getVariantsByVariantSetId() throws Exception {

        //List<MarkerBrapiDTO> variants = new ArrayList<>();

        //MarkerBrapiDTO variant1 = createMockMarkerDTO();

        //DataSetBrapiDTO dataSetBrapiDTO = createMockDatasetDTO();

        //List<Integer> dataSetDbIdArr = new ArrayList<>();
        //dataSetDbIdArr.add(dataSetBrapiDTO.getVariantSetDbId());

        //variant1.setVariantSetDbId(dataSetDbIdArr);
        //variants.add(variant1);

        //when(
        //        markerBrapiService.getMarkers(
        //                any(Integer.TYPE), any(Integer.TYPE) , any(Integer.TYPE), any(MarkerBrapiDTO.class)
        //        )
        //).thenReturn(variants);

        //mockMvc.perform(
        //        MockMvcRequestBuilders.get(
        //                "/gobii-dev/brapi/v1/variantsets/{variantSetDbId}/variants", dataSetBrapiDTO.getVariantSetDbId()
        //        ).contextPath("/gobii-dev")).andDo(print())
        //        .andExpect(MockMvcResultMatchers.status().isOk())
        //        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        //        .andExpect(jsonPath("$.metadata.pagination.pageSize").value(1))
        //        .andExpect(
        //                jsonPath("$.result.data[0].variantSetDbId[0]")
        //                        .value(dataSetBrapiDTO.getVariantSetDbId()));
    }

    @Test
    public void getCallSetsByVariantSetId() throws Exception {

        //List<CallSetBrapiDTO> callsets = new ArrayList<>();

        //CallSetBrapiDTO callset1 = createMockDnaRunDTO();

        //DataSetBrapiDTO dataSetBrapiDTO = createMockDatasetDTO();

        //List<Integer> dataSetDbIdArr = new ArrayList<>();
        //dataSetDbIdArr.add(dataSetBrapiDTO.getVariantSetDbId());

        //callset1.setVariantSetIds(dataSetDbIdArr);
        //callsets.add(callset1);

        //when(
        //        dnaRunService.getDnaRuns(
        //                any(Integer.TYPE), any(Integer.TYPE), any(CallSetBrapiDTO.class)
        //        )
        //).thenReturn(callsets);

        //mockMvc.perform(
        //        MockMvcRequestBuilders.get(
        //                "/gobii-dev/brapi/v1/variantsets/{variantSetDbId}/callsets",
        //                dataSetBrapiDTO.getVariantSetDbId()
        //        ).contextPath("/gobii-dev")).andDo(print()
        //).andExpect(MockMvcResultMatchers.status().isOk()
        //).andExpect(content().contentType(MediaType.APPLICATION_JSON)
        //).andExpect(jsonPath("$.metadata.pagination.pageSize").value(1)
        //).andExpect(jsonPath("$.result.data[0].variantSetIds[0]").value(dataSetBrapiDTO.getVariantSetDbId()));

    }

    private List<SamplesBrapiDTO> createMockSamples(Integer pageSize) {

        List<SamplesBrapiDTO> returnVal = new ArrayList<>();


        for(int i = 0; i < pageSize; i++) {

            SamplesBrapiDTO sample = new SamplesBrapiDTO();

            sample.setTissueType(RandomStringUtils.random(5, true, false));

            sample.setColumn(RandomStringUtils.random(1, true, true));

            sample.setGermplasmDbId(random.nextInt(10));

            sample.setObservationUnitDbId(UUID.randomUUID().toString());

            sample.setPlateName(RandomStringUtils.random(5, true, true));

            sample.setRow(RandomStringUtils.random(1, true, true));

            sample.setSampleDbId(i);

            sample.setSampleGroupDbId(random.nextInt(4));

            sample.setSampleName(RandomStringUtils.random(5, true, true));

            sample.setSamplePUI(UUID.randomUUID().toString());

            sample.setTissueType(RandomStringUtils.random(5, true, true));

            sample.setSampleType(RandomStringUtils.random(4, true, true));

            returnVal.add(sample);

        }

        return returnVal;

    }

    @Test
    public void testGetSamplesList() throws Exception {

        Integer pageSize = random.nextInt(100);

        final Integer defaultPageNum =  0;

        List<SamplesBrapiDTO> mockSamples = createMockSamples(pageSize);

        when(
                samplesBrapiService.getSamples(
                        any(Integer.TYPE), eq(pageSize),
                        isNull(Integer.class), isNull(Integer.class),
                        isNull(String.class))
        ).thenReturn(mockSamples);

        mockMvc.perform(
                MockMvcRequestBuilders.get(
                        "/gobii-dev/brapi/v1/samples")
                        .param("pageSize",  pageSize.toString())
                        .contextPath("/gobii-dev")).andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.metadata").exists())
                .andExpect(jsonPath("$.metadata.pagination").exists())
                .andExpect(jsonPath("$.metadata.pagination.currentPage").exists())
                .andExpect(jsonPath("$.metadata.pagination.pageSize").value(pageSize))
                //Current page should be zero as page param is null
                .andExpect(jsonPath("$.metadata.pagination.currentPage").value(defaultPageNum))
                .andExpect(jsonPath("$.result.data[0].sampleDbId")
                        .value(mockSamples.get(0).getSampleDbId().toString()))
                .andExpect(jsonPath("$.result.data[0].column")
                        .value(mockSamples.get(0).getColumn()))
                .andExpect(jsonPath("$.result.data[0].germplasmDbId")
                        .value(mockSamples.get(0).getGermplasmDbId().toString()))
                .andExpect(jsonPath("$.result.data[0].observationUnitDbId")
                        .value(mockSamples.get(0).getObservationUnitDbId()))
                .andExpect(jsonPath("$.result.data[0].plateName")
                        .value(mockSamples.get(0).getPlateName()))
                .andExpect(jsonPath("$.result.data[0].row")
                        .value(mockSamples.get(0).getRow()))
                .andExpect(jsonPath("$.result.data[0].sampleDbId")
                        .value(mockSamples.get(0).getSampleDbId().toString()))
                .andExpect(jsonPath("$.result.data[0].sampleGroupDbId")
                        .value(mockSamples.get(0).getSampleGroupDbId().toString()))
                .andExpect(jsonPath("$.result.data[0].sampleName")
                        .value(mockSamples.get(0).getSampleName()))
                .andExpect(jsonPath("$.result.data[0].samplePUI")
                        .value(mockSamples.get(0).getSamplePUI()))
                .andExpect(jsonPath("$.result.data[0].sampleType")
                        .value(mockSamples.get(0).getSampleType()))
                .andExpect(jsonPath("$.result.data[0].tissueType")
                        .value(mockSamples.get(0).getTissueType()));


    }

    @Test
    public void testGetSamplesListPageNum() throws Exception {

        Integer pageSize = random.nextInt(100);
        Integer pageNum = random.nextInt(10);

        List<SamplesBrapiDTO> mockSamples = createMockSamples(pageSize);

        when(
                samplesBrapiService.getSamples(
                        any(Integer.TYPE), eq(pageSize),
                        isNull(Integer.class), isNull(Integer.class),
                        isNull(String.class))
        ).thenReturn(mockSamples);

        mockMvc.perform(
                MockMvcRequestBuilders.get(
                        "/gobii-dev/brapi/v1/samples")
                        .param("pageSize",  pageSize.toString())
                        .param("page",  pageNum.toString())
                        .contextPath("/gobii-dev")).andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.metadata").exists())
                .andExpect(jsonPath("$.metadata.pagination").exists())
                .andExpect(jsonPath("$.metadata.pagination.currentPage").exists())
                .andExpect(jsonPath("$.metadata.pagination.pageSize").value(pageSize))
                .andExpect(jsonPath("$.metadata.pagination.currentPage").value(pageNum));



    }

    private List<VariantSetDTO> createMockVariantSets(Integer pageSize) {

        List<VariantSetDTO> returnVal = new ArrayList<>();


        for(int i = 0; i < pageSize; i++) {

            VariantSetDTO variantSet = new VariantSetDTO();


            variantSet.setStudyDbId(random.nextInt(10));

            variantSet.setVariantCount(100);
            variantSet.setCallSetCount(1000);

            variantSet.setFileUrl(RandomStringUtils.random(10, true, true));
            variantSet.setFileFormat(RandomStringUtils.random(4, true, true));
            variantSet.setVariantSetDbId(random.nextInt(2));
            variantSet.setDataFormat(RandomStringUtils.random(4, true, true));

            variantSet.setReferenceSetDbId(random.nextInt(2));
            variantSet.setVariantSetName(RandomStringUtils.random(5, true, true));


            returnVal.add(variantSet);

        }

        return returnVal;

    }

}
