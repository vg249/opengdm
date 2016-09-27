package org.gobiiproject.gobiidtomapping;

import org.gobiiproject.gobiimodel.headerlesscontainer.MapsetDTO;

import java.util.List;

/**
 * Created by Phil on 4/28/2016.
 */
public interface DtoMapMapset {

    List<MapsetDTO> getAllMapsetNames() throws GobiiDtoMappingException;
    MapsetDTO getMapsetDetails(MapsetDTO mapsetDTO) throws GobiiDtoMappingException;
    MapsetDTO createMapset(MapsetDTO mapsetDTO) throws GobiiDtoMappingException;
    MapsetDTO updateMapset(MapsetDTO mapsetDTO) throws GobiiDtoMappingException;
}
