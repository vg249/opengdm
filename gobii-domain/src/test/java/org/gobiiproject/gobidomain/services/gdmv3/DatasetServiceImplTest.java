package org.gobiiproject.gobidomain.services.gdmv3;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiimodel.cvnames.CvGroupTerm;
import org.gobiiproject.gobiimodel.dto.gdmv3.DatasetRequestDTO;
import org.gobiiproject.gobiimodel.entity.Analysis;
import org.gobiiproject.gobiimodel.entity.Contact;
import org.gobiiproject.gobiimodel.entity.Cv;
import org.gobiiproject.gobiimodel.entity.CvGroup;
import org.gobiiproject.gobiimodel.entity.Dataset;
import org.gobiiproject.gobiimodel.entity.Experiment;
import org.gobiiproject.gobiimodel.entity.Project;
import org.gobiiproject.gobiisampletrackingdao.AnalysisDao;
import org.gobiiproject.gobiisampletrackingdao.ContactDao;
import org.gobiiproject.gobiisampletrackingdao.CvDao;
import org.gobiiproject.gobiisampletrackingdao.DatasetDao;
import org.gobiiproject.gobiisampletrackingdao.ExperimentDao;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
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

    @Test(expected = GobiiDaoException.class)
    public void testCreateDatasetNullExperiment() throws Exception {
        //Mock request
        DatasetRequestDTO request = new DatasetRequestDTO();
        request.setDatasetName("test-name");
        request.setExperimentId(1);
        request.setCallingAnalysisId(1);

        when(experimentDao.getExperiment(1)).thenReturn(null);

        datasetServiceImpl.createDataset(request, "test-user");

    }

    @Test
    public void testCreateDatasetSimple() throws Exception {
        //Mock request
        DatasetRequestDTO request = new DatasetRequestDTO();
        request.setDatasetName("test-name");
        request.setExperimentId(1);
        request.setCallingAnalysisId(1);

        String user = "test-user";

        Experiment mockExperiment = new Experiment();
        mockExperiment.setExperimentId(1);
        mockExperiment.setExperimentName("test-experiment");
        
        Project mockProject = new Project();
        mockProject.setProjectId(1);
        mockProject.setProjectName("test-project");

        Contact mockContact = new Contact();
        mockContact.setContactId(1);
        mockContact.setFirstName("firstName");
        mockContact.setLastName("lastName");
         
        mockProject.setContact(mockContact);
        mockExperiment.setProject(mockProject);

        when(
            experimentDao.getExperiment(1)
        ).thenReturn(
            mockExperiment //mock experiment
        );
        
        Analysis mockAnalysis = new Analysis();
        mockAnalysis.setAnalysisName("test-name");
        mockAnalysis.setAnalysisId(1);


       
        
        when(
            analysisDao.getAnalysis(1)
        ).thenReturn(
            mockAnalysis
        );

        Cv mockNewStatus = new Cv();
        mockNewStatus.setTerm("new");
        mockNewStatus.setCvId(1);

        when( cvDao.getNewStatus()).thenReturn(mockNewStatus);
        Contact mockEditor = new Contact();
        mockEditor.setContactId(2);
        mockEditor.setUsername("test-user");
        when(contactDao.getContactByUsername("test-user")).thenReturn(
            mockEditor
        );

        Dataset mockDataset = new Dataset();
        mockDataset.setExperiment(mockExperiment);

        when(datasetDao.saveDataset(any(Dataset.class))).thenReturn(
            mockDataset
        );
        
        ArgumentCaptor<Dataset> arg = ArgumentCaptor.forClass(Dataset.class);
        datasetServiceImpl.createDataset(request, user);

        verify(experimentDao, times(1)).getExperiment(1);
        verify(analysisDao, times(1)).getAnalysis(1);
        verify(datasetDao).saveDataset(arg.capture());

        assertTrue(arg.getValue().getDatasetName().equals("test-name"));

    }

    @Test(expected = GobiiDaoException.class)
    public void testCreateAnalysisCallingAnalysisNotFound() throws Exception {
        //Mock request
        DatasetRequestDTO request = new DatasetRequestDTO();
        request.setDatasetName("test-name");
        request.setExperimentId(1);
        request.setCallingAnalysisId(1);

        when(experimentDao.getExperiment(1)).thenReturn(new Experiment());

        when(analysisDao.getAnalysis(1)).thenReturn(null);

        datasetServiceImpl.createDataset(request, "test-user");
    }

    @Test
    public void testCreateAnalysisWithDatasetTypeId() throws Exception {
        //Mock request
        DatasetRequestDTO request = new DatasetRequestDTO();
        request.setDatasetName("test-name");
        request.setExperimentId(1);
        request.setCallingAnalysisId(1);
        request.setDatasetTypeId(2);

        String user = "test-user";

        Experiment mockExperiment = new Experiment();
        mockExperiment.setExperimentId(1);
        mockExperiment.setExperimentName("test-experiment");
        
        Project mockProject = new Project();
        mockProject.setProjectId(1);
        mockProject.setProjectName("test-project");

        Contact mockContact = new Contact();
        mockContact.setContactId(1);
        mockContact.setFirstName("firstName");
        mockContact.setLastName("lastName");
         
        mockProject.setContact(mockContact);
        mockExperiment.setProject(mockProject);

        when(
            experimentDao.getExperiment(1)
        ).thenReturn(
            mockExperiment //mock experiment
        );
        
        Analysis mockAnalysis = new Analysis();
        mockAnalysis.setAnalysisName("test-name");
        mockAnalysis.setAnalysisId(1);

        when(
            analysisDao.getAnalysis(1)
        ).thenReturn(
            mockAnalysis
        );

        Cv mockDatasetTypeCv = new Cv();
        CvGroup cvGroup = new CvGroup();
        cvGroup.setCvGroupName(CvGroupTerm.CVGROUP_DATASET_TYPE.getCvGroupName());
        mockDatasetTypeCv.setCvGroup(cvGroup);
        mockDatasetTypeCv.setCvId(2);

        when(cvDao.getCvByCvId(2)).thenReturn(
            mockDatasetTypeCv
        );

        Cv mockNewStatus = new Cv();
        mockNewStatus.setTerm("new");
        mockNewStatus.setCvId(1);

        when( cvDao.getNewStatus()).thenReturn(mockNewStatus);
        Contact mockEditor = new Contact();
        mockEditor.setContactId(2);
        mockEditor.setUsername("test-user");
        when(contactDao.getContactByUsername("test-user")).thenReturn(
            mockEditor
        );

        Dataset mockDataset = new Dataset();
        mockDataset.setExperiment(mockExperiment);

        when(datasetDao.saveDataset(any(Dataset.class))).thenReturn(
            mockDataset
        );
        
        ArgumentCaptor<Dataset> arg = ArgumentCaptor.forClass(Dataset.class);
        datasetServiceImpl.createDataset(request, user);

        verify(experimentDao, times(1)).getExperiment(1);
        verify(analysisDao, times(1)).getAnalysis(1);
        verify(datasetDao).saveDataset(arg.capture());

        assertTrue(arg.getValue().getDatasetName().equals("test-name"));
        assertTrue(arg.getValue().getType().getCvId() == 2);
    }


    @Test
    public void testCreateWithAnalysisIds() throws Exception {
        //Mock request
        DatasetRequestDTO request = new DatasetRequestDTO();
        request.setDatasetName("test-name");
        request.setExperimentId(1);
        request.setCallingAnalysisId(1);
        request.setAnalysisIds(new Integer[]{10, 11});

        String user = "test-user";

        Experiment mockExperiment = getMockExperiment();

        when(
            experimentDao.getExperiment(1)
        ).thenReturn(
            mockExperiment //mock experiment
        );
        
        Analysis mockAnalysis = new Analysis();
        mockAnalysis.setAnalysisName("test-name");
        mockAnalysis.setAnalysisId(1);   
        when(
            analysisDao.getAnalysis(1)
        ).thenReturn(
            mockAnalysis
        );

        Cv mockNewStatus = new Cv();
        mockNewStatus.setTerm("new");
        mockNewStatus.setCvId(1);

        when( cvDao.getNewStatus()).thenReturn(mockNewStatus);
        Contact mockEditor = new Contact();
        mockEditor.setContactId(2);
        mockEditor.setUsername("test-user");
        when(contactDao.getContactByUsername("test-user")).thenReturn(
            mockEditor
        );

        Dataset mockDataset = new Dataset();
        mockDataset.setExperiment(mockExperiment);

        List<Analysis> mockAnalysisList = this.getMockAnalysisList();

        when(analysisDao.getAnalysesByAnalysisIds(anySet())).thenReturn(
            mockAnalysisList
        );

        when(datasetDao.saveDataset(any(Dataset.class))).thenReturn(
            mockDataset
        );
        
        ArgumentCaptor<Dataset> arg = ArgumentCaptor.forClass(Dataset.class);
        datasetServiceImpl.createDataset(request, user);

        verify(experimentDao, times(1)).getExperiment(1);
        verify(analysisDao, times(1)).getAnalysis(1);
        verify(datasetDao).saveDataset(arg.capture());

        assertTrue(arg.getValue().getDatasetName().equals("test-name"));
        assertTrue(arg.getValue().getAnalyses().length == 2);
    }


    @Test(expected = GobiiDaoException.class)
    public void testUpdateDatasetNotFound() throws Exception {
        when(datasetDao.getDataset(123)).thenReturn(null);

        DatasetRequestDTO request = new DatasetRequestDTO();
        datasetServiceImpl.updateDataset(123, request, "test-user");
    }
    

    @Test
    public void testUpdateDatasetOk() throws Exception {
        Dataset mockDataset = new Dataset();
        mockDataset.setDatasetName("test-name");
        mockDataset.setExperiment(new Experiment()); 
        
        Experiment mockExperiment = getMockExperiment();
        mockDataset.setExperiment(mockExperiment);

        Analysis mockAnalysis = new Analysis();
        mockAnalysis.setAnalysisName("test-name");
        mockAnalysis.setAnalysisId(1);   
        when(
            analysisDao.getAnalysis(1)
        ).thenReturn(
            mockAnalysis
        );

        when (experimentDao.getExperiment(1)).thenReturn(mockExperiment);

        when(datasetDao.getDataset(123)).thenReturn(mockDataset);

        Cv mockNewStatus = new Cv();
        mockNewStatus.setTerm("new");
        mockNewStatus.setCvId(1);

        when( cvDao.getNewStatus()).thenReturn(mockNewStatus);
        Contact mockEditor = new Contact();
        mockEditor.setContactId(2);
        mockEditor.setUsername("test-user");
        when(contactDao.getContactByUsername("test-user")).thenReturn(
            mockEditor
        );

        when(analysisDao.getAnalysesByAnalysisIds(anySet())).thenReturn(this.getMockAnalysisList());


        DatasetRequestDTO request = new DatasetRequestDTO();
        request.setDatasetName("new-name");
        request.setExperimentId(1);
        request.setCallingAnalysisId(1);
        request.setAnalysisIds(new Integer[]{10, 11});

        Dataset mockDataset2 = new Dataset();
        mockDataset.setExperiment(mockExperiment);

        when(datasetDao.updateDataset(any(Dataset.class))).thenReturn(mockDataset2);
        
        ArgumentCaptor<Dataset> arg = ArgumentCaptor.forClass(Dataset.class);
        datasetServiceImpl.updateDataset(123, request, "test-user");

       
        verify(datasetDao).updateDataset(arg.capture());
    
        assertTrue(arg.getValue().getDatasetName().equals("new-name"));
        assertTrue(arg.getValue().getExperiment().getExperimentId() == 1);
        assertTrue(arg.getValue().getCallingAnalysis().getAnalysisId() == 1);
    }

    private Experiment getMockExperiment() {
        Experiment mockExperiment = new Experiment();
        mockExperiment.setExperimentId(1);
        mockExperiment.setExperimentName("test-experiment");
        
        Project mockProject = new Project();
        mockProject.setProjectId(1);
        mockProject.setProjectName("test-project");

        Contact mockContact = new Contact();
        mockContact.setContactId(1);
        mockContact.setFirstName("firstName");
        mockContact.setLastName("lastName");
         
        mockProject.setContact(mockContact);
        mockExperiment.setProject(mockProject);
        return mockExperiment;
    }

    private List<Analysis> getMockAnalysisList() {
        List<Analysis> mockAnalysisList = new ArrayList<>();
        Analysis analysis1 = new Analysis();
        analysis1.setAnalysisId(10);
        Analysis analysis2 = new Analysis();
        analysis2.setAnalysisId(11);
        mockAnalysisList.add(analysis1);
        mockAnalysisList.add(analysis2);

        return mockAnalysisList;

    }
}