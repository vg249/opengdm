package org.gobiiproject.gobidomain.services.impl;

import org.gobiiproject.gobidomain.services.MarkerGroupService;
import org.gobiiproject.gobiidtomapping.DtoMapMarkerGroup;
import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.dto.container.MarkerGroupDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * Created by Phil on 4/21/2016.
 */
public class MarkerGroupServiceImpl implements MarkerGroupService {

    Logger LOGGER = LoggerFactory.getLogger(MarkerGroupServiceImpl.class);

    @Autowired
    DtoMapMarkerGroup dtoMapMarkerGroup = null;

    public MarkerGroupDTO process(MarkerGroupDTO markerGroupDTO) {

        MarkerGroupDTO returnVal = new MarkerGroupDTO();

        try {

            switch (markerGroupDTO.getProcessType()) {

                case READ:
                    returnVal = dtoMapMarkerGroup.getMarkerGroupDetails(markerGroupDTO);
                    break;

                case CREATE:
                    returnVal.setCreateDate(new Date());
                    returnVal.setModifiedDate(new Date());
                    returnVal = dtoMapMarkerGroup.createMarkerGroup(markerGroupDTO);
                    break;
//
//                case UPDATE:
//                    returnVal = dtoMapMarkerGroup.updateMarkerGroup(MarkerGroupDTO);
//                    break;
            }

        } catch (GobiiDtoMappingException e) {

            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error(e.getMessage());
        }

        return returnVal;
    }
}
