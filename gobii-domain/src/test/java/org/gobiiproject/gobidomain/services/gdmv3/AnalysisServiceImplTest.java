package org.gobiiproject.gobidomain.services.gdmv3;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.gobiiproject.gobiimodel.cvnames.CvGroupTerm;
import org.gobiiproject.gobiimodel.dto.gdmv3.AnalysisDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.CvTypeDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;
import org.gobiiproject.gobiimodel.entity.Analysis;
import org.gobiiproject.gobiimodel.entity.Contact;
import org.gobiiproject.gobiimodel.entity.Cv;
import org.gobiiproject.gobiimodel.entity.CvGroup;
import org.gobiiproject.gobiisampletrackingdao.AnalysisDao;
import org.gobiiproject.gobiisampletrackingdao.ContactDao;
import org.gobiiproject.gobiisampletrackingdao.CvDao;
import org.gobiiproject.gobiisampletrackingdao.ReferenceDao;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.web.WebAppConfiguration;

import lombok.extern.slf4j.Slf4j;

@WebAppConfiguration
@Slf4j
public class AnalysisServiceImplTest {
    @Mock
    private AnalysisDao analysisDao;

    @Mock
    private CvDao cvDao;

    @Mock
    private ReferenceDao referenceDao;

    @Mock
    private ContactDao contactDao;

    @InjectMocks
    private AnalysisServiceImpl analysisServiceImpl;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAnalyses() throws Exception {
        assert analysisDao != null;
        List<Analysis> mockItems = new ArrayList<>();
        Integer offset = 0;
        Integer pageSize = 1000;
        when(analysisDao.getAnalyses(0, 1000)).thenReturn(mockItems);

        PagedResult<AnalysisDTO> results = analysisServiceImpl.getAnalyses(0, 1000);
        assert results != null;
        verify(analysisDao, times(1)).getAnalyses(offset, pageSize);

    }

    @Test
    public void testGetAnalysisById() throws Exception {
        Analysis mockAnalysis = new Analysis();
        when(analysisDao.getAnalysis(123)).thenReturn(mockAnalysis);

        AnalysisDTO sampleDTO = analysisServiceImpl.getAnalysis(123);
        assert sampleDTO != null;
        verify(analysisDao, times(1)).getAnalysis(123);
    }

    @Test
    public void testCreateAnalysisType() throws Exception {
        assert cvDao != null;

        when(
            cvDao.getCvGroupByNameAndType( CvGroupTerm.CVGROUP_ANALYSIS_TYPE.getCvGroupName(), 2)
        ).thenReturn(
            new org.gobiiproject.gobiimodel.entity.CvGroup()
        );
        
        when(
            cvDao.createCv(
                any(Cv.class)
            )
        ).thenReturn(new Cv());

        when(
            cvDao.getNewStatus()
        ).thenReturn(new Cv());

        CvTypeDTO analysisTypeRequest = new CvTypeDTO();
        analysisTypeRequest.setTypeName("test-name");
        analysisTypeRequest.setTypeDescription("test-description");
        CvTypeDTO analysisType = analysisServiceImpl.createAnalysisType(analysisTypeRequest, "test-user");
        assert analysisType != null;
        verify(
            cvDao, times(1)
        ).getCvGroupByNameAndType(CvGroupTerm.CVGROUP_ANALYSIS_TYPE.getCvGroupName(), 2);
        verify(
            cvDao, times(1)
        ).createCv(any(Cv.class));
        
    }

    @Test
    public void testCreateAnalysisOk()  throws Exception {
        AnalysisDTO request = new AnalysisDTO();
        request.setAnalysisTypeId(456);
        request.setAnalysisName("test-analysis");


        Cv analysisType = new Cv();
        analysisType.setCvId(456);
        analysisType.setTerm("test-analysis-type");
        
        CvGroup group = new CvGroup();
        group.setCvGroupId(321);
        group.setCvGroupName(CvGroupTerm.CVGROUP_ANALYSIS_TYPE.getCvGroupName());

        analysisType.setCvGroup(group);

        Contact mockContact = new Contact();
        mockContact.setContactId(788);
        mockContact.setUsername("test-editor");

        when(cvDao.getCvByCvId(456)).thenReturn(analysisType);
        when(contactDao.getContactByUsername("test-editor")).thenReturn(
            mockContact
        );

        when(analysisDao.createAnalysis(any(Analysis.class))).thenReturn(
            new Analysis()
        ); 

        ArgumentCaptor<Analysis> arg = ArgumentCaptor.forClass(Analysis.class);
        analysisServiceImpl.createAnalysis(request, "test-editor");

        verify(analysisDao).createAnalysis(arg.capture());
        log.info(arg.getValue().getAnalysisName());
        assertTrue(arg.getValue().getAnalysisName().equals("test-analysis"));
    }

    

  
}