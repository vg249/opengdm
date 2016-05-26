package org.gobiiproject.gobiidtomapping.impl;

import org.gobiiproject.gobiidao.resultset.access.RsPingDao;
import org.gobiiproject.gobiidtomapping.DtoMapPing;
import org.gobiiproject.gobiimodel.dto.container.PingDTO;
import org.gobiiproject.gobiimodel.utils.LineUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Phil on 3/29/2016.
 */
public class DtoMapPingImpl implements DtoMapPing {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapPingImpl.class);


    @Autowired
    RsPingDao rsPingDao;

    @Override
    public PingDTO getPings(PingDTO pingDTO) {

        PingDTO returnVal = pingDTO;

        try {

            List<String> pingResponses = new ArrayList<>();

            String newPingMessage = LineUtils.wrapLine("Mapping layer responded");
            pingResponses.add(newPingMessage);


            Map<String,String> dbInfo = rsPingDao.getPingResponses(pingDTO.getDbMetaData());
            for( Map.Entry<String,String> entry : dbInfo.entrySet() ) {

                String currentPingMessage = LineUtils.wrapLine(entry.getKey()
                        + ": "
                        + entry.getValue());

                pingResponses.add(currentPingMessage);
            }

            returnVal.setPingResponses(pingResponses);

        } catch (Exception e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Gobii Maping Error", e);
        }


        return returnVal;

    } // getPings
}
