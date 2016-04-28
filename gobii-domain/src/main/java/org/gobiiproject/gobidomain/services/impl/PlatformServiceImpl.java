package org.gobiiproject.gobidomain.services.impl;

import org.gobiiproject.gobidomain.services.PlatformService;
import org.gobiiproject.gobiidtomapping.DtoMapPlatform;
import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.dto.container.PlatformDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Phil on 4/27/2016.
 */
public class PlatformServiceImpl implements PlatformService {

    Logger LOGGER = LoggerFactory.getLogger(PlatformServiceImpl.class);

    @Autowired
    DtoMapPlatform dtoMapPlatform;

    @Override
    public PlatformDTO processPlatform(PlatformDTO platformDTO) {

        PlatformDTO returnVal = platformDTO;

        try {

            switch (returnVal.getProcessType()) {
                case READ:
                    returnVal = dtoMapPlatform.getPlatformDetails(returnVal);
                    break;

                default:
                    throw new GobiiDtoMappingException("Unsupported process type " + platformDTO.getProcessType().toString());

            } // switch()

        } catch (GobiiDtoMappingException e) {

            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error(e.getMessage());
        }


        return returnVal;
    }
}
