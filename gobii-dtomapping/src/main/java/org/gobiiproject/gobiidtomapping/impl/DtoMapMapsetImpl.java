package org.gobiiproject.gobiidtomapping.impl;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.resultset.access.RsMapSetDao;
import org.gobiiproject.gobiidao.resultset.core.ParamExtractor;
import org.gobiiproject.gobiidao.resultset.core.ResultColumnApplicator;
import org.gobiiproject.gobiidtomapping.DtoMapMapset;
import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.dto.DtoMetaData;
import org.gobiiproject.gobiimodel.dto.container.AnalysisDTO;
import org.gobiiproject.gobiimodel.dto.container.MapsetDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by Phil on 4/21/2016.
 */
public class DtoMapMapsetImpl implements DtoMapMapset {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapMapsetImpl.class);


    @Autowired
    private RsMapSetDao rsMapSetDao;

    @Override
    public MapsetDTO getMapsetDetails(MapsetDTO mapsetDTO) throws GobiiDtoMappingException {

        MapsetDTO returnVal = mapsetDTO;

        try {

            ResultSet resultSet = rsMapSetDao.getMapsetDetailsByMapsetId(mapsetDTO.getMapsetId());

            if (resultSet.next()) {

                // apply dataset values
                ResultColumnApplicator.applyColumnValues(resultSet, returnVal);


            } // if result set has a row

        } catch (GobiiDaoException e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Error mapping result set to DTO", e);
        } catch (SQLException e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Error mapping result set to DTO", e);
        }


        return returnVal;
    }

    @Override
    public MapsetDTO createMapset(MapsetDTO mapsetDTO) throws GobiiDtoMappingException {
        MapsetDTO returnVal = mapsetDTO;

        try {

            Map<String, Object> parameters = ParamExtractor.makeParamVals(mapsetDTO);
            Integer mapsetId = rsMapSetDao.createMapset(parameters);
            returnVal.setMapsetId(mapsetId);

        } catch (GobiiDaoException e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Error mapping result set to DTO", e);
        }

        return returnVal;
    }
}
