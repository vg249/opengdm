package org.gobiiproject.gobiimodel.dto.gdmv3;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

public class MarkerUploadRequestDTO {

    @JsonSerialize(using = ToStringSerializer.class)
    private Integer mapId;

    @JsonSerialize(using = ToStringSerializer.class)
    private Integer platformId;

    @JsonSerialize(using = ToStringSerializer.class)
    private Integer markerTemplateId;

    public Integer getMapId() {
        return mapId;
    }

    public void setMapId(Integer mapId) {
        this.mapId = mapId;
    }

    public Integer getPlatformId() {
        return platformId;
    }

    public void setPlatformId(Integer platformId) {
        this.platformId = platformId;
    }

    public Integer getMarkerTemplateId() {
        return markerTemplateId;
    }

    public void setMarkerTemplateId(Integer markerTemplateId) {
        this.markerTemplateId = markerTemplateId;
    }
}
