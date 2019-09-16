package org.gobiiproject.gobidomain.services.impl;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobidomain.services.MapsetBrapiService;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.MapsetListBrapiDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class MapsetBrapiServiceImpl implements MapsetBrapiService {

    Logger LOGGER = LoggerFactory.getLogger(MapsetBrapiServiceImpl.class);


    @Override
    public List<MapsetListBrapiDTO> getMapSets(Integer pageNum, Integer pageSize) throws GobiiDomainException {

        List<MapsetListBrapiDTO> returnVal = new ArrayList<>();

        try {

        } catch (Exception e) {

            LOGGER.error("Gobii service error", e);
            throw new GobiiDomainException(e);

        }

        return returnVal;
    }

}
