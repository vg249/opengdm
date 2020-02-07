package org.gobiiproject.gobidomain.services.impl.brapi;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobidomain.services.LinkageGroupBrapiService;
import org.gobiiproject.gobidomain.services.MapsetBrapiService;
import org.gobiiproject.gobiidtomapping.entity.auditable.DtoMapMapsetBrApi;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.MapsetBrapiDTO;
import org.gobiiproject.gobiimodel.entity.Mapset;
import org.gobiiproject.gobiimodel.modelmapper.ModelMapper;
import org.gobiiproject.gobiisampletrackingdao.MapsetDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

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
    public List<MapsetBrapiDTO> getMapSets(Integer pageSize, Integer pageNum,
                                           Integer studyDbId) throws GobiiDomainException {

        List<MapsetBrapiDTO> returnVal = new ArrayList<>();

        try {

            List<Mapset> mapsets = mapsetDao.getMapsetsWithCountsByExperimentId(pageSize, pageNum, studyDbId);

            for (Mapset mapset : mapsets) {


                MapsetBrapiDTO mapsetBrapiDTO = new MapsetBrapiDTO();

                ModelMapper.mapEntityToDto(mapset, mapsetBrapiDTO);

                returnVal.add(mapsetBrapiDTO);

            }
        } catch (GobiiException gE) {
            throw gE;
        } catch (Exception e) {
            LOGGER.error("Gobii service error", e);
            throw new GobiiDomainException(e);

        }

        return returnVal;

    }
}
