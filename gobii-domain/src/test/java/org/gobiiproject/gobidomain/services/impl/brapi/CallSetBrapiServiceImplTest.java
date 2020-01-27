package org.gobiiproject.gobidomain.services.impl.brapi;

import org.gobiiproject.gobiidtomapping.entity.noaudit.DtoMapDnaRun;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.CallSetBrapiDTO;
import org.gobiiproject.gobiimodel.entity.DnaRun;
import org.gobiiproject.gobiisampletrackingdao.DnaRunDao;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.any;

/**
 * Created by VCalaminos on 7/18/2019.
 */
@WebAppConfiguration
public class CallSetBrapiServiceImplTest {


    @InjectMocks
    private CallSetBrapiServiceImpl callSetBrapiService;

    @Mock
    private DnaRunDaoImpl dnaRunDao;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    private CallSetBrapiDTO createMockDnaRunDTO() {

        CallSetBrapiDTO callSetBrapiDTO = new CallSetBrapiDTO();

        callSetBrapiDTO.setCallSetDbId(34);
        callSetBrapiDTO.setCallSetName("test-callset");
        callSetBrapiDTO.setGermplasmDbId(1);

        return callSetBrapiDTO;

    }


    @Test
    public void getCallsets() throws Exception {

        List<CallSetBrapiDTO> callsetsMock = new ArrayList<>();



    }

    @Test
    public void getCallsetById() throws Exception {

    }

}
