package org.gobiiproject.gobiimodel.dto.entity.noaudit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.gobiiproject.gobiimodel.dto.base.DTOBase;
import org.gobiiproject.gobiimodel.dto.entity.annotations.GobiiEntityColumn;


@JsonIgnoreProperties(ignoreUnknown = true, value={
        "id", "allowedProcessTypes"
})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenotypeCallsDnarunMetadataDTO extends DTOBase{

    private Integer dnarunId;

    private String dnarunName;


    private String hdf5DnarunIdx;


    @Override
    public Integer getId() { return null; }

    @Override
    public void setId(Integer id) { this.dnarunId = null; }

    public Integer getMarkerId() { return this.dnarunId; }

    @GobiiEntityColumn(columnName = "dnarun_id")
    public void setCallSetDbId(Integer id) { this.dnarunId = id; }

    public String getMarkerName() { return this.dnarunName; }

    @GobiiEntityColumn(columnName = "dnarun_name")
    public void setMarkerName(String dnarunName) { this.dnarunName = dnarunName; }


    public String getHdf5MarkerIdx() {
        return this.hdf5DnarunIdx;
    }

    @GobiiEntityColumn(columnName = "hdf5_dnarun_idx")
    public void setHdf5DnarunIdx(String hdf5DnarunIdx) {
        this.hdf5DnarunIdx = hdf5DnarunIdx;
    }

}
