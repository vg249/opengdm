package org.gobiiproject.gobiimodel.modelmapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.lang.StringUtils;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.children.CvPropertyDTO;
import org.gobiiproject.gobiimodel.entity.Cv;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;

/**
 * Module to map CV Ids to CV terms and vice versa.
 */
public class CvIdCvTermMapper {

    /**
     * Function maps the properties object with CV term as a key to Json Node with
     * cvIds as Key.
     *
     * @param cvList     List of Cv Entity
     * @param properties Map object with cv term as key which needs to be mapped to
     *                   their cvId as key
     * @return Json Node with cvId as Key. Returning Json Node as Entity Column is a
     *         Json Column.
     */
    public static JsonNode mapCvTermsToCvId(List<Cv> cvList, Map<String, String> properties) {

        JsonNode returnVal;

        ObjectMapper mapper = new ObjectMapper();

        try {

            Map<String, String> cvIdMappedProperties = new HashMap<>();

            for (Cv cv : cvList) {
                if (properties.containsKey(cv.getTerm())) {

                    cvIdMappedProperties.put(cv.getCvId().toString(), properties.get(cv.getTerm()));

                }
            }

            returnVal = mapper.reader().readTree(mapper.writeValueAsString(cvIdMappedProperties));

        } catch (Exception e) {

            throw new GobiiException(
                    GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNKNOWN,
                    e.getMessage());

        }

        return returnVal;
    }


    public static Map<String, String> mapCvIdToCvTerms(List<Cv> cvList, JsonNode propertiesJson) {

        Map<String, String> returnVal = new HashMap<>();

        return mapCvIdToCvTerms(cvList, propertiesJson, returnVal);

    }

    public static Map<String, String> mapCvIdToCvTerms(List<Cv> cvList,
                                                       JsonNode propertiesJson, Map<String, String> returnVal) {

        String cvValue;

        try {
            for(Cv cv : cvList) {
                if(propertiesJson.has(cv.getCvId().toString())) {

                    cvValue = propertiesJson.get(cv.getCvId().toString()).asText();

                    if(StringUtils.isNotEmpty(cvValue)) {
                        returnVal.put(cv.getTerm(), cvValue);
                    }
                }
            }
        }
        catch(Exception e) {

            throw new GobiiException(
                    GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNKNOWN,
                    e.getMessage());

        }

        return returnVal;

    }

    /**
     * Convert Cv data to List
     * @param cvList
     * @param propertiesJson
     * @return
     */
    public static List<CvPropertyDTO> listCvIdToCvTerms(List<Cv> cvList, JsonNode propertiesJson) {
        List<CvPropertyDTO> dtoList = new java.util.ArrayList<>();
        try {
            for(Cv cv : cvList) {
                if(propertiesJson.has(cv.getCvId().toString())) {

                    CvPropertyDTO dto = new CvPropertyDTO();
                    ModelMapper.mapEntityToDto(cv, dto);
                    dto.setPropertyType(cv.getCvGroup().getCvGroupType());
                    dto.setPropertyValue(propertiesJson.get(cv.getCvId().toString()).asText());
                    dtoList.add(dto);
                }
            }
        }
        catch(Exception e) {

            throw new GobiiException(
                    GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNKNOWN,
                    e.getMessage());

        }
        return dtoList;
    }


    /**
     * Convert Cv db entities to DTO
     * @param cvList
     * @return
     */
    public static List<CvPropertyDTO> convert(List<Cv> cvList) {
        List<CvPropertyDTO> dtoList = new java.util.ArrayList<>();
        try {
            for(Cv cv : cvList) {
                CvPropertyDTO dto = new CvPropertyDTO();
                ModelMapper.mapEntityToDto(cv, dto);
                dto.setPropertyType(cv.getCvGroup().getCvGroupType());
                dtoList.add(dto);
            }
        }
        catch(Exception e) {

            throw new GobiiException(
                    GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNKNOWN,
                    e.getMessage());

        }
        return dtoList;
    }
}
