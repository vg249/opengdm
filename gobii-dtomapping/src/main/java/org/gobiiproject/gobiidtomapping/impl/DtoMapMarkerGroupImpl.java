package org.gobiiproject.gobiidtomapping.impl;

import ch.qos.logback.core.status.Status;
import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.resultset.access.RsMarkerGroupDao;
import org.gobiiproject.gobiidao.resultset.core.EntityPropertyParamNames;
import org.gobiiproject.gobiidao.resultset.core.ParamExtractor;
import org.gobiiproject.gobiidao.resultset.core.ResultColumnApplicator;
import org.gobiiproject.gobiidtomapping.DtoMapMarkerGroup;
import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.dto.container.MarkerGroupDTO;
import org.gobiiproject.gobiimodel.dto.container.MarkerGroupMarkerDTO;
import org.gobiiproject.gobiimodel.dto.header.DtoHeaderResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Phil on 4/22/2016.
 */
public class DtoMapMarkerGroupImpl implements DtoMapMarkerGroup {


    Logger LOGGER = LoggerFactory.getLogger(DtoMapDataSetImpl.class);


    @Autowired
    private RsMarkerGroupDao rsMarkerGroupDao;

    public MarkerGroupDTO getMarkerGroupDetails(MarkerGroupDTO markerGroupDTO) throws GobiiDtoMappingException {

        MarkerGroupDTO returnVal = markerGroupDTO;

        try {

            ResultSet resultSet = rsMarkerGroupDao.getMarkerGroupDetailByMarkerGroupId(markerGroupDTO.getMarkerGroupId());

            if (resultSet.next()) {
                ResultColumnApplicator.applyColumnValues(resultSet, returnVal);
            }

//            ResultSet propertyResultSet = rsMarkerGroupDao.getParameters(MarkerGroupDTO.getMarkerGroupId());
//            List<EntityPropertyDTO> entityPropertyDTOs =
//                    EntityProperties.resultSetToProperties(MarkerGroupDTO.getMarkerGroupId(),propertyResultSet);
//
//            MarkerGroupDTO.setParameters(entityPropertyDTOs);


        } catch (SQLException e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Error mapping result set to DTO", e);
        } catch (GobiiDaoException e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Error mapping result set to DTO", e);
        }

        return returnVal;

    }

//    private void upsertMarkerGroupProperties(Integer markerGroupId, List<EntityPropertyDTO> markerGroupProperties) throws GobiiDaoException {
//
//        for (EntityPropertyDTO currentProperty : markerGroupProperties) {
//
//            Map<String, Object> spParamsParameters =
//                    EntityProperties.propertiesToParams(markerGroupId, currentProperty);
//
//            rsMarkerGroupDao.createUpdateMarkerGroupMarker(spParamsParameters);
//
//            currentProperty.setEntityIdId(markerGroupId);
//        }
//
//    }

    @Override
    public MarkerGroupDTO createMarkerGroup(MarkerGroupDTO markerGroupDTO) throws GobiiDtoMappingException {

        MarkerGroupDTO returnVal = markerGroupDTO;

        try {

            Map<String, Object> parameters = ParamExtractor.makeParamVals(markerGroupDTO);
            Integer markerGroupId = rsMarkerGroupDao.createMarkerGroup(parameters);
            returnVal.setMarkerGroupId(markerGroupId);


            boolean allMarkerNamesExist = true;
            for (MarkerGroupMarkerDTO currentMarkerGroupMarkerDto : returnVal.getMarkers()) {
                String currentMarkerName = currentMarkerGroupMarkerDto.getMarkerName();
                ResultSet markersResultSet = rsMarkerGroupDao.getMarkersByMarkerName(currentMarkerName);
                boolean currentMarkerNameExists = false;
                while(markersResultSet.next()) {
                    currentMarkerNameExists = true;
                    ResultColumnApplicator.applyColumnValues(markersResultSet, currentMarkerGroupMarkerDto);
                }

                if(!currentMarkerNameExists) {
                    currentMarkerGroupMarkerDto.setMarkerExists(false);
                    allMarkerNamesExist = false;
                }
            }

            if( allMarkerNamesExist ) {

                for(MarkerGroupMarkerDTO currentMarkerGroupMarkerDTO : returnVal.getMarkers()) {

                    Map<String, Object> markerGroupMarkerParameters = new HashMap<>();

                    markerGroupMarkerParameters.put(EntityPropertyParamNames.PROPPCOLARAMNAME_ENTITY_ID, returnVal.getMarkerGroupId());
                    markerGroupMarkerParameters.put(EntityPropertyParamNames.PROPPCOLARAMNAME_PROP_ID, currentMarkerGroupMarkerDTO.getMarkerId());
                    markerGroupMarkerParameters.put(EntityPropertyParamNames.PROPPCOLARAMNAME_VALUE, currentMarkerGroupMarkerDTO.getFavorableAllele());

                    rsMarkerGroupDao.createUpdateMarkerGroupMarker(markerGroupMarkerParameters);

                }

            } else {
                returnVal.getDtoHeaderResponse().addStatusMessage(DtoHeaderResponse.StatusLevel.ERROR,
                        DtoHeaderResponse.ValidationStatusType.NONEXISTENT_FK_ENTITY,
                        "At least one marker name does not exist");
            }

        } catch (SQLException e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Error mapping result set to DTO", e);
        } catch (GobiiDaoException e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Error mapping result set to DTO", e);
        }

        return returnVal;
    }
//
//    @Override
//    public MarkerGroupDTO updateMarkerGroup(MarkerGroupDTO markerGroupDTO) throws GobiiDtoMappingException {
//
//        MarkerGroupDTO returnVal = markerGroupDTO;
//
//        try {
//
//            Map<String, Object> parameters = ParamExtractor.makeParamVals(returnVal);
//            rsMarkerGroupDao.updateMarkerGroup(parameters);
//
//            if( null != markerGroupDTO.getParameters() ) {
//                upsertMarkerGroupProperties(markerGroupDTO.getMarkerGroupId(),
//                        markerGroupDTO.getParameters());
//            }
//
//        } catch (GobiiDaoException e) {
//            returnVal.getDtoHeaderResponse().addException(e);
//            LOGGER.error(e.getMessage());
//        }
//
//        return returnVal;
//    }
}
