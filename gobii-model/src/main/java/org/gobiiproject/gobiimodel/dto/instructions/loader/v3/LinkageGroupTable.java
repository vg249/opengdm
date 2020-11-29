package org.gobiiproject.gobiimodel.dto.instructions.loader.v3;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.gobiiproject.gobiimodel.dto.annotations.GobiiAspectTable;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;

@JsonInclude(JsonInclude.Include.NON_NULL)
@GobiiAspectTable(name = "linkage_group")
public class LinkageGroupTable implements Table {

    @JsonProperty("map_id")
    private String mapId;

    @JsonProperty("map_name")
    private Aspect mapName;

    @NotNull(message = "linkage group name is required")
    @JsonProperty("name")
    private Aspect linkageGroupName;

    @JsonProperty("start")
    private Aspect linkageGroupStart;

    @JsonProperty("stop")
    private Aspect linkageGroupStop;

    public String getMapId() {
        return mapId;
    }

    public void setMapId(String mapId) {
        this.mapId = mapId;
    }

    public Aspect getLinkageGroupName() {
        return linkageGroupName;
    }

    public void setLinkageGroupName(ColumnAspect linkageGroupName) {
        this.linkageGroupName = linkageGroupName;
    }

    public Aspect getMapName() {
        return mapName;
    }

    public void setMapName(Aspect mapName) {
        this.mapName = mapName;
    }

    public void setLinkageGroupName(Aspect linkageGroupName) {
        this.linkageGroupName = linkageGroupName;
    }

    public Aspect getLinkageGroupStart() {
        return linkageGroupStart;
    }

    public void setLinkageGroupStart(Aspect linkageGroupStart) {
        this.linkageGroupStart = linkageGroupStart;
    }

    public Aspect getLinkageGroupStop() {
        return linkageGroupStop;
    }

    public void setLinkageGroupStop(Aspect linkageGroupStop) {
        this.linkageGroupStop = linkageGroupStop;
    }
}
