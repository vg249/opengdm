// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-24
// ************************************************************************
package org.gobiiproject.gobiiprocess;

import org.gobiiproject.gobidomain.services.MarkerService;
import org.gobiiproject.gobiimodel.dto.container.MarkerGroupDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Phil on 3/24/2016.
 */
public class ExtractorProcess {

    public static void main(String[] args) {

        Logger LOGGER = LoggerFactory.getLogger(ExtractorProcess.class);
        SpringContextLoader springContextLoader = new SpringContextLoader();

        try {

            MarkerService markerService = springContextLoader.getApplicationContext().getBean(MarkerService.class);
            MarkerGroupDTO markerGroupDTO = markerService.getMarkers(null);
            LOGGER.info("Got markers: " + markerGroupDTO.getMarkerGroups());

        } catch (Exception exception) {
            LOGGER.error(exception.getMessage());
        }

    }//main

}
