/**
 * ExperimentServiceImplTest.java
 * 
 * @author Rodolfo N. Duldulao, Jr.
 */

package org.gobiiproject.gobidomain.services.gdmv3;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.gobiiproject.gobiimodel.dto.gdmv3.ExperimentDTO;
import org.gobiiproject.gobiimodel.dto.request.ExperimentRequest;
import org.gobiiproject.gobiimodel.entity.Experiment;
import org.gobiiproject.gobiimodel.entity.Platform;
import org.gobiiproject.gobiimodel.entity.Project;
import org.gobiiproject.gobiimodel.entity.Protocol;
import org.gobiiproject.gobiimodel.entity.VendorProtocol;
import org.gobiiproject.gobiisampletrackingdao.ExperimentDao;
import org.gobiiproject.gobiisampletrackingdao.ProjectDao;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.web.WebAppConfiguration;

@WebAppConfiguration
public class ExperimentServiceImplTest {

    @Mock
    private ExperimentDao experimentDao;

    @Mock
    private ProjectDao projectDao;

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

    @Test
    public void testCreateExperiment() throws Exception {
        assert experimentDao != null;

        ExperimentRequest request = new ExperimentRequest();
        request.setExperimentName("test-experiment");
        request.setProjectId(7);
        request.setVendorProtocolId(4);

        Project dummyProject = new Project();
        dummyProject.setProjectId(7);
        dummyProject.setProjectName("test-project");
        
        when(
            projectDao.getProject(7)
        ).thenReturn(
            dummyProject
        );

        VendorProtocol dummyVp = new VendorProtocol();
        dummyVp.setName("test-vendor-protocol");
        dummyVp.setVendorProtocolId(4);

        Platform platform = new Platform();
        platform.setPlatformId(1);
        platform.setPlatformName("test-platform");

        Protocol dummyProtocol = new Protocol();
        dummyProtocol.setProtocolId(1);
        dummyProtocol.setName("test-protocol");
        dummyProtocol.setPlatform(platform);

        dummyVp.setProtocol(dummyProtocol);

        when(
            experimentDao.getVendorProtocol(4)
        ).thenReturn(
            dummyVp
        );

        Experiment experiment = new Experiment();
        experiment.setExperimentName("test-experiment");
        experiment.setProject(dummyProject);
        experiment.setVendorProtocol(dummyVp);
        
        when(
            experimentDao.createExperiment(Mockito.any(Experiment.class))
        ).thenReturn(
            experiment
        );

        ExperimentDTO target = experimentServiceImpl.createExperiment(request);
        assert target.getExperimentName() == experiment.getExperimentName();
        verify(projectDao, times(1)).getProject(7);
        verify(experimentDao, times(1)).getVendorProtocol(4);
        verify(experimentDao, times(1)).createExperiment( Mockito.any(Experiment.class));
        
    }


}