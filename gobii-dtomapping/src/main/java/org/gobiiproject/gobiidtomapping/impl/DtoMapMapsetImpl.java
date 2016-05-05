package org.gobiiproject.gobiidtomapping.impl;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.resultset.access.RsMapSetDao;
import org.gobiiproject.gobiidao.resultset.core.ParamExtractor;
import org.gobiiproject.gobiidao.resultset.core.ResultColumnApplicator;
import org.gobiiproject.gobiidtomapping.DtoMapMapset;
import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiidtomapping.core.EntityProperties;
import org.gobiiproject.gobiimodel.dto.container.MapsetDTO;
import org.gobiiproject.gobiimodel.dto.container.EntityPropertyDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by Phil on 4/21/2016.
 */
public class DtoMapMapsetImpl implements DtoMapMapset {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapMapsetImpl.class);


    @Autowired
    private RsMapSetDao rsMapsetDao;

    @Override
    public MapsetDTO getMapsetDetails(MapsetDTO mapsetDTO) throws GobiiDtoMappingException {

        MapsetDTO returnVal = mapsetDTO;

        try {

            ResultSet resultSet = rsMapsetDao.getMapsetDetailsByMapsetId(mapsetDTO.getMapsetId());

            if (resultSet.next()) {

                // apply dataset values
                ResultColumnApplicator.applyColumnValues(resultSet, returnVal);

                ResultSet propertyResultSet = rsMapsetDao.getParameters(mapsetDTO.getMapsetId());
                List<EntityPropertyDTO> entityPropertyDTOs =
                        EntityProperties.resultSetToProperties(mapsetDTO.getMapsetId(),propertyResultSet);

                mapsetDTO.setParameters(entityPropertyDTOs);

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
            Integer mapsetId = rsMapsetDao.createMapset(parameters);
            returnVal.setMapsetId(mapsetId);

            List<EntityPropertyDTO> mapsetParameters = mapsetDTO.getParameters();
            upsertMapsetProperties(mapsetDTO.getMapsetId(), mapsetParameters);

        } catch (GobiiDaoException e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Error mapping result set to DTO", e);
        }

        return returnVal;
    }

    private void upsertMapsetProperties(Integer mapsetId, List<EntityPropertyDTO> mapsetProperties) throws GobiiDaoException {

        for (EntityPropertyDTO currentProperty : mapsetProperties) {

            Map<String, Object> spParamsParameters =
                    EntityProperties.propertiesToParams(mapsetId, currentProperty);

            rsMapsetDao.createUpdateParameter(spParamsParameters);

            currentProperty.setEntityIdId(mapsetId);
        }

    }
    
    @Override
    public MapsetDTO updateMapset(MapsetDTO mapsetDTO) throws GobiiDtoMappingException {

        MapsetDTO returnVal = mapsetDTO;

        try {

            Map<String, Object> parameters = ParamExtractor.makeParamVals(returnVal);
            rsMapsetDao.updateMapset(parameters);

            if( null != mapsetDTO.getParameters() ) {
                upsertMapsetProperties(mapsetDTO.getMapsetId(),
                        mapsetDTO.getParameters());
            }

        } catch (GobiiDaoException e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error(e.getMessage());
        }

        return returnVal;
    }
}
