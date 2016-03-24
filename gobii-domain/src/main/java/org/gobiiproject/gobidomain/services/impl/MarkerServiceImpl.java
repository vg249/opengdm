// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-24
// ************************************************************************
package org.gobiiproject.gobidomain.services.impl;

import org.gobiiproject.gobidomain.services.MarkerService;
import org.gobiiproject.gobiidao.MarkerDao;
import org.gobiiproject.gobiimodel.dto.container.MarkerGroupDTO;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Phil on 3/24/2016.
 */
public class MarkerServiceImpl implements MarkerService {

    @Autowired
    private MarkerDao markerDao = null;

    @Override
    public MarkerGroupDTO getMarkers(List<String> Chromosomes) {

        MarkerGroupDTO returnVal = new MarkerGroupDTO();

        Map<String, List<String>> markerMap = markerDao.getMarkers(null);
        returnVal.setMarkerMap(markerMap);
        return returnVal;

    } // getMarkers()

} // MarkerServiceImpl
