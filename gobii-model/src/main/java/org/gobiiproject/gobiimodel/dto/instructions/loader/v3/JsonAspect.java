package org.gobiiproject.gobiimodel.dto.instructions.loader.v3;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Map;

@JsonFormat(shape=JsonFormat.Shape.ARRAY)
public class JsonAspect {

    private final String aspectType = "JSON";

    private Map<String, Aspect> jsonMap;

    public String getAspectType() {
        return aspectType;
    }

    public Map<String, Aspect> getJsonMap() {
        return jsonMap;
    }

    public void setJsonMap(Map<String, Aspect> jsonMap) {
        this.jsonMap = jsonMap;
    }
}
