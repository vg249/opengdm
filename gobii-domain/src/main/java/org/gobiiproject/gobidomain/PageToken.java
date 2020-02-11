package org.gobiiproject.gobidomain;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class PageToken {

    private static Logger LOGGER = LoggerFactory.getLogger(PageToken.class);

    /**
     * Encode the Map object with cursor keys and values into a base 64 encoded string.
     * @param cursorMap - Map with cursor keys and their respective values
     * @return encoded string to be used as page token
     */
    public static String encode(Map<String, Integer> cursorMap) {
       try {
           String cursorMapAsJsonString = new ObjectMapper().writeValueAsString(cursorMap);
           return Base64.getEncoder().encodeToString(cursorMapAsJsonString.getBytes());
       }
       catch(Exception e) {

           LOGGER.error("Unable to encode hashmap to base64 string", e);

            throw new GobiiDomainException(GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.NONE,
                    "Internal server error. Please check the error log.");
       }
    }

    /**
     * Decodes the pageToken into the cursorMap with cursor keys and their respective values
     * @param pageToken - page token to decoded.
     * @return - Map with cursor keys and their respective integer values
     */
    public static Map<String, Integer> decode(String pageToken) {

        try {

            if(pageToken == null || pageToken.isEmpty()) {
                return null;
            }

            byte[] decodedBytes = Base64.getDecoder().decode(pageToken);
            String cursorMapJsonString = new String(decodedBytes);

            Map<String, Integer> cursorMap = (new ObjectMapper()).readValue(cursorMapJsonString,
                    new TypeReference<HashMap<String, Integer>>(){});

            return cursorMap;

        }
        catch(InvalidFormatException je) {

            LOGGER.error("Unable to parse page token json string", je);

            throw new GobiiDomainException(GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.BAD_REQUEST,
                    "Invalid page token");

        }
        catch(Exception e) {

            LOGGER.error("Unable to decode pageToken", e);

            throw new GobiiDomainException(GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.NONE,
                    "Internal server error. Please check the error log.");
        }

    }


}