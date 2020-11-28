package org.gobiiproject.gobiimodel.dto.instructions.loader.v3;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.gobiiproject.gobiimodel.dto.annotations.GobiiAspectTable;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;

@JsonInclude(JsonInclude.Include.NON_NULL)
@GobiiAspectTable(name = "marker")
public class MarkerTable implements Table {

    @JsonProperty("platform_id")
    @NotNull(message = "platform id cannot be null")
    private String platformId;

    private String status;

    @JsonProperty("name")
    private ColumnAspect markerName;

    @JsonProperty("platform_name")
    private ColumnAspect platformName;

    @JsonProperty("ref")
    private ColumnAspect markerRef;

    @JsonProperty("alt")
    private ColumnAspect markerAlt;

    @JsonProperty("sequence")
    private ColumnAspect markerSequence;

    @JsonProperty("strand_name")
    private ColumnAspect markerStrandName;

    @JsonProperty("props")
    private JsonAspect markerProperties;

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

    public ColumnAspect getMarkerRef() {
        return markerRef;
    }

    public void setMarkerRef(ColumnAspect markerRef) {
        this.markerRef = markerRef;
    }

    public ColumnAspect getMarkerAlt() {
        return markerAlt;
    }

    public void setMarkerAlt(ColumnAspect markerAlt) {
        this.markerAlt = markerAlt;
    }

    public ColumnAspect getMarkerSequence() {
        return markerSequence;
    }

    public void setMarkerSequence(ColumnAspect markerSequence) {
        this.markerSequence = markerSequence;
    }

    public ColumnAspect getMarkerStrandName() {
        return markerStrandName;
    }

    public void setMarkerStrandName(ColumnAspect markerStrandName) {
        this.markerStrandName = markerStrandName;
    }

    public JsonAspect getMarkerProperties() {
        return markerProperties;
    }

    public void setMarkerProperties(JsonAspect markerProperties) {
        this.markerProperties = markerProperties;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ColumnAspect getPlatformName() {
        return platformName;
    }

    public void setPlatformName(ColumnAspect platformName) {
        this.platformName = platformName;
    }
}
