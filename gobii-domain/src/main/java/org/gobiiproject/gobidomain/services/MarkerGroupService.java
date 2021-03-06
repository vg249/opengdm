package org.gobiiproject.gobidomain.services;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.dto.entity.auditable.MarkerGroupDTO;

import java.util.List;

/**
 * Created by Phil on 4/21/2016.
 */
public interface MarkerGroupService {

    List<MarkerGroupDTO> getMarkerGroups() throws GobiiDomainException;
    MarkerGroupDTO createMarkerGroup(MarkerGroupDTO markerGroupDTO) throws GobiiDomainException;
    MarkerGroupDTO replaceMarkerGroup(Integer markerGroupId, MarkerGroupDTO markerGroupDTO) throws GobiiDomainException;
    MarkerGroupDTO getMarkerGroupById(Integer markerGroupId) throws GobiiDomainException;

}
