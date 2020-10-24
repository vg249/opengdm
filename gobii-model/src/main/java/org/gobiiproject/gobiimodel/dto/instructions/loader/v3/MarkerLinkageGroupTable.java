package org.gobiiproject.gobiimodel.dto.instructions.loader.v3;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class MarkerLinkageGroupTable implements Table {

    @JsonProperty("platform_id")
    @NotNull(message = "platform id is required")
    private String platformId;

    @JsonProperty("marker_name")
    @NotNull(message = "marker name is required")
    private ColumnAspect markerName;

    @JsonProperty("linkage_group_name")
    @NotNull(message = "linkage group name is required")
    private ColumnAspect linkageGroupName;

    @JsonProperty("map_id")
    @NotNull(message = "map id is required")
    private String mapId;

    private ColumnAspect start;

    private ColumnAspect stop;

    public String getPlatformId() {
        return platformId;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }

    public ColumnAspect getMarkerName() {
        return markerName;
    }

    public void setMarkerName(ColumnAspect markerName) {
        this.markerName = markerName;
    }

    public ColumnAspect getLinkageGroupName() {
        return linkageGroupName;
    }

    public void setLinkageGroupName(ColumnAspect linkageGroupName) {
        this.linkageGroupName = linkageGroupName;
    }

    public String getMapId() {
        return mapId;
    }

    public void setMapId(String mapId) {
        this.mapId = mapId;
    }

    public ColumnAspect getStart() {
        return start;
    }

    public void setStart(ColumnAspect start) {
        this.start = start;
    }

    public ColumnAspect getStop() {
        return stop;
    }

    public void setStop(ColumnAspect stop) {
        this.stop = stop;
    }
}
