package org.gobiiproject.gobiisampletrackingdao;

import static junit.framework.TestCase.assertTrue;

import java.util.List;

import org.gobiiproject.gobiimodel.entity.LinkageGroup;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring/test-config.xml"})
public class LinkageGroupDaoTest {

    @Autowired
    private LinkageGroupDao linkageGroupDao;

    @Test
    public void getLinkageGroupsTest() {

        Integer testPageSize = 100;

        List<LinkageGroup> linkageGroups = linkageGroupDao.getLinkageGroups(testPageSize, 0,
                null, null);

        assertTrue("getLinkageGroups page size condition fails",
                linkageGroups.size() > 0 && linkageGroups.size() <= testPageSize);

    }

}
