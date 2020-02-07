package org.gobiiproject.gobidomain.services.impl.brapi;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobidomain.services.LinkageGroupBrapiService;
import org.gobiiproject.gobidomain.services.MapsetBrapiService;
import org.gobiiproject.gobiidtomapping.entity.auditable.DtoMapMapsetBrApi;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.MapsetBrapiDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;
import org.gobiiproject.gobiimodel.entity.Mapset;
import org.gobiiproject.gobiimodel.modelmapper.ModelMapper;
import org.gobiiproject.gobiisampletrackingdao.MapsetDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MapsetBrapiServiceImpl implements MapsetBrapiService {

    Logger LOGGER = LoggerFactory.getLogger(MapsetBrapiServiceImpl.class);

    @Autowired
    private DtoMapMapsetBrApi dtoMapMapsetBrApi;

    @Autowired
    private LinkageGroupBrapiService linkageGroupBrapiService;

    @Autowired
    private MapsetDao mapsetDao;


    /**
     * Gets the list of Genome Maps in the database by Page Number and Page Size
     *
     * @param pageNum  - Page Number to be fetched
     * @param pageSize - Page Size to be fetched
     * @return List of Brapi Specified Genome Maps DTO
     * @throws GobiiDomainException
     */
    @Override
    public PagedResult<MapsetBrapiDTO> getMapSets(Integer pageSize, Integer pageNum,
                                           Integer studyDbId) throws GobiiDomainException {

        PagedResult<MapsetBrapiDTO> pagedResult = new PagedResult<>();

        try {

            Objects.requireNonNull(pageSize);
            Objects.requireNonNull(pageNum);

            List<MapsetBrapiDTO> mapsetBrapiDTOS = new ArrayList<>();

            Integer rowOffset = pageNum*pageSize;

            List<Mapset> mapsets = mapsetDao.getMapsetsWithCounts(pageSize, rowOffset, studyDbId);

            for (Mapset mapset : mapsets) {

                MapsetBrapiDTO mapsetBrapiDTO = new MapsetBrapiDTO();

                ModelMapper.mapEntityToDto(mapset, mapsetBrapiDTO);

                mapsetBrapiDTOS.add(mapsetBrapiDTO);

            }

            pagedResult.setResult(mapsetBrapiDTOS);

            pagedResult.setCurrentPageNum(pageNum);

            pagedResult.setCurrentPageSize(pageSize);

            return pagedResult;

        } catch (GobiiException gE) {
            throw gE;
        } catch (Exception e) {
            LOGGER.error("Gobii service error", e);
            throw new GobiiDomainException(e);

        }

    }
}
