/**
 * ExperimentServiceImplTest.java
 * 
 * @author Rodolfo N. Duldulao, Jr.
 */

package org.gobiiproject.gobidomain.services.gdmv3;

import static org.mockito.Mockito.when;

import org.gobiiproject.gobiimodel.dto.gdmv3.ExperimentDTO;
import org.gobiiproject.gobiimodel.entity.Experiment;
import org.gobiiproject.gobiisampletrackingdao.ExperimentDao;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.web.WebAppConfiguration;

@WebAppConfiguration
public class ExperimentServiceImplTest {

    @Mock
    private ExperimentDao experimentDao;

    @InjectMocks
    private ExperimentServiceImpl experimentServiceImpl;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }
    @Test
    public void testGetExperiment() throws Exception {
        assert experimentDao != null;
        Experiment experiment = new Experiment();
        experiment.setExperimentName("test-experiment");

        
        when(
            experimentDao.getExperiment(123)
        ).thenReturn(
            experiment
        );

        ExperimentDTO target = experimentServiceImpl.getExperiment(123);
        assert target.getExperimentName() == experiment.getExperimentName();
        
    }


}