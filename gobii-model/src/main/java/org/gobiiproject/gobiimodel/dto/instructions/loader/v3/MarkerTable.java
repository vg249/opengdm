package org.gobiiproject.gobiimodel.dto.instructions.loader.v3;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MarkerTable implements Table {

    @JsonProperty("platform_id")
    @NotNull(message = "platform id cannot be null")
    private String platformId;

    private ColumnAspect name;

    private ColumnAspect ref;

    private ColumnAspect alt;

    private ColumnAspect sequence;

    @JsonProperty("strand_name")
    private ColumnAspect starndName;

    private JsonAspect props;

    public String getPlatformId() {
        return platformId;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }

    public ColumnAspect getName() {
        return name;
    }

    public void setName(ColumnAspect name) {
        this.name = name;
    }

    public ColumnAspect getRef() {
        return ref;
    }

    public void setRef(ColumnAspect ref) {
        this.ref = ref;
    }

    public ColumnAspect getAlt() {
        return alt;
    }

    public void setAlt(ColumnAspect alt) {
        this.alt = alt;
    }

    public ColumnAspect getSequence() {
        return sequence;
    }

    public void setSequence(ColumnAspect sequence) {
        this.sequence = sequence;
    }

    public ColumnAspect getStarndName() {
        return starndName;
    }

    public void setStarndName(ColumnAspect starndName) {
        this.starndName = starndName;
    }

    public JsonAspect getProps() {
        return props;
    }

    public void setProps(JsonAspect props) {
        this.props = props;
    }
}
