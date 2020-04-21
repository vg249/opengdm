package org.gobiiproject.gobidomain.services.gdmv3;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.gobiiproject.gobiimodel.dto.gdmv3.AnalysisDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;
import org.gobiiproject.gobiimodel.entity.Analysis;
import org.gobiiproject.gobiisampletrackingdao.AnalysisDao;
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
        when(
            analysisDao.getAnalyses( 0, 1000) 
        ).thenReturn(
            mockItems
        );


        PagedResult<AnalysisDTO> results = analysisServiceImpl.getAnalyses(0, 1000);
        assert results != null;
        verify(analysisDao, times(1)).getAnalyses( offset, pageSize);

    }

}