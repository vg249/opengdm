package org.gobiiproject.gobiisampletrackingdao;

import org.gobiiproject.gobiimodel.entity.MarkerLinkageGroup;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static junit.framework.TestCase.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring/test-config.xml"})
public class MarkerLinkageGroupDaoTest {

    @Autowired
    private MarkerLinkageGroupDao markerLinkageGroupDao;

    @Test
    public void testGetMarkerLinkageGroupsByDatasetId() {

        Integer pageSize = 100;

        //Selected with developer knowledge on test database.
        //TODO: Write initilize script
        Integer testDatasetId = 5;

        List<MarkerLinkageGroup> markerLinkageGroups = markerLinkageGroupDao.getMarkerLinkageGroups(
                pageSize, 0,
                null, null,
                null, "1",
                null, null,
                null, null,
                testDatasetId);


        assertTrue("Empty MarkerLinkageGroup list",markerLinkageGroups.size() > 0);
        assertTrue("MarkerLinkageGroups size not equal to the page size",
                markerLinkageGroups.size() <= pageSize);

        for(MarkerLinkageGroup markerLinkageGroup : markerLinkageGroups) {
            assertTrue(markerLinkageGroup.getMarker().getDatasetMarkerIdx().has(testDatasetId.toString()));
        }

    }

    @Test
    public void testGetMarkerLinkageGroupsByMinPosition() {


    }


}
