package org.gobiiproject.gobidomain.services.brapi;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.lang.RandomStringUtils;
import org.gobiiproject.gobiimodel.dto.brapi.VariantSetDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;
import org.gobiiproject.gobiimodel.entity.Analysis;
import org.gobiiproject.gobiimodel.entity.Dataset;
import org.gobiiproject.gobiisampletrackingdao.CvDaoImpl;
import org.gobiiproject.gobiisampletrackingdao.DatasetDaoImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.web.WebAppConfiguration;

@WebAppConfiguration
public class VariantSetsServiceImplTest {

    @InjectMocks
    private VariantSetsServiceImpl variansetService;

    @Mock
    private DatasetDaoImpl datasetDao;

    @Mock
    private CvDaoImpl cvDao;

    MockSetup mockSetup;

    Integer pageSize = 10;
    Integer pageNum = 0;

    Random random = new Random();


    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        mockSetup  = new MockSetup();
        mockSetup.createMockDatasets(pageSize);
    }



    @Test
    public void testMainFieldsMapping() {

        final Integer pageSize = 1000;


        when (
            datasetDao.getDatasets(
                pageSize, 0,
                any(Integer.TYPE), any(String.class),
                any(Integer.TYPE), any(String.class))
        ).thenReturn(mockSetup.mockDatasets);

        PagedResult<VariantSetDTO> pagedResult = variansetService.getVariantSets(
            pageSize, 0, null, null, null, null);


        List<VariantSetDTO> variantSets = pagedResult.getResult();


        assertEquals("Size mismatch", datasetsMock.size(), variantSets.size());

        for(int i = 0; i < 10; i++) {

            Integer assertIndex = new Random().nextInt(1000);

            assertEquals("variansetName check failed",
                    datasetsMock.get(assertIndex).getDatasetName(),
                    variantSets.get(assertIndex).getVariantSetName());

            assertEquals("variansetid check failed",
                    datasetsMock.get(assertIndex).getDatasetId(),
                    variantSets.get(assertIndex).getVariantSetDbId());

            assertEquals("studyDbId check failed",
                    datasetsMock.get(assertIndex).getExperiment().getExperimentId(),
                    variantSets.get(assertIndex).getStudyDbId());

            assertEquals("referenceDbId check failed",
                    datasetsMock.get(assertIndex).getCallingAnalysis().getReference().getReferenceId(),
                    variantSets.get(assertIndex).getReferenceSetDbId());

            assertEquals("created check failed",
                    datasetsMock.get(assertIndex).getCreatedDate(),
                    variantSets.get(assertIndex).getCreated());

            assertEquals("updated check failed",
                    datasetsMock.get(assertIndex).getModifiedDate(),
                    variantSets.get(assertIndex).getUpdated());


            //Only one analysis added in mock, so, just check the mapping for that one is correct
            //assertTrue("Analysis got mapped to the Variantset Analysis",
            //        variantSets.get(assertIndex).getAnalyses().iterator().hasNext());

            //Analysis analysis = datasetsMock.get(assertIndex).getMappedAnalyses().iterator().next();

            //AnalysisDTO analysisDTO = variantSets.get(assertIndex).getAnalyses().iterator().next();

            //assertEquals("check analysisDbId is mapped",
            //        analysis.getAnalysisId(),
            //        analysisDTO.getAnalysisDbId());


            //assertEquals("check analysisName is mapped",
            //        analysis.getAnalysisName(),
            //        analysisDTO.getAnalysisName());

            //assertEquals("check analysis description is mapped",
            //        analysis.getDescription(),
            //        analysisDTO.getDescription());



        }

    }



}
