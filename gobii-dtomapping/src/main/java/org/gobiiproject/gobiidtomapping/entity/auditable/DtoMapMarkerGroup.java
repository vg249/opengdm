package org.gobiiproject.gobiidtomapping.entity.auditable;

import org.gobiiproject.gobiidtomapping.core.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.headerlesscontainer.MarkerGroupDTO;

import java.util.List;

/**
 * Created by Phil on 4/21/2016.
 */
public interface DtoMapMarkerGroup extends DtoMap<MarkerGroupDTO> {

    MarkerGroupDTO create(MarkerGroupDTO markerGroupDTO) throws GobiiDtoMappingException;
    MarkerGroupDTO replace(Integer markerGroupId, MarkerGroupDTO markerGroupDTO) throws GobiiDtoMappingException;
    MarkerGroupDTO get(Integer markerGroupId) throws GobiiDtoMappingException;
    List<MarkerGroupDTO> getList() throws GobiiDtoMappingException;

}
