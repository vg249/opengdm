package org.gobiiproject.gobiimodel.entity.JpaConverters;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.gobiiproject.gobiimodel.config.GobiiException;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * JPA convertor class to convert Postgresql Database json string to jackson Json Node and vice versa.
 */
@Converter
public class JsonbConverter implements AttributeConverter<JsonNode, String> {

    /**
     * Converts json node to string.
     * @param jsonObject - Jackson Json Node
     * @return
     */
    @Override
    public String convertToDatabaseColumn(JsonNode jsonObject) {
        return jsonObject.toString();
    }

    /**
     * Converts json string to jackson json node.
     * @param jsonString - Json string.
     * @return
     */
    @Override
    public JsonNode convertToEntityAttribute(String jsonString) {

        try {

            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode jsonNode = objectMapper.readTree(jsonString);

            return  jsonNode;
        }
        catch(Exception e) {
            throw new GobiiException("Conversion of jsonb database column failed");
        }

    }

}
