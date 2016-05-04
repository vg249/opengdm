package org.gobiiproject.gobiiclient.dtorequests.Helpers;

import org.gobiiproject.gobiimodel.dto.container.EntityPropertyDTO;
import org.gobiiproject.gobiimodel.dto.header.DtoHeaderResponse;
import org.w3c.dom.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Phil on 5/4/2016.
 */
public class EntityParamValues {

    private List<EntityPropertyDTO> properties = new ArrayList<>();

    public List<EntityPropertyDTO> getProperties() {
        return properties;
    }

    public void add(String name, String value) {
        properties.add(new EntityPropertyDTO(null, null, name, value));
    }

    public boolean compare(List<EntityPropertyDTO> propertiesToCompare) {

        boolean returnVal = true;

        for (int idx = 0; (idx < properties.size()) && returnVal; idx++) {

            EntityPropertyDTO currentProperty = properties.get(idx);

            List<EntityPropertyDTO> matchedProperties = propertiesToCompare
                    .stream()
                    .filter(m -> m.getPropertyName().equals(currentProperty.getPropertyName()) )
                    .collect(Collectors.toList());

            if (matchedProperties.size() == 1) {
                EntityPropertyDTO matchedProperty = matchedProperties.get(0);
                returnVal = currentProperty.getPropertyValue().equals(matchedProperty.getPropertyValue());

            } else {
                returnVal = false;
            }

        }

        return returnVal;
    }
}
