package org.gobiiproject.gobiimodel.dto.entity.noaudit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.gobiiproject.gobiimodel.dto.base.DTOBase;
import org.gobiiproject.gobiimodel.dto.entity.annotations.GobiiEntityColumn;

import java.util.Map;


@JsonIgnoreProperties(ignoreUnknown = true, value={
        "id", "allowedProcessTypes"
})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenotypeCallsMarkerMetadataDTO extends DTOBase{

    private Integer markerId;

    private String markerName;


    private String hdf5MarkerIdx;


    @Override
    public Integer getId() { return null; }

    @Override
    public void setId(Integer id) { this.markerId = null; }

    public Integer getMarkerId() { return this.markerId; }

    @GobiiEntityColumn(columnName = "marker_id")
    public void setCallSetDbId(Integer id) { this.markerId = id; }

    public String getMarkerName() { return this.markerName; }

    @GobiiEntityColumn(columnName = "marker_name")
    public void setMarkerName(String markerName) { this.markerName = markerName; }


    public String getHdf5MarkerIdx() {
        return this.hdf5MarkerIdx;
    }

    @GobiiEntityColumn(columnName = "hdf5_marker_idx")
    public void setHdf5MarkerIdx(String hdf5MarkerIdx) {
        this.hdf5MarkerIdx = hdf5MarkerIdx;
    }

}
