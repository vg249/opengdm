package org.gobiiproject.gobiidtomapping.entity.auditable;

import org.gobiiproject.gobiidtomapping.core.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.headerlesscontainer.DisplayDTO;

import java.util.List;

/**
 * Created by Phil on 4/6/2016.
 */
public interface DtoMapDisplay extends DtoMap<DisplayDTO> {

    DisplayDTO create(DisplayDTO displayDTO) throws GobiiDtoMappingException;
    DisplayDTO replace(Integer displayId, DisplayDTO displayDTO) throws GobiiDtoMappingException;
    DisplayDTO get(Integer displayId) throws GobiiDtoMappingException;
    List<DisplayDTO> getList() throws GobiiDtoMappingException;
}
