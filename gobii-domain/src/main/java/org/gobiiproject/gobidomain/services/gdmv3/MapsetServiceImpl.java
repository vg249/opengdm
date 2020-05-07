package org.gobiiproject.gobidomain.services.gdmv3;

import java.util.ArrayList;
import java.util.List;

import org.gobiiproject.gobiimodel.dto.gdmv3.MapsetDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;
import org.gobiiproject.gobiimodel.entity.Mapset;
import org.gobiiproject.gobiimodel.modelmapper.ModelMapper;
import org.gobiiproject.gobiisampletrackingdao.MapsetDao;
import org.springframework.beans.factory.annotation.Autowired;

public class MapsetServiceImpl implements MapsetService {

    @Autowired
    private MapsetDao mapsetDao;

    @Override
    public PagedResult<MapsetDTO> getMapsets(Integer page, Integer pageSize, Integer mapsetTypeId) throws Exception {
        Integer offset = page * pageSize;
        List<Mapset> mapsets = mapsetDao.getMapsets(pageSize, offset, mapsetTypeId);
        List<MapsetDTO> mapsetDTOs = new ArrayList<>();
        mapsets.forEach(mapset -> {
            MapsetDTO mapsetDTO = new MapsetDTO();
            ModelMapper.mapEntityToDto(mapset, mapsetDTO);
            mapsetDTOs.add(mapsetDTO);
        });

        PagedResult<MapsetDTO> result = new PagedResult<>();
        result.setCurrentPageNum(page);
        result.setCurrentPageSize(mapsets.size());
        result.setResult(mapsetDTOs);
        
        return result;
    }
    
    
}