package org.gobiiproject.gobiimodel.dto.entity.noaudit;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.gobiiproject.gobiimodel.dto.entity.annotations.GobiiEntityColumn;

import java.util.List;

/**
 * Brapi DTO for Genome Maps List
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MapsetBrapiDTO {


    private Integer mapDbId;

    private String mapName;

    private String comments;

    private String type;

    private Integer linkageGroupCount;

    private Integer markerCount;

    public Integer getMapDbId() {
        return mapDbId;
    }

    public void setMapDbId(Integer mapDbId) {
        this.mapDbId = mapDbId;
    }

    public String getMapName() {
        return mapName;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getLinkageGroupCount() {
        return linkageGroupCount;
    }

    public void setLinkageGroupCount(Integer linkageGroupCount) {
        this.linkageGroupCount = linkageGroupCount;
    }

    public Integer getMarkerCount() {
        return markerCount;
    }

    public void setMarkerCount(Integer markerCount) {
        this.markerCount = markerCount;
    }
}
