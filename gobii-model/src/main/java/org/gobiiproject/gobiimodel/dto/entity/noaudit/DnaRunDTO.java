package org.gobiiproject.gobiimodel.dto.entity.noaudit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.gobiiproject.gobiimodel.dto.base.DTOBase;
import org.gobiiproject.gobiimodel.dto.entity.annotations.GobiiEntityColumn;
import org.gobiiproject.gobiimodel.dto.entity.annotations.GobiiEntityParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by VCalaminos on 6/25/2019.
 */

@JsonIgnoreProperties(ignoreUnknown = true, value={
        "id", "allowedProcessTypes", "entityNameType",
        "datasetDnarunIndex"
})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DnaRunDTO extends DTOBase{

    private int callSetDbId;
    private Integer studyDbId;
    private Integer sampleDbId;
    private String callSetName;
    private String dnaRunCode;
    private List<Integer> variantSetIds = new ArrayList<>();
    private Integer germplasmDbId;
    private String germplasmName;
    private String germplasmExternalCode;
    private String sampleName;
    private Map<String, Object> datasetDnarunIndex;

    @Override
    public Integer getId() { return this.callSetDbId; }

    @Override
    public void setId(Integer id) { this.callSetDbId = id; }

    @GobiiEntityParam(paramName = "callSetDbId")
    public Integer getCallSetDbId() { return this.callSetDbId; }

    @GobiiEntityColumn(columnName = "dnarun_id")
    public void setCallSetDbId(Integer id) { this.callSetDbId = id; }

    @GobiiEntityParam(paramName = "studyDbId")
    public Integer getStudyDbId() { return this.studyDbId; }

    @GobiiEntityColumn(columnName = "experiment_id")
    public void setStudyDbId(Integer studyDbId) { this.studyDbId = studyDbId; }

    @GobiiEntityParam(paramName = "sampleDbId")
    public Integer getSampleDbId() { return this.sampleDbId; }

    @GobiiEntityColumn(columnName = "dnasample_id")
    public void setSampleDbId(Integer sampleDbId) { this.sampleDbId = sampleDbId; }

    @GobiiEntityParam(paramName = "callSetName")
    public String getCallSetName() { return this.callSetName; }

    @GobiiEntityColumn(columnName = "name")
    public void setCallSetName(String callSetName) { this.callSetName = callSetName; }

    @GobiiEntityParam(paramName = "dnaRunCode")
    public String getDnaRunCode() { return this.dnaRunCode; }

    @GobiiEntityColumn(columnName = "code")
    public void setDnaRunCode(String dnaRunCode) { this.dnaRunCode = dnaRunCode; }

    @GobiiEntityParam(paramName = "variantSetIds")
    public List<Integer> getVariantSetIds() { return this.variantSetIds; }

    public void setVariantSetIds(List<Integer> variantSetIds) { this.variantSetIds = variantSetIds; }

    @GobiiEntityParam(paramName = "germplasmDbId")
    public Integer getGermplasmDbId() { return this.germplasmDbId; }

    @GobiiEntityColumn(columnName = "germplasm_id")
    public void setGermplasmDbId(Integer germplasmDbId) { this.germplasmDbId = germplasmDbId; }

    @GobiiEntityParam(paramName = "germplasmName")
    public String getGermplasmName() { return this.germplasmName; }

    @GobiiEntityColumn(columnName = "germplasm_name")
    public void setGermplasmName(String germplasmName) { this.germplasmName = germplasmName; }

    @GobiiEntityParam(paramName = "germplasmExternalCode")
    public String getGermplasmExternalCode() { return this.germplasmExternalCode; }

    @GobiiEntityColumn(columnName = "germplasm_external_code")
    public void setGermplasmExternalCode(String germplasmExternalCode) { this.germplasmExternalCode = germplasmExternalCode; }

    @GobiiEntityParam(paramName = "sampleName")
    public String getSampleName() { return this.sampleName; }

    @GobiiEntityColumn(columnName = "sample_name")
    public void setSampleName(String sampleName) { this.sampleName = sampleName; }

    @GobiiEntityColumn(columnName = "dataset_dnarun_idx")
    public void setDatasetDnarunIndex(Map<String, Object> datasetDnarunIndex) {
        this.datasetDnarunIndex = datasetDnarunIndex;
    }

    public Map<String, Object> getDatasetDnarunIndex() {
        return this.datasetDnarunIndex;
    }

}
