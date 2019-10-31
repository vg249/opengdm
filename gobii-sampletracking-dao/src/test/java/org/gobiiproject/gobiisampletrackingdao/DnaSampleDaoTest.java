package org.gobiiproject.gobiisampletrackingdao;

import org.gobiiproject.gobiimodel.entity.DnaSample;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static junit.framework.TestCase.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring/test-config.xml"})
public class DnaSampleDaoTest {

    @Autowired
    private DnaSampleDao dnaSampleDao;

    @Test
    public void getDnaSamples() {

        List<DnaSample> dnaSamples = dnaSampleDao.getDnaSamples(1, 10, null);

        assertTrue(dnaSamples.size() > 0);

    }



}
