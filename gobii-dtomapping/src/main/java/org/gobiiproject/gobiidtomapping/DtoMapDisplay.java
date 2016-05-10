package org.gobiiproject.gobiidtomapping;

import org.gobiiproject.gobiimodel.dto.container.DisplayDTO;
import org.gobiiproject.gobiimodel.dto.container.NameIdListDTO;

/**
 * Created by Phil on 4/6/2016.
 */
public interface DtoMapDisplay {
    DisplayDTO getDisplayNames(DisplayDTO displayDTO);
    DisplayDTO createDisplay(DisplayDTO displayDTO) throws GobiiDtoMappingException;
    DisplayDTO updateDisplay(DisplayDTO displayDTO) throws GobiiDtoMappingException;
}
