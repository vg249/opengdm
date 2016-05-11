package org.gobiiproject.gobiidtomapping.impl;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.resultset.access.RsMarkerGroupDao;
import org.gobiiproject.gobiidao.resultset.core.EntityPropertyParamNames;
import org.gobiiproject.gobiidao.resultset.core.ParamExtractor;
import org.gobiiproject.gobiidao.resultset.core.ResultColumnApplicator;
import org.gobiiproject.gobiidtomapping.DtoMapMarkerGroup;
import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.dto.DtoMetaData;
import org.gobiiproject.gobiimodel.dto.container.MarkerGroupDTO;
import org.gobiiproject.gobiimodel.dto.container.MarkerGroupMarkerDTO;
import org.gobiiproject.gobiimodel.dto.header.DtoHeaderResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

            ResultSet markersForMarkerGroupResultSet = rsMarkerGroupDao.getMarkersForMarkerGroup(returnVal.getMarkerGroupId());
            while (markersForMarkerGroupResultSet.next()) {

                Integer currentMarkerId = markersForMarkerGroupResultSet.getInt("marker_id");
                ResultSet markerDetailsResultSet = rsMarkerGroupDao.getMarkerByMarkerId(currentMarkerId);
                MarkerGroupMarkerDTO currentMarkerGroupMarkerDTO = new MarkerGroupMarkerDTO();
                if (markerDetailsResultSet.next()) {
                    ResultColumnApplicator.applyColumnValues(markerDetailsResultSet, currentMarkerGroupMarkerDTO);
                    currentMarkerGroupMarkerDTO.setMarkerExists(true);
                } else {
                    currentMarkerGroupMarkerDTO.setMarkerId(currentMarkerId);
                    currentMarkerGroupMarkerDTO.setMarkerExists(false);
                    returnVal.getDtoHeaderResponse().addStatusMessage(DtoHeaderResponse.StatusLevel.OK,
                            DtoHeaderResponse.ValidationStatusType.NONEXISTENT_FK_ENTITY,
                            "A marker id in the marker_group table does not exist " + currentMarkerId);
                }

                returnVal.getMarkers().add(currentMarkerGroupMarkerDTO);
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


    private List<MarkerGroupMarkerDTO> getMarkerGroupMarkersByMarkerName(String markerName) throws SQLException, GobiiDaoException {

        List<MarkerGroupMarkerDTO> returnVal = new ArrayList<>();

        ResultSet markersResultSet = rsMarkerGroupDao.getMarkersByMarkerName(markerName);
        while (markersResultSet.next()) {
            MarkerGroupMarkerDTO currentMarkerGroupMarkerDto = new MarkerGroupMarkerDTO();
            ResultColumnApplicator.applyColumnValues(markersResultSet, currentMarkerGroupMarkerDto);
            returnVal.add(currentMarkerGroupMarkerDto);
        }

        return returnVal;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED) // if we throw a runtime exception, we'll rollback
    public MarkerGroupDTO createMarkerGroup(MarkerGroupDTO markerGroupDTO) throws GobiiDtoMappingException {

        MarkerGroupDTO returnVal = markerGroupDTO;

        try {

            Map<String, Object> parameters = ParamExtractor.makeParamVals(markerGroupDTO);
            Integer markerGroupId = rsMarkerGroupDao.createMarkerGroup(parameters);
            returnVal.setMarkerGroupId(markerGroupId);


            // populate marker DTO's in a way that deals with case of
            // multiple markers with that name
            List<MarkerGroupMarkerDTO> newMarkerDTOsForMarker = new ArrayList<>();
            if ((null != returnVal.getMarkers()) && (returnVal.getMarkers().size() > 0)) {

                for (Iterator<MarkerGroupMarkerDTO> iterator =
                     returnVal.getMarkers().iterator();
                     iterator.hasNext(); ) {

                    MarkerGroupMarkerDTO currentMarkerGroupMarkerDto = iterator.next();

                    List<MarkerGroupMarkerDTO> markerDTOsForMarkerName =
                            getMarkerGroupMarkersByMarkerName(currentMarkerGroupMarkerDto.getMarkerName());

                    if (markerDTOsForMarkerName.size() > 0) {

                        markerDTOsForMarkerName
                                .stream()
                                .forEach(m -> {
                                            m.setFavorableAllele(currentMarkerGroupMarkerDto
                                                    .getFavorableAllele());
                                            m.setMarkerExists(true);
                                        }
                                );

                        // the one from our list has not been populated with values from the query
                        // only the ones in the list we got back have been populated
                        // we'll add the new, populated one to the marker group DTO's list below
                        iterator.remove();

                        newMarkerDTOsForMarker.addAll(markerDTOsForMarkerName);

                    } else if (0 == markerDTOsForMarkerName.size()) {
                        currentMarkerGroupMarkerDto.setMarkerExists(false);
                    }
                }


                returnVal.getMarkers().addAll(newMarkerDTOsForMarker); // if new markers is empty, we don't care

                List<MarkerGroupMarkerDTO> existingMarkers = returnVal.getMarkers()
                        .stream()
                        .filter(m -> m.isMarkerExists())
                        .collect(Collectors.toList());


                if (existingMarkers.size() > 1) {

                    for (MarkerGroupMarkerDTO currentMarkerGroupMarkerDTO : existingMarkers) {

                        Map<String, Object> markerGroupMarkerParameters = new HashMap<>();

                        markerGroupMarkerParameters.put(EntityPropertyParamNames.PROPPCOLARAMNAME_ENTITY_ID, returnVal.getMarkerGroupId());
                        markerGroupMarkerParameters.put(EntityPropertyParamNames.PROPPCOLARAMNAME_PROP_ID, currentMarkerGroupMarkerDTO.getMarkerId());
                        markerGroupMarkerParameters.put(EntityPropertyParamNames.PROPPCOLARAMNAME_VALUE, currentMarkerGroupMarkerDTO.getFavorableAllele());

                        rsMarkerGroupDao.createUpdateMarkerGroupMarker(markerGroupMarkerParameters);

                    }
                } else {

                    throw new GobiiDtoMappingException(DtoHeaderResponse.StatusLevel.ERROR,
                            DtoHeaderResponse.ValidationStatusType.NONEXISTENT_FK_ENTITY,
                            "None of the specified markers exists");

                } // if else at least one marker is valid

            } // if any markers were specified


        } catch (
                SQLException e
                )

        {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Error mapping result set to DTO", e);
        } catch (
                GobiiDaoException e
                )

        {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Error mapping result set to DTO", e);
        }

        return returnVal;
    }

    @Override
    public MarkerGroupDTO updateMarkerGroup(MarkerGroupDTO markerGroupDTO) throws GobiiDtoMappingException {

        MarkerGroupDTO returnVal = markerGroupDTO;

        try {

            Map<String, Object> parameters = ParamExtractor.makeParamVals(returnVal);
            rsMarkerGroupDao.updateMarkerGroup(parameters);

            List<MarkerGroupMarkerDTO> markerGroupMarkerDTOs = returnVal.getMarkers();
            for (MarkerGroupMarkerDTO currentMarkerGroupMarkerDTO : markerGroupMarkerDTOs) {

                if (DtoMetaData.ProcessType.CREATE == currentMarkerGroupMarkerDTO.getProcessType()) {

                } else if (DtoMetaData.ProcessType.UPDATE == currentMarkerGroupMarkerDTO.getProcessType()) {

                } else if (DtoMetaData.ProcessType.DELETE == currentMarkerGroupMarkerDTO.getProcessType()) {

                }
            }

        } catch (GobiiDaoException e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error(e.getMessage());
        }

        return returnVal;
    }
}
