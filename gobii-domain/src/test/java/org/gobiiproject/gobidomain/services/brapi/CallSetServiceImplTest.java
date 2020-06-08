package org.gobiiproject.gobidomain.services.brapi;

import org.gobiiproject.gobiimodel.cvnames.CvGroup;
import org.gobiiproject.gobiimodel.dto.brapi.CallSetDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;
import org.gobiiproject.gobiimodel.entity.Cv;
import org.gobiiproject.gobiisampletrackingdao.CvDao;
import org.gobiiproject.gobiisampletrackingdao.CvDaoImpl;
import org.gobiiproject.gobiisampletrackingdao.DnaRunDaoImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by VCalaminos on 7/18/2019.
 */
@WebAppConfiguration
public class CallSetServiceImplTest {

    final int testPageSize = 10;

    @InjectMocks
    private CallSetServiceImpl callSetBrapiService;

    @Mock
    private DnaRunDaoImpl dnaRunDao;

    @Mock
    private CvDaoImpl cvDao;

    Random random = new Random();

    MockSetup mockSetup;

    final Integer pageSize = 10;
    final Integer pageNum = 0;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        mockSetup  = new MockSetup();
    }

    @Test
    public void getCallsets() throws Exception {

        mockSetup.createMockDnaRuns(pageSize);

        when (
            dnaRunDao.getDnaRuns(any(Integer.TYPE), any(Integer.TYPE),
                any(Integer.TYPE), any(String.class),
                any(Integer.TYPE), any(Integer.TYPE),
                any(Integer.TYPE), any(String.class),
                any(Integer.TYPE), any(String.class))
        ).thenReturn(mockSetup.mockDnaRuns);


        when (cvDao.getCvListByCvGroup(
            CvGroup.CVGROUP_DNASAMPLE_PROP.getCvGroupName(), null)).thenReturn(
                new ArrayList<>()
        );

        when (cvDao.getCvListByCvGroup(
            CvGroup.CVGROUP_GERMPLASM_PROP.getCvGroupName(), null))
            .thenReturn(mockSetup.mockGermplasmProps);


        PagedResult<CallSetDTO>  callSetsPageResult =
            callSetBrapiService.getCallSets(
                pageSize, 0, null, new CallSetDTO());

        assertEquals("Page Size mismatch",
            pageSize,
            callSetsPageResult.getCurrentPageSize());

        assertEquals("Wrong page number",
            pageNum,
            callSetsPageResult.getCurrentPageNum());

        for(int i = 0; i < testPageSize; i++) {
            assertEquals("CallSetId : DnaRunId mismatch",
                mockSetup.mockDnaRuns.get(i).getDnaRunId(),
                callSetsPageResult.getResult().get(i).getCallSetDbId());
            assertEquals("CallSetName : DnaRunName mismacth",
                mockSetup.mockDnaRuns.get(i).getDnaRunName(),
                callSetsPageResult.getResult().get(i).getCallSetName());
        }



    }


}
