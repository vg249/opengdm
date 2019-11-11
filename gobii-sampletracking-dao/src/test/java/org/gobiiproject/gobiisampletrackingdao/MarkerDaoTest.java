package org.gobiiproject.gobiisampletrackingdao;

import org.gobiiproject.gobiimodel.entity.Marker;
import org.gobiiproject.gobiimodel.entity.MarkerLinkageGroup;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.Tuple;
import java.util.List;

import static junit.framework.TestCase.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring/test-config.xml"})
public class MarkerDaoTest {

    @Autowired
    private MarkerDao markerDao;

    @Test
    public void testGetMarkers() {

        final Integer pageSize = 100;
        final Integer pageNum = 0;

        List<Marker> markers = markerDao.getMarkers(
                pageNum, pageSize,
                null, null);

        assertTrue("Empty marker list",markers.size() > 0);
        assertTrue("marker result list size not equal to the page size",
                markers.size() <= pageSize);

    }

    @Test
    public void testGetMarkerStartStops() {

        final Integer pageSize = 200;
        final Integer pageNum = 0;

        List<Tuple> markers = markerDao.getMarkerWithStartStopTuples(
                pageNum, pageSize,
                null, 24);

        for(Tuple markerTuple : markers) {

            Marker marker = (Marker) markerTuple.get("marker");
            MarkerLinkageGroup markerLi = (MarkerLinkageGroup) markerTuple.get("markerli");

        }



        assertTrue("Empty marker list",markers.size() > 0);
        assertTrue("marker result list size not equal to the page size",
                markers.size() <= pageSize);

    }

}
