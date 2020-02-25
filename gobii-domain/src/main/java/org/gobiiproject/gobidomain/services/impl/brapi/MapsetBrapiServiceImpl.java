package org.gobiiproject.gobidomain.services.impl.brapi;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobidomain.services.MapsetBrapiService;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.MapsetBrapiDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class MapsetBrapiServiceImpl implements MapsetBrapiService {

    Logger LOGGER = LoggerFactory.getLogger(MapsetBrapiServiceImpl.class);



    /**
     * Gets the list of Genome Maps in the database by Page Number and Page Size
     * @param pageNum - Page Number to be fetched
     * @param pageSize - Page Size to be fetched
     * @return List of Brapi Specified Genome Maps DTO
     * @throws GobiiDomainException
     */
    @Override
    public List<MapsetBrapiDTO> getMapSets(
            Integer pageNum, Integer pageSize) throws GobiiDomainException {

        List<MapsetBrapiDTO> returnVal = new ArrayList<>();

        return returnVal;
    }

    /**
     * Gets the Genome Map by map Id. The linkage group list in the map is paged.
     * @param pageNum - Page Number to be fetched
     * @param pageSize - Page Size to be fetched
     * @return MapSetBrapiDTO
     * @throws GobiiDomainException
     */
    @Override
    public MapsetBrapiDTO getMapSet(
            Integer mapSetId,
            Integer pageNum,
            Integer pageSize) throws GobiiDomainException {

        MapsetBrapiDTO returnVal = new MapsetBrapiDTO();

        try {

            return returnVal;
        } catch (Exception e) {

            LOGGER.error("Gobii service error", e);
            throw new GobiiDomainException(e);

        }

    }

}
