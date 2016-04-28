// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests;

import org.gobiiproject.gobiimodel.dto.container.MarkerGroupDTO;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class DtoRequestMarkersTest {

    @Test
    public void testGetMarkers() throws Exception {

        DtoRequestMarkers dtoRequestMarkers = new DtoRequestMarkers();
        MarkerGroupDTO markerGroupDTO = dtoRequestMarkers.getmMarkerGroup(new ArrayList<String>());

        Assert.assertNotEquals(null, markerGroupDTO);
        Assert.assertNotEquals(null, markerGroupDTO.getMarkerMap());
        Assert.assertTrue(markerGroupDTO.getMarkerMap().keySet().size() > 0);

    } // testGetMarkers()
}
