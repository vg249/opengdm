// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-24
// ************************************************************************
package org.gobiiproject.gobiimodel.dto.container;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.gobiiproject.gobiimodel.dto.DtoMetaData;
import org.gobiiproject.gobiimodel.dto.annotations.GobiiEntityColumn;
import org.gobiiproject.gobiimodel.dto.annotations.GobiiEntityParam;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MarkerGroupMarkerDTO extends DtoMetaData {


    public MarkerGroupMarkerDTO() {
    }

    boolean markerExists = true;
    Integer markerId;
    String markerName;
    Integer platformId;
    String platformName;
    String favorableAllele;

    public boolean isMarkerExists() {
        return markerExists;
    }

    public void setMarkerExists(boolean markerExists) {
        this.markerExists = markerExists;
    }

    public Integer getMarkerId() {
        return markerId;
    }

    @GobiiEntityColumn(columnName = "marker_id")
    public void setMarkerId(Integer markerId) {
        this.markerId = markerId;
    }

    public String getMarkerName() {
        return markerName;
    }

    @GobiiEntityColumn(columnName = "marker_name")
    public void setMarkerName(String markerName) {
        this.markerName = markerName;
    }

    public Integer getPlatformId() {
        return platformId;
    }

    @GobiiEntityColumn(columnName = "platform_id")
    public void setPlatformId(Integer platformId) {
        this.platformId = platformId;
    }

    public String getPlatformName() {
        return platformName;
    }

    @GobiiEntityColumn(columnName = "platform_name")
    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public String getFavorableAllele() {
        return favorableAllele;
    }

    public void setFavorableAllele(String favorableAllele) {
        this.favorableAllele = favorableAllele;
    }
}
