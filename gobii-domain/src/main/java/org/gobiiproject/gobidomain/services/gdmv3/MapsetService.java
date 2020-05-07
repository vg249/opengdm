package org.gobiiproject.gobidomain.services.gdmv3;

import org.gobiiproject.gobiimodel.dto.gdmv3.MapsetDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;

public interface MapsetService {
    PagedResult<MapsetDTO> getMapsets(Integer page, Integer pageSize, Integer mapsetTypeId) throws Exception;
}