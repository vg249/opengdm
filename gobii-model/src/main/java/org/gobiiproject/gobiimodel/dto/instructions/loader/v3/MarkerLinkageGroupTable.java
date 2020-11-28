package org.gobiiproject.gobiimodel.dto.instructions.loader.v3;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.gobiiproject.gobiimodel.dto.annotations.GobiiAspectTable;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;

@JsonInclude(JsonInclude.Include.NON_NULL)
@GobiiAspectTable(name="marker_linkage_group")
public class MarkerLinkageGroupTable implements Table {

    @JsonProperty("platform_id")
    private String platformId;

    @JsonProperty("marker_name")
    @NotNull(message = "marker name is required")
    private ColumnAspect markerName;

    @JsonProperty("platform_name")
    private ColumnAspect platformName;

    @JsonProperty("linkage_group_name")
    @NotNull(message = "linkage group name is required")
    private ColumnAspect linkageGroupName;

    @JsonProperty("map_id")
    private String mapId;

    @JsonProperty("map_name")
    private ColumnAspect mapName;

    @JsonProperty("start")
    private ColumnAspect markerStart;

    @JsonProperty("stop")
    private ColumnAspect markerStop;

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

    public ColumnAspect getPlatformName() {
        return platformName;
    }

    public void setPlatformName(ColumnAspect platformName) {
        this.platformName = platformName;
    }

    public String getMapId() {
        return mapId;
    }

    public void setMapId(String mapId) {
        this.mapId = mapId;
    }

    public ColumnAspect getMarkerStart() {
        return markerStart;
    }

    public void setMarkerStart(ColumnAspect markerStart) {
        this.markerStart = markerStart;
    }

    public ColumnAspect getMarkerStop() {
        return markerStop;
    }

    public void setMarkerStop(ColumnAspect markerStop) {
        this.markerStop = markerStop;
    }

    public ColumnAspect getMapName() {
        return mapName;
    }

    public void setMapName(ColumnAspect mapName) {
        this.mapName = mapName;
    }
}
