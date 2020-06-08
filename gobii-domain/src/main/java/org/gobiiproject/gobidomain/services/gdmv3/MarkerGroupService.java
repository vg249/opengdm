package org.gobiiproject.gobidomain.services.gdmv3;

import org.gobiiproject.gobiimodel.dto.gdmv3.MarkerGroupDTO;

public interface MarkerGroupService {

	MarkerGroupDTO createMarkerGroup(MarkerGroupDTO request, String creator) throws Exception;
    
}