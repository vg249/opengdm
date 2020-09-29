package org.gobiiproject.gobiiweb.controllers.brapi.v2;

import edu.emory.mathcs.backport.java.util.Arrays;
import org.apache.commons.lang.RandomStringUtils;
import org.gobiiproject.gobiidomain.services.brapi.CallSetService;
import org.gobiiproject.gobiidomain.services.brapi.GenotypeCallsService;
import org.gobiiproject.gobiimodel.dto.brapi.CallSetDTO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    private List<CallSetDTO> getMockCallSets(int pageSize) {

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

        return mockCallSets;
    }

    @Test
    public void getCallSetsTest() throws Exception {
        Integer pageSize = 10;

    }

}
