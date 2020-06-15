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
import org.gobiiproject.gobiimodel.entity.Experiment;
import org.gobiiproject.gobiisampletrackingdao.CvDaoImpl;
import org.gobiiproject.gobiisampletrackingdao.DatasetDaoImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.xml.crypto.Data;

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


    @SuppressWarnings("unused")
    private Analysis createMockAnalysis(Integer analysisId) {

        Analysis analysis = new Analysis();

        analysis.setAnalysisId(analysisId);
        analysis.setAnalysisName("test-analysis-"+analysisId.toString());
        analysis.getType().setTerm("calling");

        return analysis;

    }


    private List<Object[]> getMockDatasets(Integer listSize) {

        List<Object[]> returnVal = new ArrayList<>();


        for(int i = 0; i < listSize; i++) {

            Dataset dataset = new Dataset();

            dataset.setDatasetId(i+1);

            dataset.setDatasetName(RandomStringUtils.random(7, true, true));

            Experiment experiment = new Experiment();
            experiment.setExperimentId(i);
            dataset.setExperiment(experiment);


            //Only one analysis added in mock, so, just check the mapping for that one is correct
            Analysis analysis = new Analysis();

            analysis.setDescription(RandomStringUtils.random(7, true, true));
            analysis.setAnalysisName(RandomStringUtils.random(7, true, true));
            analysis.setAnalysisId(i);

            dataset.setCallingAnalysis(analysis);


            Integer[] analyses = {i};

            dataset.setAnalyses(analyses);

            dataset.setCreatedDate(new Date(random.nextLong()));
            dataset.setModifiedDate(new Date(random.nextLong()));

            Object[] tuple = {dataset, analysis, random.nextInt(1000), random.nextInt(1000)};
            returnVal.add(tuple);
        }


        return returnVal;
   }

    @Test
    public void testMainFieldsMapping() throws Exception {

        final Integer pageSize = 1000;

        List<Object[]> datasetsMock = getMockDatasets(pageSize);

        when (
                datasetDao.getDatasetsWithAnalysesAndCounts(
                        any(Integer.TYPE), any(Integer.TYPE),
                        any(Integer.TYPE), any(String.class),
                        any(Integer.TYPE),any(String.class))
        ).thenReturn(datasetsMock);

        PagedResult<VariantSetDTO> pagedResult = variansetService.getVariantSets(
                pageSize,0, 1, "", 1, "");


        List<VariantSetDTO> variantSets = pagedResult.getResult();


        assertEquals("Size mismatch", datasetsMock.size(), variantSets.size());

        for(int i = 0; i < 10; i++) {

            Integer assertIndex = new Random().nextInt(1000);

            assertEquals("variansetName check failed",
                ((Dataset)datasetsMock.get(assertIndex)[0]).getDatasetName(),
                    variantSets.get(assertIndex).getVariantSetName());

            assertEquals("variansetid check failed",
                    ((Dataset)datasetsMock.get(assertIndex)[0]).getDatasetId(),
                    variantSets.get(assertIndex).getVariantSetDbId());

            assertEquals("studyDbId check failed",
                    ((Dataset)datasetsMock.get(assertIndex)[0]).getExperiment().getExperimentId(),
                    variantSets.get(assertIndex).getStudyDbId());

            assertEquals("created check failed",
                    ((Dataset)datasetsMock.get(assertIndex)[0]).getCreatedDate(),
                    variantSets.get(assertIndex).getCreated());

            assertEquals("updated check failed",
                    ((Dataset)datasetsMock.get(assertIndex)[0]).getModifiedDate(),
                    variantSets.get(assertIndex).getUpdated());


            //Only one analysis added in mock, so, just check the mapping for that one is correct
            //assertTrue("Analysis got mapped to the Variantset Analysis",
            //        variantSets.get(assertIndex).getAnalyses().iterator().hasNext());

            //Analysis analysis = ((Dataset)datasetsMock.get(assertIndex)[0]).getMappedAnalyses().iterator().next();

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
