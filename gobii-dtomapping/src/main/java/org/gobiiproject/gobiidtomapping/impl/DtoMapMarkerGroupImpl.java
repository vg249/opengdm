package org.gobiiproject.gobiidtomapping.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.resultset.access.RsMarkerGroupDao;
import org.gobiiproject.gobiidao.resultset.core.EntityPropertyParamNames;
import org.gobiiproject.gobiidao.resultset.core.ParamExtractor;
import org.gobiiproject.gobiidao.resultset.core.ResultColumnApplicator;
import org.gobiiproject.gobiidtomapping.DtoMapMarkerGroup;
import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.headerlesscontainer.MarkerGroupDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.MarkerGroupMarkerDTO;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;

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


    public MarkerGroupDTO getMarkerGroupDetails(Integer markerGroupId) throws GobiiDtoMappingException {

        MarkerGroupDTO returnVal = new MarkerGroupDTO();

        try {

            ResultSet resultSet = rsMarkerGroupDao.getMarkerGroupDetailByMarkerGroupId(markerGroupId);

            if (resultSet.next()) {
                ResultColumnApplicator.applyColumnValues(resultSet, returnVal);
            }

            ResultSet markersForMarkerGroupResultSet = rsMarkerGroupDao.getMarkersForMarkerGroup(returnVal.getMarkerGroupId());
            while (markersForMarkerGroupResultSet.next()) {

                Integer currentMarkerId = markersForMarkerGroupResultSet.getInt("marker_id");
                String favorableAllele = markersForMarkerGroupResultSet.getString("favorable_allele");
                ResultSet markerDetailsResultSet = rsMarkerGroupDao.getMarkerByMarkerId(currentMarkerId);
                MarkerGroupMarkerDTO currentMarkerGroupMarkerDTO = new MarkerGroupMarkerDTO();
                if (markerDetailsResultSet.next()) {
                    ResultColumnApplicator.applyColumnValues(markerDetailsResultSet, currentMarkerGroupMarkerDTO);
                    currentMarkerGroupMarkerDTO.setMarkerExists(true);
                    currentMarkerGroupMarkerDTO.setFavorableAllele(favorableAllele);
                } else {
                    currentMarkerGroupMarkerDTO.setMarkerId(currentMarkerId);
                    currentMarkerGroupMarkerDTO.setMarkerExists(false);
                }

                returnVal.getMarkers().add(currentMarkerGroupMarkerDTO);
            }

        } catch (Exception e) {
            LOGGER.error("Gobii Maping Error", e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;

    }

    @Override
    public List<MarkerGroupDTO> getMarkerGroups() throws GobiiDtoMappingException {

        List<MarkerGroupDTO> returnVal = new ArrayList<>();

        try {
            ResultSet resultSet = rsMarkerGroupDao.getMarkerGroupNames();
            while (resultSet.next()) {
                MarkerGroupDTO currentMarkerGroupDTO = new MarkerGroupDTO();
                currentMarkerGroupDTO.setName(resultSet.getString("name"));
                currentMarkerGroupDTO.setMarkerGroupId(resultSet.getInt("marker_group_id"));

                returnVal.add(currentMarkerGroupDTO);
            }

        } catch (SQLException e) {
            LOGGER.error("Gobii Mapping Error", e);
            throw new GobiiDtoMappingException(e);
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


    private void populateMarkers(List<MarkerGroupMarkerDTO> markerGroupMarkers) throws SQLException, GobiiDaoException {

        List<MarkerGroupMarkerDTO> newMarkerDTOsForMarker = new ArrayList<>();

        for (Iterator<MarkerGroupMarkerDTO> iterator =
             markerGroupMarkers
                     .iterator();
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


        markerGroupMarkers.addAll(newMarkerDTOsForMarker); // if new markers is empty, we don't care
    }


    private void upsertMarkers(Integer markerGroupId, List<MarkerGroupMarkerDTO> markerDTOs) throws SQLException, GobiiDaoException {

        for (MarkerGroupMarkerDTO currentMarkerGroupMarkerDTO : markerDTOs) {

            if (null == currentMarkerGroupMarkerDTO.getFavorableAllele() ||
                    currentMarkerGroupMarkerDTO.getFavorableAllele().isEmpty()) {
                throw new GobiiDtoMappingException(GobiiStatusLevel.ERROR,
                        GobiiValidationStatusType.MISSING_REQUIRED_VALUE,
                        "The no allele value was specified for marker nane " + currentMarkerGroupMarkerDTO.getMarkerName());
            }

            Map<String, Object> markerGroupMarkerParameters = new HashMap<>();

            markerGroupMarkerParameters.put(EntityPropertyParamNames.PROPPCOLARAMNAME_ENTITY_ID, markerGroupId);
            markerGroupMarkerParameters.put(EntityPropertyParamNames.PROPPCOLARAMNAME_PROP_ID, currentMarkerGroupMarkerDTO.getMarkerId());
            markerGroupMarkerParameters.put(EntityPropertyParamNames.PROPPCOLARAMNAME_VALUE, currentMarkerGroupMarkerDTO.getFavorableAllele());

            rsMarkerGroupDao.createUpdateMarkerGroupMarker(markerGroupMarkerParameters);

        }


    } // upsertMarkers

    private String buildMarkers(List<MarkerGroupMarkerDTO> markerGroupMarkerDTOS) {

        JsonObject returnVal = new JsonObject();

        String returnStr = "";


        for (MarkerGroupMarkerDTO currentMarkerGroupMarkerDTO : markerGroupMarkerDTOS) {

            returnVal.addProperty(currentMarkerGroupMarkerDTO.getMarkerId().toString(), currentMarkerGroupMarkerDTO.getFavorableAllele());


        }

        Gson gson = new GsonBuilder().create();
        returnStr = gson.toJson(returnVal);


        return returnStr;



    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED) // if we throw a runtime exception, we'll rollback
    public MarkerGroupDTO createMarkerGroup(MarkerGroupDTO markerGroupDTO) throws GobiiDtoMappingException {

        MarkerGroupDTO returnVal = markerGroupDTO;

        try {

            // check if MarkerGroupDTO has specified markers
            // if yes, do validation for the markers

            if ((null != markerGroupDTO.getMarkers()) && (markerGroupDTO.getMarkers().size() > 0)) {

                populateMarkers(markerGroupDTO.getMarkers());
                List<MarkerGroupMarkerDTO> nonExistingMarkers = returnVal.getMarkers()
                        .stream()
                        .filter(m -> !m.isMarkerExists())
                        .collect(Collectors.toList());

                if (nonExistingMarkers.size() > 0) {

                    throw new GobiiDtoMappingException(GobiiStatusLevel.ERROR,
                            GobiiValidationStatusType.NONEXISTENT_FK_ENTITY,
                            "The following markers don't exist.");

                }

            }

            // populate marker DTO's in a way that deals with case of
            // multiple markers with that name

            List<MarkerGroupMarkerDTO> existingMarkers = returnVal.getMarkers()
                    .stream()
                    .filter(m -> m.isMarkerExists())
                    .collect(Collectors.toList());

            String markers = "";

            if (existingMarkers.size() > 0) {

//                upsertMarkers(returnVal.getMarkerGroupId(), existingMarkers);
                markers = buildMarkers(existingMarkers);

            }


            Map<String, Object> parameters = ParamExtractor.makeParamVals(markerGroupDTO);
            parameters.put("markers", markers);
            Integer markerGroupId = rsMarkerGroupDao.createMarkerGroup(parameters);
            returnVal.setMarkerGroupId(markerGroupId);




        } catch (Exception e) {
            LOGGER.error("Gobii Maping Error", e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;
    }

    @Override
    public MarkerGroupDTO replaceMarkerGroup(Integer markerGroupId, MarkerGroupDTO markerGroupDTO) throws GobiiDtoMappingException {

        MarkerGroupDTO returnVal = markerGroupDTO;

        try {

            // *********************************************************************************
            // CREATE NEW MARKERS
            List<MarkerGroupMarkerDTO> markerGroupMarkersToCreate =
                    returnVal.getMarkers()
                            .stream()
                            //.filter(m -> m.getGobiiProcessType() == GobiiProcessType.CREATE)
                            .collect(Collectors.toList());

            populateMarkers(markerGroupMarkersToCreate);

            List<MarkerGroupMarkerDTO> existingMarkers = markerGroupMarkersToCreate
                    .stream()
                    .filter(m -> m.isMarkerExists())
                    .collect(Collectors.toList());

            List<MarkerGroupMarkerDTO> nonExistingMarkers = markerGroupMarkersToCreate
                    .stream()
                    .filter(m -> !m.isMarkerExists())
                    .collect(Collectors.toList());

            // check if there are non existent markers

            if(nonExistingMarkers.size() > 0) {

                throw new GobiiDtoMappingException(GobiiStatusLevel.ERROR,
                        GobiiValidationStatusType.NONEXISTENT_FK_ENTITY,
                        "Some of the specified markers doesn't exists");

            }


            Map<String, Object> parameters = ParamExtractor.makeParamVals(returnVal);
            parameters.put("markerGroupId", markerGroupId);
            rsMarkerGroupDao.updateMarkerGroup(parameters);

            if (existingMarkers.size() > 0) {

                upsertMarkers(returnVal.getMarkerGroupId(), existingMarkers);

            }



        } catch (Exception e) {
            LOGGER.error("Gobii Maping Error", e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;
    }

}
