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
import java.util.*;

public class MarkerGroupDTO extends DtoMetaData {


    public MarkerGroupDTO() {
    }


    private Integer markerGroupId;
    private String name;
    private String code;
    private Serializable markers;
    private String germplasmGroup;
    private String createdBy;
    private Date createDate;
    private String modifiedBy;
    private Date modifiedDate;
    private Integer status;


    @GobiiEntityParam(paramName="markerGroupId")
    public Integer getMarkerGroupId() {
        return markerGroupId;
    }

    @GobiiEntityColumn(columnName = "marker_group_id")
    public void setMarkerGroupId(Integer markerGroupId) {
        this.markerGroupId = markerGroupId;
    }


    @GobiiEntityParam(paramName="status")
    public Integer getStatus() {
        return status;
    }

    @GobiiEntityColumn(columnName = "status")
    public void setStatus(Integer status) {
        this.status = status;
    }

    @GobiiEntityParam(paramName="modifiedDate")
    public Date getModifiedDate() {
        return modifiedDate;
    }

    @GobiiEntityColumn(columnName = "modified_date")
    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    @GobiiEntityParam(paramName="modifiedBy")
    public String getModifiedBy() {
        return modifiedBy;
    }

    @GobiiEntityColumn(columnName = "modified_by")
    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    @GobiiEntityParam(paramName="createDate")
    public Date getCreateDate() {
        return createDate;
    }

    @GobiiEntityColumn(columnName = "create_date")
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @GobiiEntityParam(paramName="createdBy")
    public String getCreatedBy() {
        return createdBy;
    }

    @GobiiEntityColumn(columnName = "created_by")
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @GobiiEntityParam(paramName="germplasmGroup")
    public String getGermplasmGroup() {
        return germplasmGroup;
    }

    @GobiiEntityColumn(columnName = "germplasm_group")
    public void setGermplasmGroup(String germplasmGroup) {
        this.germplasmGroup = germplasmGroup;
    }

    public Serializable getMarkers() {
        return markers;
    }

    public void setMarkers(Serializable markers) {
        this.markers = markers;
    }

    @GobiiEntityParam(paramName="code")
    public String getCode() {
        return code;
    }

    @GobiiEntityColumn(columnName = "code")
    public void setCode(String code) {
        this.code = code;
    }

    @GobiiEntityParam(paramName="name")
    public String getName() {
        return name;
    }

    @GobiiEntityColumn(columnName = "name")
    public void setName(String name) {
        this.name = name;
    }



    // ******************** below this line is garbage and can probably be removed
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
