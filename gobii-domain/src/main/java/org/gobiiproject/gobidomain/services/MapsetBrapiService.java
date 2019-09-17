package org.gobiiproject.gobidomain.services;

import org.gobiiproject.gobiimodel.dto.entity.noaudit.MapsetBrapiDTO;

import java.util.List;

public interface MapsetBrapiService {

    List<MapsetBrapiDTO> getMapSets(Integer pageNumber, Integer pageSize);
}
