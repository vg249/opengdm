package org.gobiiproject.gobidomain.services.brapi;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.noaudit.MapsetBrapiDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;
import org.gobiiproject.gobiimodel.entity.Mapset;
import org.gobiiproject.gobiimodel.modelmapper.ModelMapper;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
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

            List<Mapset> mapsets = mapsetDao.getMapsetsWithCounts(pageSize, rowOffset, null, studyDbId);

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

    @Override
    public MapsetBrapiDTO getMapSetById(Integer mapDbId) throws GobiiException {

        MapsetBrapiDTO mapsetBrapiDTO = new MapsetBrapiDTO();

        try {

            Mapset mapset = mapsetDao.getMapsetWithCountsById(mapDbId);

            ModelMapper.mapEntityToDto(mapset, mapsetBrapiDTO);

            return mapsetBrapiDTO;
        }
        catch(Exception e) {
            throw new GobiiException(GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNKNOWN,
                    "Internal server error");
        }
    }
}
