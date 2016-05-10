package org.gobiiproject.gobidomain.services.impl;

import org.gobiiproject.gobidomain.services.DisplayService;
import org.gobiiproject.gobidomain.services.NameIdListService;
import org.gobiiproject.gobiidtomapping.DtoMapDisplay;
import org.gobiiproject.gobiidtomapping.DtoMapNameIdList;
import org.gobiiproject.gobiidtomapping.DtoMapDisplay;
import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.dto.container.DisplayDTO;
import org.gobiiproject.gobiimodel.dto.container.NameIdListDTO;
import org.gobiiproject.gobiimodel.dto.container.DisplayDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * Created by Phil on 4/6/2016.
 */
public class DisplayServiceImpl implements DisplayService {

    @Autowired
    DtoMapDisplay dtoMapDisplay = null;

    Logger LOGGER = LoggerFactory.getLogger(DisplayServiceImpl.class);

    @Override
    public DisplayDTO process(DisplayDTO displayDTO) {

        DisplayDTO returnVal = new DisplayDTO();

        try {
            switch (displayDTO.getProcessType()) {
                case READ:
                    returnVal = dtoMapDisplay.getDisplayNames(displayDTO);
                    break;

                case CREATE:
                    returnVal = dtoMapDisplay.createDisplay(displayDTO);
                    returnVal.setCreatedDate(new Date());
                    returnVal.setModifiedDate(new Date());
                    break;

                case UPDATE:
                    returnVal = dtoMapDisplay.updateDisplay(displayDTO);
                    returnVal.setCreatedDate(new Date());
                    returnVal.setModifiedDate(new Date());
                    break;

                default:
                    throw new GobiiDtoMappingException("Unsupported process Display type " + displayDTO.getProcessType().toString());

            }

        } catch (GobiiDtoMappingException e) {

            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error(e.getMessage());
        }

        return returnVal;
    }

}
