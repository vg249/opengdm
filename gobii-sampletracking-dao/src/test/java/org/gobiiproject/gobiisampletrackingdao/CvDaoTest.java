package org.gobiiproject.gobiisampletrackingdao;

import org.gobiiproject.gobiimodel.cvnames.CvGroup;
import org.gobiiproject.gobiimodel.entity.Cv;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static junit.framework.TestCase.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring/test-config.xml"})
public class CvDaoTest {

    @Autowired
    private CvDao cvDao;

    @Test
    public void getCvListByCvGroupTest() {

        List<Cv> cvList = cvDao.getCvListByCvGroup(
                CvGroup.CVGROUP_PROJECT_PROP.getCvGroupName());

        assertTrue(cvList.size() > 0);

    }

}
