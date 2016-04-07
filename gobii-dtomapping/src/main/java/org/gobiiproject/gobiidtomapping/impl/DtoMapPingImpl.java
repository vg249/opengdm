package org.gobiiproject.gobiidtomapping.impl;

import org.gobiiproject.gobiidao.entities.access.PingDao;
import org.gobiiproject.gobiidtomapping.DtoMapPing;
import org.gobiiproject.gobiimodel.dto.container.PingDTO;
import org.gobiiproject.gobiimodel.logutils.LineUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by Phil on 3/29/2016.
 */
public class DtoMapPingImpl implements DtoMapPing {

    @Autowired
    PingDao pingDao = null;

    @Override
    public PingDTO getPings(PingDTO pingDTO) {


        List<String> pingResponses = pingDao.getPingResponses(pingDTO.getPingRequests());
        String newPingMessage = LineUtils.wrapLine("Mapping layer responded");
        pingResponses.add(newPingMessage);
        pingDTO.setPingResponses(pingResponses);

        return pingDTO;

    } // getPings
}
