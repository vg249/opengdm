package org.gobiiproject.gobidomain.services.impl.brapi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang.RandomStringUtils;
import org.gobiiproject.gobidomain.services.SamplesBrapiService;
import org.gobiiproject.gobidomain.services.impl.DnaRunServiceImpl;
import org.gobiiproject.gobiidtomapping.entity.noaudit.DtoMapDnaRun;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.DnaRunDTO;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.SamplesBrapiDTO;
import org.gobiiproject.gobiimodel.entity.Cv;
import org.gobiiproject.gobiimodel.entity.DnaSample;
import org.gobiiproject.gobiimodel.entity.Germplasm;
import org.gobiiproject.gobiimodel.types.GobiiCvGroupType;
import org.gobiiproject.gobiisampletrackingdao.CvDaoImpl;
import org.gobiiproject.gobiisampletrackingdao.DnaSampleDao;
import org.gobiiproject.gobiisampletrackingdao.DnaSampleDaoImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.*;

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

    @Mock
    private CvDaoImpl cvDao;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    private List<DnaSample> getMockDnaSamples(Integer listSize) {

        List<DnaSample> returnVal = new ArrayList();


        for(int i = 0; i < listSize; i++) {

            DnaSample dnaSample = new DnaSample();
            JsonNode properties = JsonNodeFactory.instance.objectNode();

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

            ((ObjectNode)properties).put("1", "testValue");

            dnaSample.setProperties(properties);

            returnVal.add(dnaSample);

        }



        return returnVal;
   }

    @Test
    public void testMappableFields() throws Exception {

        List<DnaSample> samplesMock = getMockDnaSamples(1000);

        when (
                dnaSampleDao.getDnaSamples(
                        any(Integer.TYPE), any(Integer.TYPE), null)
        ).thenReturn(samplesMock);


        List<SamplesBrapiDTO> samplesBrapi = samplesBrapiService.getSamples(any(Integer.TYPE), any(Integer.TYPE));

        assertEquals("Size mismatch", samplesMock.size(), samplesBrapi.size());

        for(int i = 0; i < 10; i++) {

            Integer assertIndex = new Random().nextInt(1000);

            assertEquals("germplasmDbId check failed",
                    samplesMock.get(assertIndex).getGermplasm().getGermplasmId(),
                    samplesBrapi.get(assertIndex).getGermplasmDbId());

            assertEquals("sampleDbId check failed!",
                    samplesMock.get(assertIndex).getDnaSampleId(),
                    samplesBrapi.get(assertIndex).getSampleDbId());

            assertEquals("observationUnitDbId check failed!",
                    samplesMock.get(assertIndex).getGermplasm().getExternalCode(),
                    samplesBrapi.get(assertIndex).getObservationUnitDbId());

            assertEquals("sampelName check failed!",
                    samplesMock.get(assertIndex).getDnaSampleName(),
                    samplesBrapi.get(assertIndex).getSampleName());


            assertEquals("sampleNum check failed!",
                    samplesMock.get(assertIndex).getDnaSampleNum(),
                    samplesBrapi.get(assertIndex).getWell());

            assertEquals("samplePUI check failed!",
                    samplesMock.get(assertIndex).getDnaSampleUuid(),
                    samplesBrapi.get(assertIndex).getSamplePUI());

            assertEquals("projectId check failed!",
                    samplesMock.get(assertIndex).getProjectId(),
                    samplesBrapi.get(assertIndex).getSampleGroupDbId());
        }

    }

    @Test
    public void testWithAdditionalInfo() throws Exception {

        List<DnaSample> samplesMock = getMockDnaSamples(1000);

        List cvListMock = new ArrayList();

        Cv dnaSampleCv = new Cv();
        dnaSampleCv.setCvId(1);
        dnaSampleCv.setTerm("testTerm");

        cvListMock.add(dnaSampleCv);

        when (
                dnaSampleDao.getDnaSamples(
                        any(Integer.TYPE), any(Integer.TYPE), null)
        ).thenReturn(samplesMock);

        when (
                cvDao.getCvListByCvGroup(
                        any(String.class), any(GobiiCvGroupType.class))
        ).thenReturn(cvListMock);


        List<SamplesBrapiDTO> samplesBrapi = samplesBrapiService.getSamples(any(Integer.TYPE), any(Integer.TYPE));

        assertEquals(samplesMock.size(), samplesBrapi.size());
    }


}
