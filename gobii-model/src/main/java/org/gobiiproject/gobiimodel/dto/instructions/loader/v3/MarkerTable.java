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
    private Aspect markerName;

    @JsonProperty("platform_name")
    private Aspect platformName;

    @JsonProperty("ref")
    private Aspect markerRef;

    @JsonProperty("alt")
    private Aspect markerAlt;

    @JsonProperty("sequence")
    private Aspect markerSequence;

    @JsonProperty("strand_name")
    private Aspect markerStrandName;

    @JsonProperty("props")
    private JsonAspect markerProperties;

    public String getPlatformId() {
        return platformId;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public Aspect getMarkerRef() {
        return markerRef;
    }

    public void setMarkerRef(Aspect markerRef) {
        this.markerRef = markerRef;
    }

    public Aspect getMarkerAlt() {
        return markerAlt;
    }

    public void setMarkerAlt(Aspect markerAlt) {
        this.markerAlt = markerAlt;
    }

    public Aspect getMarkerSequence() {
        return markerSequence;
    }

    public void setMarkerSequence(Aspect markerSequence) {
        this.markerSequence = markerSequence;
    }

    public Aspect getMarkerStrandName() {
        return markerStrandName;
    }

    public void setMarkerStrandName(Aspect markerStrandName) {
        this.markerStrandName = markerStrandName;
    }

    public JsonAspect getMarkerProperties() {
        return markerProperties;
    }

    public void setMarkerProperties(JsonAspect markerProperties) {
        this.markerProperties = markerProperties;
    }
}
