package org.gobiiproject.gobidomain.services.brapi;

import org.gobiiproject.gobiimodel.dto.brapi.CallSetDTO;
import org.gobiiproject.gobiisampletrackingdao.DnaRunDaoImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.List;


import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Matchers.any;

/**
 * Created by VCalaminos on 7/18/2019.
 */
@WebAppConfiguration
public class CallSetServiceImplTest {


    @InjectMocks
    private CallSetServiceImpl callSetBrapiService;

    @Mock
    private DnaRunDaoImpl dnaRunDao;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    private CallSetDTO createMockDnaRunDTO() {

        CallSetDTO callSetDTO = new CallSetDTO();

        callSetDTO.setCallSetDbId(34);
        callSetDTO.setCallSetName("test-callset");
        callSetDTO.setGermplasmDbId(1);

        return callSetDTO;

    }


    @Test
    public void getCallsets() throws Exception {

        List<CallSetDTO> callsetsMock = new ArrayList<>();



    }

    @Test
    public void getCallsetById() throws Exception {

    }

}
