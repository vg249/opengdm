package org.gobiiproject.gobiiweb.controllers.brapi.v2;

import edu.emory.mathcs.backport.java.util.Arrays;
import org.apache.commons.lang.RandomStringUtils;
import org.gobiiproject.gobiidomain.services.brapi.CallSetService;
import org.gobiiproject.gobiidomain.services.brapi.GenotypeCallsService;
import org.gobiiproject.gobiimodel.dto.brapi.CallSetDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebAppConfiguration
public class CallSetsControllerTest {

    @InjectMocks
    private CallSetsController callSetsController;

    @Mock
    private CallSetService callSetService;

    @Mock
    private GenotypeCallsService genotypeCallsService;

    private MockMvc mockMvc;

    Random random = new Random();

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(callSetsController).build();
    }

    private PagedResult<CallSetDTO> getMockCallSets(int pageSize, int pageNum) {

        PagedResult<CallSetDTO> pagedResult = new PagedResult<>();

        List<CallSetDTO> mockCallSets = new ArrayList<>();

        for(int i = 0; i < pageSize; i++) {
            CallSetDTO callSetDTO = new CallSetDTO();
            callSetDTO.setCallSetDbId(i);
            callSetDTO.setCallSetName(RandomStringUtils.random(7, true, true));
            callSetDTO.setGermplasmDbId(random.nextInt(pageSize));
            callSetDTO.setGermplasmType(RandomStringUtils.random(7, true, true));
            callSetDTO.setSampleDbId(random.nextInt(pageSize));
            callSetDTO.setSampleName(RandomStringUtils.random(7, true, true));
            List<String> variantSetIds = new ArrayList<String>(){{
                add("1"); add("2"); add("3");}};
            callSetDTO.setVariantSetDbIds(variantSetIds);
            mockCallSets.add(callSetDTO);
        }

        pagedResult.setCurrentPageNum(pageNum);
        pagedResult.setCurrentPageSize(mockCallSets.size());
        pagedResult.setResult(mockCallSets);

        return pagedResult;
    }

    @Test
    public void getCallSetWithPageAndPageSizeTest() throws Exception {

        Integer pageSize = 10;
        Integer pageNum = 4;

        when(
            callSetService.getCallSets(eq(pageSize), eq(pageNum),
                any(), any())
        ).thenReturn(getMockCallSets(pageSize, pageNum));

        mockMvc.perform(
            MockMvcRequestBuilders.get(
                "/gobii-dev/brapi/v2/callsets")
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

    @Test
    public void getCallSetsWithNoParams() throws Exception {

        Integer defaultPageSize = 1000;
        Integer defaultPageNum = 0;

        Integer totalCallSets = 10;

        when(
            callSetService.getCallSets(eq(defaultPageSize), eq(defaultPageNum),
                any(), any())
        ).thenReturn(getMockCallSets(totalCallSets, defaultPageNum));

        mockMvc.perform(
            MockMvcRequestBuilders.get(
                "/gobii-dev/brapi/v2/callsets")
                .contextPath("/gobii-dev")).andDo(print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.metadata").exists())
            .andExpect(jsonPath("$.metadata.pagination").exists())
            .andExpect(jsonPath("$.metadata.pagination.currentPage").exists())
            .andExpect(jsonPath("$.metadata.pagination.pageSize").value(totalCallSets))
            .andExpect(jsonPath("$.metadata.pagination.currentPage").value(defaultPageNum));

    }




}
