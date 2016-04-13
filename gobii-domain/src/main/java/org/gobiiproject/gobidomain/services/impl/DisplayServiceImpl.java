package org.gobiiproject.gobidomain.services.impl;

import org.gobiiproject.gobidomain.services.DisplayService;
import org.gobiiproject.gobidomain.services.NameIdListService;
import org.gobiiproject.gobiidtomapping.DtoMapDisplay;
import org.gobiiproject.gobiidtomapping.DtoMapNameIdList;
import org.gobiiproject.gobiimodel.dto.container.DisplayDTO;
import org.gobiiproject.gobiimodel.dto.container.NameIdListDTO;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Phil on 4/6/2016.
 */
public class DisplayServiceImpl implements DisplayService {

    @Autowired
    DtoMapDisplay dtoMapDisplay = null;


    @Override
    public DisplayDTO getDisplayNames(DisplayDTO displayDTO) {
        return dtoMapDisplay.getDisplayNames(displayDTO);
    }
}
