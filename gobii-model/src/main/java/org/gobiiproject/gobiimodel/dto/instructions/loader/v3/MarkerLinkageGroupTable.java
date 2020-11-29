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
    private Aspect markerName;

    @JsonProperty("platform_name")
    private Aspect platformName;

    @JsonProperty("linkage_group_name")
    @NotNull(message = "linkage group name is required")
    private Aspect linkageGroupName;

    @JsonProperty("map_id")
    private String mapId;

    @JsonProperty("map_name")
    private Aspect mapName;

    @JsonProperty("start")
    private Aspect markerStart;

    @JsonProperty("stop")
    private Aspect markerStop;

    public String getPlatformId() {
        return platformId;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }

    public Aspect getMarkerName() {
        return markerName;
    }

    public void setMarkerName(Aspect markerName) {
        this.markerName = markerName;
    }

    public Aspect getPlatformName() {
        return platformName;
    }

    public void setPlatformName(Aspect platformName) {
        this.platformName = platformName;
    }

    public Aspect getLinkageGroupName() {
        return linkageGroupName;
    }

    public void setLinkageGroupName(Aspect linkageGroupName) {
        this.linkageGroupName = linkageGroupName;
    }

    public String getMapId() {
        return mapId;
    }

    public void setMapId(String mapId) {
        this.mapId = mapId;
    }

    public Aspect getMapName() {
        return mapName;
    }

    public void setMapName(Aspect mapName) {
        this.mapName = mapName;
    }

    public Aspect getMarkerStart() {
        return markerStart;
    }

    public void setMarkerStart(Aspect markerStart) {
        this.markerStart = markerStart;
    }

    public Aspect getMarkerStop() {
        return markerStop;
    }

    public void setMarkerStop(Aspect markerStop) {
        this.markerStop = markerStop;
    }
}
