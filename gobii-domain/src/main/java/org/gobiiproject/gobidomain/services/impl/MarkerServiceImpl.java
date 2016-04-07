// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-24
// ************************************************************************
package org.gobiiproject.gobidomain.services.impl;

import org.gobiiproject.gobidomain.services.MarkerService;
import org.gobiiproject.gobiidtomapping.DtoMapMarker;
import org.gobiiproject.gobiimodel.dto.container.MarkerGroupDTO;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Phil on 3/24/2016.
 */
public class MarkerServiceImpl implements MarkerService {

    @Autowired
    private DtoMapMarker dtoMapMarker;

    @Override
    public MarkerGroupDTO getMarkers(List<String> markerIds ) {

        List<Integer> newMarkerIds = new ArrayList<>();
        newMarkerIds.add(1);
        newMarkerIds.add(2);
        newMarkerIds.add(3);
        return dtoMapMarker.getMarkers(newMarkerIds);

    } // getMarkers()

} // MarkerServiceImpl
