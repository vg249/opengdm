package org.gobiiproject.gobidomain.services;

import org.gobiiproject.gobiimodel.dto.entity.noaudit.MapsetBrapiDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;

import java.util.List;

public interface MapsetBrapiService {

    PagedResult<MapsetBrapiDTO> getMapSets(Integer pageSize, Integer pageNum, Integer studyDbId);
    MapsetBrapiDTO getMapSetById(Integer mapDbId);

}
