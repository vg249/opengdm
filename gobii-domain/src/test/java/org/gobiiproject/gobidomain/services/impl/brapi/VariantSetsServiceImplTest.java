package org.gobiiproject.gobidomain.services.impl.brapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.RandomStringUtils;
import org.gobiiproject.gobiimodel.dto.entity.auditable.VariantSetDTO;
import org.gobiiproject.gobiimodel.entity.Dataset;
import org.gobiiproject.gobiisampletrackingdao.CvDaoImpl;
import org.gobiiproject.gobiisampletrackingdao.DatasetDaoImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@WebAppConfiguration
public class VariantSetsServiceImplTest {

    @InjectMocks
    private VariantSetsServiceImpl variansetService;

    @Mock
    private DatasetDaoImpl datasetDao;

    @Mock
    private CvDaoImpl cvDao;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    Random random = new Random();

    ObjectMapper mapper = new ObjectMapper();

    private List<Dataset> getMockDatasets(Integer listSize) {

        List<Dataset> returnVal = new ArrayList();


        for(int i = 0; i < listSize; i++) {

            Dataset dataset = new Dataset();

            dataset.setDatasetId(i+1);
            dataset.setDatasetName(RandomStringUtils.random(7, true, true));
            dataset.getExperiment().setExperimentId(i);

            dataset.getCallingAnalysis().getReference().setReferenceId(i);

            dataset.getCallingAnalysis().getType().setCvId(i*10);
            dataset.getCallingAnalysis().getType().setTerm(RandomStringUtils.random(7, true, true));

            returnVal.add(dataset);
        }


        return returnVal;
   }

    @Test
    public void testMainFieldsMapping() throws Exception {

        final Integer pageSize = 1000;

        List<Dataset> datasetsMock = getMockDatasets(pageSize);

        when (
                datasetDao.listDatasetsByPageNum(any(Integer.TYPE), any(Integer.TYPE), any(Integer.TYPE))
        ).thenReturn(datasetsMock);


        List<VariantSetDTO> variantSets = variansetService.listVariantSets(
                0, pageSize, null);

        assertEquals("Size mismatch", datasetsMock.size(), variantSets.size());

        //for(int i = 0; i < 10; i++) {

        //    Integer assertIndex = new Random().nextInt(1000);

        //    assertEquals("germplasmDbId check failed",
        //            samplesMock.get(assertIndex).getGermplasm().getGermplasmId(),
        //            samplesBrapi.get(assertIndex).getGermplasmDbId());

        //    assertEquals("sampleDbId check failed!",
        //            samplesMock.get(assertIndex).getDnaSampleId(),
        //            samplesBrapi.get(assertIndex).getSampleDbId());

        //    assertEquals("observationUnitDbId check failed!",
        //            samplesMock.get(assertIndex).getGermplasm().getExternalCode(),
        //            samplesBrapi.get(assertIndex).getObservationUnitDbId());

        //    assertEquals("sampelName check failed!",
        //            samplesMock.get(assertIndex).getDnaSampleName(),
        //            samplesBrapi.get(assertIndex).getSampleName());


        //    assertEquals("sampleNum check failed!",
        //            samplesMock.get(assertIndex).getDnaSampleNum(),
        //            samplesBrapi.get(assertIndex).getWell());

        //    assertEquals("samplePUI check failed!",
        //            samplesMock.get(assertIndex).getDnaSampleUuid(),
        //            samplesBrapi.get(assertIndex).getSamplePUI());

        //    assertEquals("projectId check failed!",
        //            samplesMock.get(assertIndex).getProjectId(),
        //            samplesBrapi.get(assertIndex).getSampleGroupDbId());
        //}

    }



}
