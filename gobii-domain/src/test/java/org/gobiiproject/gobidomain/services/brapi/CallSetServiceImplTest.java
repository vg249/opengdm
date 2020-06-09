package org.gobiiproject.gobidomain.services.brapi;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.gobiiproject.gobiimodel.cvnames.CvGroupTerm;
import org.gobiiproject.gobiimodel.dto.brapi.CallSetDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;
import org.gobiiproject.gobiimodel.entity.Cv;
import org.gobiiproject.gobiimodel.utils.JsonNodeUtils;
import org.gobiiproject.gobiisampletrackingdao.CvDaoImpl;
import org.gobiiproject.gobiisampletrackingdao.DnaRunDaoImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
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
            CvGroupTerm.CVGROUP_DNASAMPLE_PROP.getCvGroupName(), null)).thenReturn(
                new ArrayList<>()
        );

        when (cvDao.getCvListByCvGroup(
            CvGroupTerm.CVGROUP_GERMPLASM_PROP.getCvGroupName(), null))
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
            assertEquals("CallSetId : DnaRunId mapping failed",
                mockSetup.mockDnaRuns.get(i).getDnaRunId(),
                callSetsPageResult.getResult().get(i).getCallSetDbId());
            assertEquals("CallSetName : DnaRunName mapping failed",
                mockSetup.mockDnaRuns.get(i).getDnaRunName(),
                callSetsPageResult.getResult().get(i).getCallSetName());

            assertEquals("StudyDbId : ExperimentId mapping failed",
                mockSetup.mockDnaRuns.get(i).getExperiment().getExperimentId(),
                callSetsPageResult.getResult().get(i).getStudyDbId());

            assertEquals("SampleDbId : DnaSampleId mapping failed",
                mockSetup.mockDnaRuns.get(i).getDnaSample().getDnaSampleId(),
                callSetsPageResult.getResult().get(i).getSampleDbId());

            assertEquals("SampleName : DnaSampleName mapping failed",
                mockSetup.mockDnaRuns.get(i).getDnaSample().getDnaSampleName(),
                callSetsPageResult.getResult().get(i).getSampleName());

            if(!CollectionUtils.isEmpty(
                callSetsPageResult.getResult().get(i).getVariantSetIds())) {

                assertTrue("VariantSetId : DatasetIds mapping failed",
                    !JsonNodeUtils.isEmpty(
                        mockSetup.mockDnaRuns.get(i).getDatasetDnaRunIdx()));

                assertEquals("VariantSetId : DatasetIds mapping failed",
                    mockSetup.mockDnaRuns.get(i).getDatasetDnaRunIdx().size(),
                    callSetsPageResult.getResult().get(i)
                        .getVariantSetIds().size()
                );

                for (Integer variantSetId :
                    callSetsPageResult.getResult().get(i).getVariantSetIds()) {

                    assertTrue("VariantSetId : DatasetIds mapping failed",
                        mockSetup.mockDnaRuns
                            .get(i).getDatasetDnaRunIdx()
                            .has(variantSetId.toString()));

                }
            }

            if(!MapUtils.isEmpty(
                callSetsPageResult.getResult().get(i).getAdditionalInfo())) {

                assertTrue("AdditionalInfo : DnaSample.Properties " +
                        "mapping failed",
                    !(
                        JsonNodeUtils.isEmpty(
                            mockSetup.mockDnaRuns.get(i)
                            .getDnaSample().getProperties()) ||
                        JsonNodeUtils.isEmpty(
                            mockSetup.mockDnaRuns.get(i)
                                .getDnaSample().getGermplasm().getProperties())
                    ));

                for(String infoKey :
                    callSetsPageResult.getResult()
                        .get(i).getAdditionalInfo().keySet()) {


                    Optional<Cv> cvDnaSampleProps = mockSetup.mockDnaSampleProps
                        .stream()
                        .parallel()
                        .filter(cv -> cv.getTerm() == infoKey)
                        .findFirst();

                    Optional<Cv> cvGermplasmProps = mockSetup.mockGermplasmProps
                        .stream()
                        .parallel()
                        .filter(cv -> cv.getTerm() == infoKey)
                        .findFirst();

                    if(cvDnaSampleProps.isPresent()) {
                        assertTrue("AdditionalInfo : DnaSample.Properties " +
                                "mapping failed",
                            mockSetup.mockDnaRuns
                                .get(i).getDnaSample()
                                .getProperties().get(
                                cvDnaSampleProps.get().getCvId().toString())
                                .asText()
                                == callSetsPageResult.getResult()
                                .get(i).getAdditionalInfo().get(infoKey)
                        );
                    }
                    else if(cvGermplasmProps.isPresent()) {
                        assertTrue("AdditionalInfo : Germplasm.Properties " +
                                "mapping failed",
                            mockSetup.mockDnaRuns
                                .get(i).getDnaSample().getGermplasm()
                                .getProperties().get(
                                cvGermplasmProps.get().getCvId().toString())
                                .asText()
                                == callSetsPageResult.getResult()
                                .get(i).getAdditionalInfo().get(infoKey)
                        );
                    }
                    else {
                        fail("AdditionalInfo : Germplasm.Properties " +
                            "mapping failed");
                    }

                }
            }


        }



    }


}
