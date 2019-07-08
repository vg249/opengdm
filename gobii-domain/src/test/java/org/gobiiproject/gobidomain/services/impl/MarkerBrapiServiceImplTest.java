package org.gobiiproject.gobidomain.services.impl;

import junit.framework.TestCase;
import org.gobiiproject.gobidomain.security.UserContextLoader;
import org.gobiiproject.gobidomain.services.MarkerBrapiService;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.MarkerBrapiDTO;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.internal.AssumptionViolatedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertFalse;

/**
 * Created by VCalaminos on 7/8/2019.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring/test-config.xml"})
public class MarkerBrapiServiceImplTest {

    @Autowired
    private MarkerBrapiService markerBrapiService = null;

    @BeforeClass
    public static void setUpClass() throws Exception {
        UserContextLoader userContextLoader = new UserContextLoader("classpath:/spring/test-config.xml");
        userContextLoader.loadUser("USER_READER");
    }

    @Test
    public void getMarkerList() {

        List<MarkerBrapiDTO> markerBrapiDTOList;

        markerBrapiDTOList = markerBrapiService.getMarkers(null, null, null);

        if (markerBrapiDTOList.size() <= 0) {
            throw new AssumptionViolatedException("There are no available markers in the database");
        }

        assertTrue(markerBrapiDTOList.size() > 0);
        assertNotNull(markerBrapiDTOList.get(0).getVariantDbId());

    }

    @Test
    public void getMarkerListWithPageSize() {

        List<MarkerBrapiDTO> markerBrapiDTOList;

        Integer pageSize = 10;

        markerBrapiDTOList = markerBrapiService.getMarkers(null, pageSize, null);

        if (markerBrapiDTOList.size() <= 0) {
            throw new AssumptionViolatedException("There are no available markers in the database");
        }

        assertTrue(markerBrapiDTOList.size() == pageSize);
        assertNotNull(markerBrapiDTOList.get(0).getVariantDbId());
    }

    @Test
    public void getMarkerListWithFilterByVariantDbSetId() {

        List<MarkerBrapiDTO> markerBrapiDTOList;
        markerBrapiDTOList = markerBrapiService.getMarkers(null, null, null);

        if (markerBrapiDTOList.size() <= 0) {
            throw new AssumptionViolatedException("There are no available markers in the database");
        }

        // check for markers that has existing dataset IDS
        Integer variantSetDbId = null;

        for (MarkerBrapiDTO currentMarkerBrapiDTO: markerBrapiDTOList) {

            if (currentMarkerBrapiDTO.getVariantSetDbId().size() > 0) {
                variantSetDbId = currentMarkerBrapiDTO.getVariantSetDbId().get(0);
                break;
            }
        }

        if (variantSetDbId == null) {
            throw new AssumptionViolatedException("There are no available markers with matching datasetIds in the database");
        }

        MarkerBrapiDTO markerBrapiDTOFilter = new MarkerBrapiDTO();
        markerBrapiDTOFilter.getVariantSetDbId().add(variantSetDbId);

        List<MarkerBrapiDTO> filteredMarkerDTOList = markerBrapiService.getMarkers(null, null, markerBrapiDTOFilter);

        assertTrue(filteredMarkerDTOList.size() > 0);
        assertNotNull(filteredMarkerDTOList.get(0).getVariantSetDbId());
        assertTrue(filteredMarkerDTOList.get(0).getVariantSetDbId().contains(variantSetDbId));
    }

    @Test
    public void getMarkerListWithFilterByVariantDbId() {

        List<MarkerBrapiDTO> markerBrapiDTOList;
        markerBrapiDTOList = markerBrapiService.getMarkers(null, null, null);

        if (markerBrapiDTOList.size() <= 0) {
            throw new AssumptionViolatedException("There are no available markers in the database");
        }

        assertNotNull(markerBrapiDTOList.get(0).getVariantDbId());

        Integer variantDbId = markerBrapiDTOList.get(0).getVariantDbId();

        MarkerBrapiDTO markerBrapiDTOFilter = new MarkerBrapiDTO();
        markerBrapiDTOFilter.setVariantDbId(variantDbId);

        List<MarkerBrapiDTO> filteredMarkerDTOList = markerBrapiService.getMarkers(null, null, markerBrapiDTOFilter);

        assertTrue(filteredMarkerDTOList.size() > 0);
        assertNotNull(filteredMarkerDTOList.get(0).getVariantDbId());
        assertTrue(filteredMarkerDTOList.get(0).getVariantDbId().equals(variantDbId));
    }

    @Test
    public void getMarkersByMarkerDbId() {

        MarkerBrapiDTO markerBrapiDTO = null;

        List<MarkerBrapiDTO> markerBrapiDTOList = markerBrapiService.getMarkers(null, null, null);

        if (markerBrapiDTOList.size() <= 0) {
            throw new AssumptionViolatedException("There are no available markers in the database");
        }

        Integer markerDbId = markerBrapiDTOList.get(0).getVariantDbId();

        markerBrapiDTO = markerBrapiService.getMarkerById(markerDbId);

        assertTrue(markerBrapiDTO.getVariantDbId() > 0);
        assertTrue(markerBrapiDTO.getVariantDbId().equals(markerDbId));
        assertNotNull(markerBrapiDTO.getVariantName());

    }

    @Test
    public void getDnaRunWithNonExistingId() {

        MarkerBrapiDTO markerBrapiDTO = new MarkerBrapiDTO();

        try {

            // non-existing ID
            Integer markerDbId = 0;

            markerBrapiDTO = markerBrapiService.getMarkerById(markerDbId);

        } catch (GobiiException gE) {

            TestCase.assertTrue(gE.getGobiiValidationStatusType() == GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST);
            assertFalse(markerBrapiDTO.getVariantDbId() > 0);
        }
    }
}
