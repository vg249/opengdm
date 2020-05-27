package org.gobiiproject.gobidomain.services.gdmv3;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.gobiiproject.gobiimodel.cvnames.CvGroup;
import org.gobiiproject.gobiimodel.dto.gdmv3.AnalysisDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.AnalysisTypeDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.CvTypeDTO;
import org.gobiiproject.gobiimodel.dto.request.AnalysisTypeRequest;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;
import org.gobiiproject.gobiimodel.entity.Analysis;
import org.gobiiproject.gobiimodel.entity.Cv;
import org.gobiiproject.gobiisampletrackingdao.AnalysisDao;
import org.gobiiproject.gobiisampletrackingdao.ContactDao;
import org.gobiiproject.gobiisampletrackingdao.CvDao;
import org.gobiiproject.gobiisampletrackingdao.ReferenceDao;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.web.WebAppConfiguration;

@WebAppConfiguration
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
            cvDao.getCvGroupByNameAndType( CvGroup.CVGROUP_ANALYSIS_TYPE.getCvGroupName(), 2)
        ).thenReturn(
            new org.gobiiproject.gobiimodel.entity.CvGroup()
        );
        
        when(
            cvDao.createCv(
                any(Cv.class)
            )
        ).thenReturn(new Cv());

        CvTypeDTO analysisTypeRequest = new CvTypeDTO();
        analysisTypeRequest.setTypeName("test-name");
        analysisTypeRequest.setTypeDescription("test-description");
        CvTypeDTO analysisType = analysisServiceImpl.createAnalysisType(analysisTypeRequest, "test-user");
        assert analysisType != null;
        verify(
            cvDao, times(1)
        ).getCvGroupByNameAndType(CvGroup.CVGROUP_ANALYSIS_TYPE.getCvGroupName(), 2);
        verify(
            cvDao, times(1)
        ).createCv(any(Cv.class));
        
    }

  
}