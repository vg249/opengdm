package org.gobiiproject.gobiimodel.dto.entity.noaudit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.gobiiproject.gobiimodel.dto.base.DTOBase;
import org.gobiiproject.gobiimodel.dto.entity.annotations.GobiiEntityColumn;
import org.gobiiproject.gobiimodel.dto.entity.annotations.GobiiEntityParam;

import java.util.Map;


@JsonIgnoreProperties(ignoreUnknown = true, value={
        "id", "allowedProcessTypes"
})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenotypeCallsDTO extends DTOBase{

    private Integer variantSetDbId;

    private Integer callSetDbId;

    private String callSetName;

    private Integer variantDbId;

    private String variantName;

    private Map<String, String> genotype;

    private String genotypeLiklihood;

    private String phaseSet;

    private String hdf5MarkerIdx;

    private String hdf5SampleIdx;

    @Override
    public Integer getId() { return null; }

    @Override
    public void setId(Integer id) { this.callSetDbId = null; }

    public Integer getVariantSetDbId() { return this.variantSetDbId; }

    @GobiiEntityColumn(columnName = "dataset_id")
    public void setVariantSetDbId(Integer variantSetDbId) { this.variantSetDbId = variantSetDbId; }

    public Integer getCallSetDbId() { return this.callSetDbId; }

    @GobiiEntityColumn(columnName = "dnarun_id")
    public void setCallSetDbId(Integer id) { this.callSetDbId = id; }

    public String getCallSetName() { return this.callSetName; }

    @GobiiEntityColumn(columnName = "dnarun_name")
    public void setCallSetName(String callSetName) { this.callSetName = callSetName; }

    public Integer getVariantDbId() { return this.variantDbId; }

    @GobiiEntityColumn(columnName = "marker_id")
    public void setVariantDbId(Integer variantDbId) { this.variantDbId = variantDbId; }

    public String getVariantName() { return this.variantName; }

    @GobiiEntityColumn(columnName = "marker_name")
    public void setVariantName(String variantName) { this.variantName = variantName; }

    public Map<String, String> getGenotype() { return this.genotype; }

    public void setGenotype(Map<String, String> genotype) { this.genotype = genotype; }

    public String getGenotypeLiklihood() { return this.genotypeLiklihood; }

    public void setGenotypeLiklihood(String genotypeLiklihood) { this.genotypeLiklihood = genotypeLiklihood; }

    public String getPhaseSet() { return this.phaseSet; }

    public void setPhaseSet(String phaseSet) { this.phaseSet = phaseSet; }

    public String getHdf5MarkerIdx() {
        return this.hdf5MarkerIdx;
    }

    @GobiiEntityColumn(columnName = "hdf5_marker_idx")
    public void setHdf5MarkerIdx(String hdf5MarkerIdx) {
        this.hdf5MarkerIdx = hdf5MarkerIdx;
    }

    public String getHdf5SampleIdx() {
        return this.hdf5SampleIdx;
    }

    @GobiiEntityColumn(columnName = "hdf5_dnarun_idx")
    public void setHdf5SampleIdx(String hdf5SampleIdx) {
        this.hdf5SampleIdx = hdf5SampleIdx;
    }
}
