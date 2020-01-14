package org.gobiiproject.gobiisampletrackingdao;

import org.gobiiproject.gobiimodel.entity.Marker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static junit.framework.TestCase.assertTrue;

/**
 * Tests are constructed with only test cases required for development tests.
 * Tests will be made complete once other required DAO objects are added.
 *
 * TODO: The dataset test are written with knowledge of undelying data in
 *   api.gobii.org. Need to refactor in future with Test databases and setup data
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring/test-config.xml"})
public class MarkerDaoTest {

    @Autowired
    private MarkerDao markerDao;

    @Test
    public void testGetMarkers() {

        Integer pageSize = 200;

        Integer rowOffset = 0;

        List<Marker> markers = markerDao.getMarkers(
                pageSize, rowOffset,
                null, null);

        assertTrue("Empty marker list",markers.size() > 0);

        if(markers.size() > 0) {

            pageSize = markers.size() - 1;

            List<Marker> markersPaged = markerDao.getMarkers(
                    pageSize, rowOffset,
                    null, null);


            assertTrue("marker result list size not equal to the page size",
                    markersPaged.size() == pageSize);
        }
    }

    @Test
    public void testGetMarkersByDatasetId() {

        Integer pageSize = 200;

        Integer rowOffset = 0;

        Integer datasetId  = 17;


        List<Marker> markers = markerDao.getMarkersByDatasetId(datasetId,
                pageSize, rowOffset);

        assertTrue("Empty marker list",markers.size() > 0);

        if(markers.size() > 0) {

            for(Marker marker : markers) {

                assertTrue(marker.getDatasetMarkerIdx().has(datasetId.toString()));

            }

        }

        //Test Get MarkerStartAndStop by datasetId
         markers = markerDao.getMarkersWithStartAndStop(pageSize, rowOffset, null, datasetId);

        assertTrue("Empty marker list",markers.size() > 0);

        if(markers.size() > 0) {

            for(Marker marker : markers) {

                assertTrue(marker.getDatasetMarkerIdx().has(datasetId.toString()));

            }

        }


    }

    @Test
    public void testGetMarkerStartStopTuples() {

        Integer pageSize = 200;

        Integer rowOffset = 0;

        List<Marker> markers = markerDao.getMarkersWithStartAndStop(
                pageSize, rowOffset,
                null, null);

        assertTrue("Empty marker list",markers.size() > 0);

        if(markers.size() > 0) {

            pageSize = markers.size() - 1;

            List<Marker> markersPaged = markerDao.getMarkersWithStartAndStop(
                    pageSize, rowOffset,
                    null, null);

            boolean atleastOneStartStopIsMapped = false;

            for(Marker marker : markersPaged) {
                if(marker.getMarkerStart() != null || marker.getMarkerStop() != null) {
                    atleastOneStartStopIsMapped = true;
                }
            }

            assertTrue(atleastOneStartStopIsMapped);

            assertTrue("marker result list size not equal to the page size",
                    markersPaged.size() == pageSize);
        }

    }

}
