package org.gobiiproject.gobidomain.services.impl.brapi;

import org.gobiiproject.gobiidtomapping.entity.noaudit.DtoMapDnaRun;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.CallSetBrapiDTO;
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
public class DnaRunServiceImplTest {


    @Mock
    private DtoMapDnaRun dtoMapDnaRun;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    private CallSetBrapiDTO createMockDnaRunDTO() {

        CallSetBrapiDTO callSetBrapiDTO = new CallSetBrapiDTO();

        callSetBrapiDTO.setCallSetDbId(34);
        callSetBrapiDTO.setCallSetName("test-callset");
        //callSetBrapiDTO.setDnaRunCode("test-code");
        callSetBrapiDTO.setGermplasmDbId(1);

        return callSetBrapiDTO;
    }

    @Test
    public void getCallsets() throws Exception {

        List<CallSetBrapiDTO> callsetsMock = new ArrayList<>();

        CallSetBrapiDTO callset1 = createMockDnaRunDTO();
        callsetsMock.add(callset1);

        when (
                dtoMapDnaRun.getList(
                        any(Integer.TYPE), any(Integer.TYPE), any(CallSetBrapiDTO.class)
                )
        ).thenReturn(callsetsMock);

        //List<CallSetBrapiDTO> callsetsList = dnaRunService.getDnaRuns(any(Integer.TYPE), any(Integer.TYPE), any(CallSetBrapiDTO.class));

        //assertEquals(callsetsMock.size(), callsetsList.size());
        verify(dtoMapDnaRun, times(1)).getList(any(Integer.TYPE), any(Integer.TYPE), any(CallSetBrapiDTO.class));
    }

    @Test
    public void getCallsetById() throws Exception {

        CallSetBrapiDTO callSetBrapiDTOMock = createMockDnaRunDTO();

        when (
                dtoMapDnaRun.get(callSetBrapiDTOMock.getCallSetDbId())
        ).thenReturn(callSetBrapiDTOMock);

        //CallSetBrapiDTO callSetResult = dnaRunService.getDnaRunById(callSetBrapiDTOMock.getCallSetDbId());

        //assertEquals(callSetBrapiDTOMock.getCallSetDbId(), callSetResult.getCallSetDbId());
        //assertEquals(callSetBrapiDTOMock.getCallSetName(), callSetResult.getCallSetName());
        verify(dtoMapDnaRun, times(1)).get(callSetBrapiDTOMock.getCallSetDbId());
    }

}
