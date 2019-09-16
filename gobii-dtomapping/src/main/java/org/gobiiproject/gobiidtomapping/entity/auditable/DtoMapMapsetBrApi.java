package org.gobiiproject.gobiidtomapping.entity.auditable;

import org.gobiiproject.gobiimodel.dto.entity.noaudit.MapsetListBrapiDTO;

import java.util.List;

public interface DtoMapMapsetBrApi {

    List<MapsetListBrapiDTO> listMapset(Integer pageNum, Integer pageSize);

}
