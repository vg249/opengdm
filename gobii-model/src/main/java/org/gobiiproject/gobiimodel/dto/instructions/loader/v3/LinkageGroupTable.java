package org.gobiiproject.gobiimodel.dto.instructions.loader.v3;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LinkageGroupTable implements Table {

    @JsonProperty("map_id")
    @NotNull(message = "map id is required")
    private String mapId;

    @NotNull(message = "linkage group name is required")
    private ColumnAspect name;

    private ColumnAspect start;

    private ColumnAspect stop;

    public String getMapId() {
        return mapId;
    }

    public void setMapId(String mapId) {
        this.mapId = mapId;
    }

    public ColumnAspect getName() {
        return name;
    }

    public void setName(ColumnAspect name) {
        this.name = name;
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
