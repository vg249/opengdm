package org.gobiiproject.gobiisampletrackingdao;

import org.gobiiproject.gobiimodel.entity.Mapset;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import javax.transaction.Transactional;

import static junit.framework.TestCase.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring/test-config.xml"})
public class MapsetDaoTest {

    @Autowired
    private MapsetDao mapsetDao;

    @Test
    public void getMapsetsTest() {

        Integer testPageSize = 100;

        List<Mapset> mapsets = mapsetDao.getMapsetsWithCounts(testPageSize, 0,
                null, null);

        assertTrue("getMapsets page size condition fails",
                mapsets.size() > 0 && mapsets.size() <= testPageSize);

    }

    @Test
    public void getMapsetByIdTest() {

        //Integer testPageSize = 100;

        Mapset mapset = mapsetDao.getMapsetWithCountsById(2);

        assertTrue("Get Mapset By Id fails",mapset.getMapsetId() == 2);

    }

    @Test
    @Transactional
    public void getMapsetsSimpleTest() throws Exception {
        List<Mapset> mapsets = mapsetDao.getMapsets(1000, 0, null);
        assertTrue("getMapsets fails",mapsets != null);
    }
}
