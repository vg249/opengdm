package org.gobiiproject.gobidomain.services.brapi;

import org.apache.commons.lang.RandomStringUtils;
import org.gobiiproject.gobiimodel.dto.brapi.CallSetDTO;
import org.gobiiproject.gobiimodel.entity.DnaRun;
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

@WebAppConfiguration
public class CallSetServiceImplTest {

    final int testPageSize = 10;

    @InjectMocks
    private CallSetServiceImpl callSetBrapiService;

    @Mock
    private DnaRunDaoImpl dnaRunDao;

    Random random = new Random();

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    private List<DnaRun> createMockDnaRuns(int numOfDnaRuns) {

        List<DnaRun> dnaRuns = new ArrayList<>();

        for(int i = 0; i < numOfDnaRuns; i++) {
            DnaRun dnaRun = new DnaRun();
            dnaRun.setDnaRunName(RandomStringUtils.random(7, true, true));

        }

        return dnaRuns;
    }


    @Test
    public void getCallsets() throws Exception {

        //List<CallSetDTO> callsetsMock = new ArrayList<>();



    }

    @Test
    public void getCallsetById() throws Exception {

    }

}
