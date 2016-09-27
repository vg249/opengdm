package org.gobiiproject.gobidomain.services.impl;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobidomain.services.MapsetService;
import org.gobiiproject.gobiidtomapping.DtoMapMapset;
import org.gobiiproject.gobiimodel.headerlesscontainer.MapsetDTO;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Phil on 4/28/2016.
 */
public class MapsetServiceImpl implements MapsetService {

    Logger LOGGER = LoggerFactory.getLogger(MapsetServiceImpl.class);

    @Autowired
    DtoMapMapset dtoMapMapset;

    /*//@Override
    public MapsetDTO processMapset(MapsetDTO mapsetDTO) {

        MapsetDTO returnVal = mapsetDTO;

        try {

            switch (returnVal.getProcessType()) {
                case READ:
                    returnVal = dtoMapMapset.getMapsetDetails(returnVal);
                    break;

                case CREATE:
                    returnVal = dtoMapMapset.createMapset(mapsetDTO);
                    returnVal.setCreatedDate(new Date());
                    returnVal.setModifiedDate(new Date());
                    break;

                case UPDATE:
                    returnVal = dtoMapMapset.updateMapset(mapsetDTO);
                    returnVal.setCreatedDate(new Date());
                    returnVal.setModifiedDate(new Date());
                    break;

                default:
                    returnVal.getStatus().addStatusMessage(Status.StatusLevel.ERROR,
                            Status.ValidationStatusType.BAD_REQUEST,
                            "Unsupported proces mapset type " + returnVal.getProcessType().toString());

            } // switch()

        } catch (Exception e) {

            returnVal.getStatus().addException(e);
            LOGGER.error("Gobii service error", e);
        }

        return returnVal;
    }*/

    @Override
    public List<MapsetDTO> getAllMapsetNames() throws GobiiDomainException {

        List<MapsetDTO> returnVal;

        try {
            returnVal = dtoMapMapset.getAllMapsetNames();
            for(MapsetDTO currentMapsetDTO : returnVal ) {
                currentMapsetDTO.getAllowedProcessTypes().add(GobiiProcessType.READ);
                currentMapsetDTO.getAllowedProcessTypes().add(GobiiProcessType.UPDATE);
            }


            if (null == returnVal) {
                returnVal = new ArrayList<>();
            }

        } catch (Exception e) {

            LOGGER.error("Gobii service error", e);
            throw new GobiiDomainException(e);

        }

        return returnVal;
    }

}
