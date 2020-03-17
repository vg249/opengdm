/**
 * ProjectProperties.java
 * 
 * Project Properties attribute type in Project class
 * @author Rodolfo N. Duldulao, Jr.
 * @since 2020-03-13
 */
package org.gobiiproject.gobiimodel.entity.pgsql;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ProjectProperties implements java.io.Serializable, Map<String,String> {
    /**
     *
     */
    private static final long serialVersionUID = -8869498320638000413L;
    private Map<String, String> values = new java.util.HashMap<String, String>();

    public JsonNode getProperties() {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.createObjectNode();
        if (values != null) {
            jsonNode = objectMapper.valueToTree(values);
        }
        return jsonNode;
    }

    public void setProperties(JsonNode properties) {
        //convert to map
        values.clear();
        properties.fieldNames().forEachRemaining(
            fieldName -> {
                values.put(fieldName, (properties.get(fieldName)).asText());
            }
        );
    }

    


    @Override
    public int size() {
        return values.size();
    }

    @Override
    public boolean isEmpty() {
        return values.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return values.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return values.containsValue(value);
    }

    @Override
    public String get(Object key) {
        return values.get(key);
    }

    @Override
    public String put(String key, String value) {
        return values.put(key.toString(), value.toString());
    }

    @Override
    public String remove(Object key) {
        return values.remove(key);
    }

    @Override
    public void putAll(Map m) {
        // TODO Auto-generated method stub
        values.putAll(m);

    }

    @Override
    public void clear() {
        values.clear();

    }

    @Override
    public Set<String> keySet() {
        return values.keySet();
    }

    @Override
    public Collection<String> values() {
        return values.values();
    }

    @Override
    public Set<Map.Entry<String,String>> entrySet() {
        return values.entrySet();
    }
    
    
}