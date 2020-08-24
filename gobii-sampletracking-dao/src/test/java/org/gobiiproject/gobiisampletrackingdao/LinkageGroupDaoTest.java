package org.gobiiproject.gobiisampletrackingdao;

import static junit.framework.TestCase.assertTrue;

import java.util.List;
import java.util.Random;

import org.gobiiproject.gobiimodel.entity.LinkageGroup;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring/test-config.xml"})
@Transactional
public class LinkageGroupDaoTest {

    @PersistenceContext
    protected EntityManager em;

    @Autowired
    private LinkageGroupDao linkageGroupDao;

    @Autowired
    private CvDao cvDao;

    Random random = new Random();

    final int testPageSize = 10;

    private DaoTestSetUp daoTestSetUp;

    @Before
    public void createTestData() {
        daoTestSetUp = new DaoTestSetUp(em, cvDao);
        daoTestSetUp.createTestLinkageGroups(testPageSize);
        em.flush();
    }

    @Test
    public void getLinkageGroupsTest() {

        List<LinkageGroup> linkageGroups =
            linkageGroupDao.getLinkageGroups(testPageSize, 0, null, null);

        assertTrue("getLinkageGroups page size condition fails",
                linkageGroups.size() > 0 &&
                    linkageGroups.size() <= testPageSize);

    }

    @Test
    public void getLinkageGroupByIdTest() {

        Integer testLinkageGroupId =
            daoTestSetUp
                .getCreatedLinkageGroups()
                .get(random.nextInt(
                    daoTestSetUp.getCreatedLinkageGroups().size()))
                .getLinkageGroupId();

        List<LinkageGroup> linkageGroups =
            linkageGroupDao.getLinkageGroups(
                testPageSize, 0,
                testLinkageGroupId, null);

        assertTrue("Get by linkageGroupId failed", linkageGroups.size() == 1);
        assertTrue("Get by linkageGroupId failed",
            linkageGroups.get(0).getLinkageGroupId() == testLinkageGroupId);

    }

}
