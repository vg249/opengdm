package org.gobiiproject.gobiidtomapping.impl;

import org.gobiiproject.gobiidao.entities.access.MarkerDao;
import org.gobiiproject.gobiidtomapping.DtoMapMarker;
import org.gobiiproject.gobiimodel.dto.container.MarkerGroupDTO;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * Created by Phil on 3/29/2016.
 */
public class DtoMapMarkerImpl implements DtoMapMarker {

    @Autowired
    private MarkerDao markerDao;

    @Override
    public MarkerGroupDTO getMarkers(List<Integer> markerIds) {

        MarkerGroupDTO returnVal = new MarkerGroupDTO();

        Map<String, List<String>> markerGroups = markerDao.getMarkers(markerIds);

        returnVal.setMarkerMap(markerGroups);

        return returnVal;

    } // getMarkers()

} // DtoMapMarkerImpl
