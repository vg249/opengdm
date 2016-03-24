// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-24
// ************************************************************************
package org.gobiiproject.gobiimodel.dto.container;


import org.gobiiproject.gobiimodel.dto.DtoMetaData;

import java.util.*;

public class MarkerGroupDTO extends DtoMetaData {


    public MarkerGroupDTO() {}

    private Map<String, List<String>> markerMap = new HashMap<>();

    public void setMarkerMap(Map<String, List<String>> markerMap) {
        this.markerMap = markerMap;
    }

    public Map getMarkerMap() {
        return markerMap;
    }

    public Set<String> getMarkerGroups() {
        return markerMap.keySet();
    }

}//ResourceDTO
