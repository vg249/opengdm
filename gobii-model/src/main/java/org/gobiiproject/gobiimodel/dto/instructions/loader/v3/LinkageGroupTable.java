package org.gobiiproject.gobiimodel.dto.instructions.loader.v3;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LinkageGroupTable implements Table {

    @JsonProperty("map_id")
    @NotNull(message = "map id is required")
    private String mapId;

    @NotNull(message = "linkage group name is required")
    @JsonProperty("name")
    private ColumnAspect linkageGroupName;

    @JsonProperty("start")
    private ColumnAspect linkageGroupStart;

    @JsonProperty("stop")
    private ColumnAspect linkageGroupStop;

    public String getMapId() {
        return mapId;
    }

    public void setMapId(String mapId) {
        this.mapId = mapId;
    }

    public ColumnAspect getLinkageGroupName() {
        return linkageGroupName;
    }

    public void setLinkageGroupName(ColumnAspect linkageGroupName) {
        this.linkageGroupName = linkageGroupName;
    }

    public ColumnAspect getLinkageGroupStart() {
        return linkageGroupStart;
    }

    public void setLinkageGroupStart(ColumnAspect linkageGroupStart) {
        this.linkageGroupStart = linkageGroupStart;
    }

    public ColumnAspect getLinkageGroupStop() {
        return linkageGroupStop;
    }

    public void setLinkageGroupStop(ColumnAspect linkageGroupStop) {
        this.linkageGroupStop = linkageGroupStop;
    }

}
