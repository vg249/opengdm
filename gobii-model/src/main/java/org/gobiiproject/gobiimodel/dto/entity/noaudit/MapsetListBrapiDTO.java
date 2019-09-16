package org.gobiiproject.gobiimodel.dto.entity.noaudit;

import org.gobiiproject.gobiimodel.dto.entity.annotations.GobiiEntityColumn;

/**
 * Brapi DTO for Genome Maps List
 */
public class MapsetListBrapiDTO {


    private Integer mapDbId;

   private String comments;

   private String type;

   private String name;

   private Integer linkageGroupCount;

    public String getComments() {
        return comments;
    }

    public Integer getMapDbId() {
        return mapDbId;
    }

    @GobiiEntityColumn(columnName = "mapset_id")
    public void setMapDbId(Integer mapDbId) {
        this.mapDbId = mapDbId;
    }

    @GobiiEntityColumn(columnName = "description")
    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getType() {
        return type;
    }

    @GobiiEntityColumn(columnName = "type")
    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    @GobiiEntityColumn(columnName = "name")
    public void setName(String name) {
        this.name = name;
    }

    public Integer getLinkageGroupCount() {
        return linkageGroupCount;
    }

    @GobiiEntityColumn(columnName = "linkage_group_count")
    public void setLinkageGroupCount(Integer linkageGroupCount) {
        this.linkageGroupCount = linkageGroupCount;
    }

    public Integer getMarkerCount() {
        return markerCount;
    }

    @GobiiEntityColumn(columnName = "marker_count")
    public void setMarkerCount(Integer markerCount) {
        this.markerCount = markerCount;
    }

    private Integer markerCount;

}
