package org.gobiiproject.gobiiweb.controllers;

import org.apache.commons.lang.RandomStringUtils;
import org.gobiiproject.gobidomain.services.brapi.SamplesService;
import org.gobiiproject.gobidomain.services.brapi.VariantSetsService;
import org.gobiiproject.gobiimodel.dto.brapi.*;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;
import org.junit.Before;
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

@WebAppConfiguration
public class BRAPIIControllerV1Test {

    @Mock
    private SamplesService samplesBrapiService;

    @Mock
    private VariantSetsService variantSetsService;

    @InjectMocks
    private BrapiV2Controller brApiV2Controller;

    private MockMvc mockMvc;

    Random random = new Random();

    @Before
    public void setup() throws Exception {

        MockitoAnnotations.initMocks(this);

        this.mockMvc = MockMvcBuilders.standaloneSetup(brApiV2Controller).build();

    }

    @SuppressWarnings("unused")
    private CallSetDTO createMockDnaRunDTO() {

        CallSetDTO callSetDTO = new CallSetDTO();

        callSetDTO.setCallSetDbId(34);
        callSetDTO.setCallSetName("test-callset");
        callSetDTO.setGermplasmDbId(1);

        return callSetDTO;
    }


    @SuppressWarnings("unused")
    private AnalysisDTO createMockAnalysisDTO(Integer analysisId) {

        AnalysisDTO analysisDTO = new AnalysisDTO();

        analysisDTO.setAnalysisDbId(analysisId);
        analysisDTO.setAnalysisName("test-analysis-"+analysisId.toString());
        analysisDTO.setType("calling");

        return analysisDTO;
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

    private List<SamplesDTO> createMockSamples(Integer pageSize) {

        List<SamplesDTO> returnVal = new ArrayList<>();


        for(int i = 0; i < pageSize; i++) {

            SamplesDTO sample = new SamplesDTO();

            sample.setTissueType(RandomStringUtils.random(5, true, false));

            sample.setColumn(RandomStringUtils.random(1, true, true));

            sample.setGermplasmDbId(random.nextInt(10));

            sample.setGermplasmPUI(UUID.randomUUID().toString());

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

    @SuppressWarnings("deprecation")
    @Test
    public void testGetSamplesList() throws Exception {

        Integer pageSize = random.nextInt(100);

        final Integer defaultPageNum =  0;

        List<SamplesDTO> mockSamples = createMockSamples(pageSize);

        PagedResult<SamplesDTO> result = new PagedResult<>();

        result.setCurrentPageSize(mockSamples.size());
        result.setCurrentPageNum(defaultPageNum);
        result.setResult(mockSamples);

        when(
                samplesBrapiService.getSamples(
                        eq(pageSize), any(Integer.TYPE),
                        isNull(Integer.class),isNull(Integer.class),
                        isNull(String.class))
        ).thenReturn(result);

        mockMvc.perform(
                MockMvcRequestBuilders.get(
                        "/gobii-dev/brapi/v2/samples")
                        .param("pageSize",  pageSize.toString())
                        .contextPath("/gobii-dev")).andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.metadata").exists())
                .andExpect(jsonPath("$.metadata.pagination").exists())
                .andExpect(jsonPath("$.metadata.pagination.currentPage").exists())
                .andExpect(jsonPath("$.metadata.pagination.pageSize").value(pageSize))
                .andExpect(jsonPath("$.metadata.pagination.currentPage").value(defaultPageNum))
                .andExpect(jsonPath("$.result.data[0].sampleDbId")
                        .value(mockSamples.get(0).getSampleDbId().toString()))
                .andExpect(jsonPath("$.result.data[0].column")
                        .value(mockSamples.get(0).getColumn()))
                .andExpect(jsonPath("$.result.data[0].germplasmDbId")
                        .value(mockSamples.get(0).getGermplasmDbId().toString()))
                .andExpect(jsonPath("$.result.data[0].observationUnitDbId")
                        .value(mockSamples.get(0).getGermplasmPUI()))
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

    @SuppressWarnings("deprecation")
    @Test
    public void testGetSamplesListPageNum() throws Exception {

        Integer pageSize = random.nextInt(100);
        Integer pageNum = random.nextInt(10);

        List<SamplesDTO> mockSamples = createMockSamples(pageSize);

        PagedResult<SamplesDTO> result = new PagedResult<>();

        result.setCurrentPageSize(pageSize);
        result.setCurrentPageNum(pageNum);
        result.setResult(mockSamples);

        when(
                samplesBrapiService.getSamples(
                        eq(pageSize), any(Integer.TYPE),
                        isNull(Integer.class), isNull(Integer.class),
                        isNull(String.class))
        ).thenReturn(result);

        mockMvc.perform(
                MockMvcRequestBuilders.get(
                        "/gobii-dev/brapi/v2/samples")
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

    @SuppressWarnings("unused")
    private List<VariantSetDTO> createMockVariantSets(Integer pageSize) {

        List<VariantSetDTO> returnVal = new ArrayList<>();


        for(int i = 0; i < pageSize; i++) {

            VariantSetDTO variantSet = new VariantSetDTO();


            variantSet.setStudyDbId(random.nextInt(10));
            variantSet.setVariantSetDbId(random.nextInt(2));

            variantSet.setVariantCount(100);
            variantSet.setCallSetCount(1000);

            variantSet.setAvailableFormats(new ArrayList<>());
            FileFormatDTO fileFormat = new FileFormatDTO();
            fileFormat.setFileURL(RandomStringUtils.random(10, true, true));
            fileFormat.setFileFormat(RandomStringUtils.random(4, true, true));
            fileFormat.setDataFormat(RandomStringUtils.random(4, true, true));
            variantSet.getAvailableFormats().add(fileFormat);

            variantSet.setReferenceSetDbId(random.nextInt(2));
            variantSet.setVariantSetName(RandomStringUtils.random(5, true, true));


            returnVal.add(variantSet);

        }

        return returnVal;

    }

}
