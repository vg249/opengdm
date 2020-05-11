package org.gobiiproject.gobiisampletrackingdao;

import static junit.framework.TestCase.assertTrue;

import java.math.BigDecimal;
import java.util.List;

import org.gobiiproject.gobiimodel.entity.MarkerLinkageGroup;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * Tests are developer tests against local database with existing data.
 * TODO: Before classes need to be created for setup data for tests.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring/test-config.xml"})
public class MarkerLinkageGroupDaoTest {

    @Autowired
    private MarkerLinkageGroupDao markerLinkageGroupDao;

    @Test
    public void testGetMarkerLinkageGroupsByMapsetId() {

        Integer pageSize = 100;

        //Selected with developer knowledge on test database.
        //TODO: Write initialize script
        Integer testMapsetId = 2;

        List<MarkerLinkageGroup> markerLinkageGroups = markerLinkageGroupDao.getMarkerLinkageGroups(
                pageSize, 0,
                testMapsetId, null,
                null, null,
                null, null,
                null, null,
                null);


        assertTrue("Empty MarkerLinkageGroup list",markerLinkageGroups.size() > 0);
        assertTrue("MarkerLinkageGroups size not equal to the page size",
                markerLinkageGroups.size() <= pageSize);

        for(MarkerLinkageGroup markerLinkageGroup : markerLinkageGroups) {
            assertTrue("Mapset Filter not working",
                    markerLinkageGroup.getLinkageGroup().getMapset().getMapsetId() == testMapsetId);
        }
    }

    @Test
    public void testGetMarkerLinkageGroupsByMarkerId() {

        Integer pageSize = 100;

        //Selected with developer knowledge on test database.
        //TODO: Write initialize script

        Integer testMarkerId = 6;

        List<MarkerLinkageGroup> markerLinkageGroups = markerLinkageGroupDao.getMarkerLinkageGroups(
                pageSize, 0,
                null, null,
                null, null,
                testMarkerId, null,
                null, null, null);

        assertTrue("Empty MarkerLinkageGroup list",markerLinkageGroups.size() > 0);
        assertTrue("MarkerLinkageGroups size not equal to the page size",
                markerLinkageGroups.size() <= pageSize);

        for(MarkerLinkageGroup markerLinkageGroup : markerLinkageGroups) {
            assertTrue("Marker Id filter not working",
                    markerLinkageGroup.getMarker().getMarkerId() == testMarkerId);
        }

    }

    @Test
    public void testGetMarkerLinkageGroupsByMinAndMaxPosition() {

        Integer pageSize = 100;

        //Selected with developer knowledge on test database.
        //TODO: Write initialize script
        BigDecimal testMinPosition = new BigDecimal( 79);
        BigDecimal testMaxPosition = new BigDecimal( 100);

        List<MarkerLinkageGroup> markerLinkageGroups = markerLinkageGroupDao.getMarkerLinkageGroups(
                pageSize, 0,
                null, null,
                null, null,
                null, null,
                testMinPosition, testMaxPosition, null);

        assertTrue("Empty MarkerLinkageGroup list",markerLinkageGroups.size() > 0);
        assertTrue("MarkerLinkageGroups size not equal to the page size",
                markerLinkageGroups.size() <= pageSize);

        for(MarkerLinkageGroup markerLinkageGroup : markerLinkageGroups) {

            int minPositionComparision = testMinPosition.compareTo(markerLinkageGroup.getStart());
            int maxPositionComparision = testMaxPosition.compareTo(markerLinkageGroup.getStop());

            assertTrue("Min position filter not working",
                    minPositionComparision == -1 || minPositionComparision == 0);
            assertTrue("Max position filter not working",
                    maxPositionComparision == 1 || maxPositionComparision == 0);

        }


    }


}
