package org.gobiiproject.gobidomain.services.gdmv3;

import org.gobiiproject.gobiimodel.dto.gdmv3.MarkerGroupDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;

public interface MarkerGroupService {

	MarkerGroupDTO createMarkerGroup(MarkerGroupDTO request, String creator) throws Exception;

	PagedResult<MarkerGroupDTO> getMarkerGroups(Integer page, Integer pageSize);
    
}