package org.gobiiproject.gobiisampletrackingdao;

import static junit.framework.TestCase.assertTrue;

import java.util.List;

import org.gobiiproject.gobiimodel.entity.gdmv3.ExperimentV3;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring/test-config.xml"})
public class ExperimentDaoTest {

    @Autowired
    private ExperimentDao experimentDao;

    @Test
    public void getExperimentsTest() {

        Integer testPageSize = 100;

        List<ExperimentV3> experiments = experimentDao.getExperiments(
                testPageSize, 0, null);

        assertTrue("getExperiments page size condition fails",
                experiments.size() > 0
                        && experiments.size() <= testPageSize);

    }
}
