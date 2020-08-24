package org.gobiiproject.gobidomain.services.gdmv3;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.gobiiproject.gobiimodel.dto.gdmv3.DatasetRequestDTO;
import org.gobiiproject.gobiimodel.entity.Analysis;
import org.gobiiproject.gobiimodel.entity.Experiment;
import org.gobiiproject.gobiisampletrackingdao.AnalysisDao;
import org.gobiiproject.gobiisampletrackingdao.ContactDao;
import org.gobiiproject.gobiisampletrackingdao.CvDao;
import org.gobiiproject.gobiisampletrackingdao.DatasetDao;
import org.gobiiproject.gobiisampletrackingdao.ExperimentDao;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.web.WebAppConfiguration;

@WebAppConfiguration
public class DatasetServiceImplTest {

    @Mock
	private DatasetDao datasetDao;

	@Mock
	private ExperimentDao experimentDao;

	@Mock
	private CvDao cvDao;
	
	@Mock
	private AnalysisDao analysisDao;

	@Mock
	private ContactDao contactDao;

    @InjectMocks
    private DatasetServiceImpl datasetServiceImpl;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @Ignore //for now
    public void testCreateDatasetSimple() throws Exception {
        //Mock request
        DatasetRequestDTO request = new DatasetRequestDTO();
        request.setDatasetName("test-name");
        request.setExperimentId(1);
        request.setCallingAnalysisId(1);

        String user = "test-user";

        when(
            experimentDao.getExperiment(1)
        ).thenReturn(
            new Experiment() //mock experiment
        );

        when(
            analysisDao.getAnalysis(1)
        ).thenReturn(
            new Analysis() //mock analysis
        );

        datasetServiceImpl.createDataset(request, user);

        verify(experimentDao, times(1)).getExperiment(1);
        verify(analysisDao, times(1)).getAnalysis(1);

    }
}