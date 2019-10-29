package org.gobiiproject.gobidomain.services.impl.brapi;

import org.apache.commons.lang.RandomStringUtils;
import org.gobiiproject.gobidomain.services.SamplesBrapiService;
import org.gobiiproject.gobidomain.services.impl.DnaRunServiceImpl;
import org.gobiiproject.gobiidtomapping.entity.noaudit.DtoMapDnaRun;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.DnaRunDTO;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.SamplesBrapiDTO;
import org.gobiiproject.gobiimodel.entity.DnaSample;
import org.gobiiproject.gobiimodel.entity.Germplasm;
import org.gobiiproject.gobiisampletrackingdao.DnaSampleDao;
import org.gobiiproject.gobiisampletrackingdao.DnaSampleDaoImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Created by VCalaminos on 7/18/2019.
 */
@WebAppConfiguration
public class SamplesBrapiServiceImplTest {

    @InjectMocks
    private SamplesBrapiServiceImpl samplesBrapiService;

    @Mock
    private DnaSampleDaoImpl dnaSampleDao;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    private List<DnaSample> getMockDnaSamples(Integer listSize) {

        List<DnaSample> returnVal = new ArrayList();

        for(int i = 0; i < listSize; i++) {

            DnaSample dnaSample = new DnaSample();

            dnaSample.setDnaSampleId(i+1);
            dnaSample.setDnaSampleName(RandomStringUtils.random(7, true, true));
            dnaSample.setDnaSampleNum(RandomStringUtils.random(4, false, true));
            dnaSample.setDnaSampleUuid(UUID.randomUUID().toString());
            dnaSample.setProjectId(i);

            Germplasm germplasm = new Germplasm();
            germplasm.setGermplasmId(i);
            germplasm.setGermplasmName(RandomStringUtils.random(7, true, true));
            germplasm.setExternalCode(UUID.randomUUID().toString());

            dnaSample.setGermplasm(germplasm);

            returnVal.add(dnaSample);

        }



        return returnVal;
   }

    @Test
    public void getSamples() throws Exception {

        List<DnaSample> samplesMock = new ArrayList<>();

        samplesMock = getMockDnaSamples(10);

        when (
                dnaSampleDao.getDnaSamples(
                        any(Integer.TYPE), any(Integer.TYPE))
        ).thenReturn(samplesMock);

        List<SamplesBrapiDTO> samplesBrapi = samplesBrapiService.getSamples(any(Integer.TYPE), any(Integer.TYPE));

        assertEquals(samplesMock.size(), samplesBrapi.size());
    }


}
