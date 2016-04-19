// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-24
// ************************************************************************
package org.gobiiproject.gobidomain.services.impl;

import org.gobiiproject.gobidomain.services.PingService;
import org.gobiiproject.gobiidtomapping.DtoMapPing;
import org.gobiiproject.gobiimodel.dto.container.PingDTO;
import org.gobiiproject.gobiimodel.utils.LineUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Phil on 3/24/2016.
 */
public class PingServiceImpl implements PingService {

    @Autowired
    private DtoMapPing dtoMapPing;

    @Override
    public PingDTO getPings( PingDTO pingDTO ) {

        pingDTO = dtoMapPing.getPings(pingDTO);
        String newPingMessage = LineUtils.wrapLine("Service layer responded");
        pingDTO.getPingResponses().add(newPingMessage);

        return pingDTO;
    } // getMarkers()

} // MarkerServiceImpl
