package org.gobiiproject.gobidomain.services.brapi;

import org.gobiiproject.gobiimodel.dto.noaudit.MapsetBrapiDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;

public interface MapsetBrapiService {

    PagedResult<MapsetBrapiDTO> getMapSets(Integer pageSize, Integer pageNum, Integer studyDbId);
    MapsetBrapiDTO getMapSetById(Integer mapDbId);

}
