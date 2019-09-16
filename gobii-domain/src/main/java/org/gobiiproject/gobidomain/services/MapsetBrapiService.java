package org.gobiiproject.gobidomain.services;

import org.gobiiproject.gobiimodel.dto.entity.noaudit.MapsetListBrapiDTO;

import java.util.List;

public interface MapsetBrapiService {

    List<MapsetListBrapiDTO> getMapSets(Integer pageNumber, Integer pageSize);
}
