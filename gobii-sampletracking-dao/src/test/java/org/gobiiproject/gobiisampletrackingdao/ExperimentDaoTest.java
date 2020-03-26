package org.gobiiproject.gobiisampletrackingdao;

import org.gobiiproject.gobiimodel.entity.Experiment;
import org.gobiiproject.gobiimodel.entity.Mapset;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static junit.framework.TestCase.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring/test-config.xml"})
public class ExperimentDaoTest {

    @Autowired
    private ExperimentDao experimentDao;

    @Test
    public void getExperimentsTest() {

        Integer testPageSize = 100;

        List<Experiment> experiments = experimentDao.getExperiments(
                testPageSize, 0, null);

        assertTrue("getExperiments page size condition fails",
                experiments.size() > 0
                        && experiments.size() <= testPageSize);

    }
}
