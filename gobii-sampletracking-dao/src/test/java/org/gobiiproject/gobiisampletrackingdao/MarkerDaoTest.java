package org.gobiiproject.gobiisampletrackingdao;

import org.gobiiproject.gobiimodel.entity.Marker;
import org.gobiiproject.gobiimodel.entity.MarkerLinkageGroup;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static junit.framework.TestCase.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring/test-config.xml"})
public class MarkerDaoTest {

    @Autowired
    private MarkerDao markerDao;

    @Test
    public void testGetMarkers() {

        Integer pageSize = 100;

        List<Marker> markers = markerDao.getMarkers(
                0, pageSize,
                null, null);

        assertTrue("Empty marker list",markers.size() > 0);
        assertTrue("marker result list size not equal to the page size",
                markers.size() <= pageSize);

    }


}
