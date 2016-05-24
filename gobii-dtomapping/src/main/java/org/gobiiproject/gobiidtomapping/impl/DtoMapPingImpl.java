package org.gobiiproject.gobiidtomapping.impl;

import org.gobiiproject.gobiidao.entity.access.PingEntityDao;
import org.gobiiproject.gobiidtomapping.DtoMapPing;
import org.gobiiproject.gobiimodel.dto.container.PingDTO;
import org.gobiiproject.gobiimodel.utils.LineUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by Phil on 3/29/2016.
 */
public class DtoMapPingImpl implements DtoMapPing {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapPingImpl.class);


    @Autowired
    PingEntityDao pingEntityDao = null;

    @Override
    public PingDTO getPings(PingDTO pingDTO) {

        PingDTO returnVal = pingDTO;

        try {

            List<String> pingResponses = pingEntityDao.getPingResponses(pingDTO.getPingRequests());
            String newPingMessage = LineUtils.wrapLine("Mapping layer responded");
            pingResponses.add(newPingMessage);
            returnVal.setPingResponses(pingResponses);

        } catch (Exception e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Gobii Maping Error", e);
        }


        return returnVal;

    } // getPings
}
