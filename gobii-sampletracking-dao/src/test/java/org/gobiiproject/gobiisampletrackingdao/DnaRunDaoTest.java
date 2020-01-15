package org.gobiiproject.gobiisampletrackingdao;

import org.gobiiproject.gobiimodel.entity.DnaRun;
import org.gobiiproject.gobiimodel.entity.Marker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.assertTrue;

/**
 * Test cases for DnaRunDaoImpl
 * TODO: The dataset test are written with knowledge of undelying data in
 *   api.gobii.org. Need to refactor in future with Test databases and setup data
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring/test-config.xml"})
public class DnaRunDaoTest {

    @Autowired
    private DnaRunDao dnaRunDao;

    @Test
    public void testGetDnaRuns() {

        Integer pageSize = 200;

        Integer rowOffset = 0;

        List<DnaRun> dnaruns = dnaRunDao.getDnaRuns(
                pageSize, rowOffset,
                null, null);

        assertTrue("Empty dnaRun list: ",dnaruns.size() > 0);

        if(dnaruns.size() > 0) {

            pageSize = dnaruns.size() - 1;

            List<DnaRun> dnaRunsPaged = dnaRunDao.getDnaRuns(
                    pageSize, rowOffset,
                    null, null);


            assertTrue("dnarun result list size not equal to the page size",
                    dnaRunsPaged.size() == pageSize);
        }
    }


}
