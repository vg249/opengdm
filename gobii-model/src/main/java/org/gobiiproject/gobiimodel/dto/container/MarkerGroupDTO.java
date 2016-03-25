// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-24
// ************************************************************************
package org.gobiiproject.gobiimodel.dto.container;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.gobiiproject.gobiimodel.dto.DtoMetaData;

import java.util.*;

public class MarkerGroupDTO extends DtoMetaData {


    public MarkerGroupDTO() {}

    private Map<String, List<String>> markerMap = new HashMap<>();

    public Map<String, List<String>> getMarkerMap() {
        return markerMap;
    }

    public void setMarkerMap(Map<String, List<String>> markerMap) {
        this.markerMap = markerMap;
    }

    @JsonIgnore
    public Set<String> getMarkerGroups() {
        return markerMap.keySet();
    }

}//ResourceDTO
