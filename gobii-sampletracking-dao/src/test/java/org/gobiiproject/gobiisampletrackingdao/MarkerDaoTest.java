package org.gobiiproject.gobiisampletrackingdao;

import static junit.framework.TestCase.assertTrue;

import java.util.*;

import org.gobiiproject.gobiimodel.entity.Marker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Test cases for MarkerDaoImpl
 * TODO: The dataset test are written with knowledge of undelying data in
 *   api.gobii.org. Need to refactor in future with Test databases and setup data
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring/test-config.xml"})
public class MarkerDaoTest {

    @PersistenceContext
    protected EntityManager em;

    @Autowired
    private MarkerDao markerDao;

    @Autowired
    private CvDao cvDao;

    Random random = new Random();

    private DaoTestSetUp daoTestSetUp;

    public void createTestData() {
        daoTestSetUp = new DaoTestSetUp(em, cvDao);
    }

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


        for(Marker marker : markers) {

            assertTrue(marker.getDatasetMarkerIdx().has(datasetId.toString()));

        }

        for(Marker marker : markers) {

            assertTrue(marker.getDatasetMarkerIdx().has(datasetId.toString()));

        }


    }

    @Test
    public void testGetMarkersByMarkerIdCursor() {

        Integer pageSize = 200;

        Integer rowOffset = 0;

        Integer datasetId  = 17;

        List<Marker> markers = markerDao.getMarkersByDatasetId(datasetId,
                pageSize, rowOffset);

        assertTrue("Empty marker list",markers.size() > 0);

        Integer markerIdCursor = markers.get(random.nextInt(markers.size())).getMarkerId();

        List<Marker> markersByMarkerIdCursor = markerDao.getMarkersByMarkerIdCursor(
                pageSize,
                markerIdCursor,
                null,
                datasetId);


        for(Marker marker : markersByMarkerIdCursor) {

            assertTrue(marker.getMarkerId() > markerIdCursor);

            assertTrue(marker.getDatasetMarkerIdx().has(datasetId.toString()));

        }


    }


    @Test
    public void testGetMarkersByMarkerIds() {


        // TODO: Hardcoded list from api.gobii.ord:gobii-dev marker table
        //  Need to replace this with a standard setupcalss functionality in future.
        Set<Integer> markerIds = new HashSet<>(Arrays.asList(
                6, 7, 8, 9, 10,
                11, 12, 13, 14,
                15
        ));

        List<Marker> markers = markerDao.getMarkersByMarkerIds(markerIds);

        assertTrue(markers.size() <= markerIds.size());

        for(Marker marker : markers) {
            assertTrue(markerIds.contains(marker.getMarkerId()));
        }

    }


    @Test
    public void testGetMarkersByMarkerNames() {


        Set<String> markerNames = new HashSet<>(Arrays.asList(
                "BS00062676", "kw004_Sbm1", "TaMoc-7A_2433",
                "Tsn1", "TaCKX-D1", "TaCwi-4A_1523",
                "TaCwi-A1a/b", "snp3BS-8", "TaGASR-A1", "TaGS-D1",
                "TaGW2-HAP-A/G", "TaSus2-2B_SNP", "TaTGW6-A1_1050",
                "snpOS0312", "Xsnp3BS-2"));

        List<Marker> markers = markerDao.getMarkersByMarkerNames(markerNames);

        assertTrue(markers.size() <= markerNames.size());

        for(Marker marker : markers) {
            assertTrue(markerNames.contains(marker.getMarkerName()));
        }

    }

    @Test
    public void testGetMarkersByMarkerNamesAndDatasetIds() {


        Set<String> markerNames = new HashSet<>(Arrays.asList(
                "BS00062676", "kw004_Sbm1", "TaMoc-7A_2433",
                "Tsn1", "TaCKX-D1", "TaCwi-4A_1523",
                "TaCwi-A1a/b", "snp3BS-8", "TaGASR-A1", "TaGS-D1",
                "TaGW2-HAP-A/G", "TaSus2-2B_SNP", "TaTGW6-A1_1050",
                "snpOS0312", "Xsnp3BS-2"));

        String[] datasetIdsList = {"2", "5"};
        Set<String> datasetIds = new HashSet<>(Arrays.asList(datasetIdsList));

        List<Marker> markers = markerDao.getMarkers(
                null, markerNames,
                datasetIds, null, null);

        assertTrue(markers.size() <= markerNames.size());

        for(Marker marker : markers) {
            assertTrue(markerNames.contains(marker.getMarkerName()));
            assertTrue(marker.getDatasetMarkerIdx().has(datasetIdsList[0]) ||
                    marker.getDatasetMarkerIdx().has(datasetIdsList[1]));
        }

    }

}
