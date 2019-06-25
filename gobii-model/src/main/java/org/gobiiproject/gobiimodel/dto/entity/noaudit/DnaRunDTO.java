package org.gobiiproject.gobiimodel.dto.entity.noaudit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.gobiiproject.gobiimodel.dto.base.DTOBase;
import org.gobiiproject.gobiimodel.dto.entity.annotations.GobiiEntityColumn;
import org.gobiiproject.gobiimodel.dto.entity.annotations.GobiiEntityParam;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by VCalaminos on 6/25/2019.
 */

@JsonIgnoreProperties(ignoreUnknown = true, value={
        "id", "allowedProcessTypes", "entityNameType"
})
public class DnaRunDTO extends DTOBase{

    private int callSetDbId;
    private Integer experimentId;
    private Integer sampleDbId;
    private String callSetName;
    private String dnaRunCode;
    private List<Integer> datasetIds = new ArrayList<>();

    @Override
    public Integer getId() { return this.callSetDbId; }

    @Override
    public void setId(Integer id) { this.callSetDbId = id; }

    @GobiiEntityParam(paramName = "callSetDbId")
    public Integer getCallSetDbId() { return this.callSetDbId; }

    @GobiiEntityColumn(columnName = "dnarun_id")
    public void setCallSetDbId(Integer id) { this.callSetDbId = id; }

    @GobiiEntityParam(paramName = "experimentId")
    public Integer getExperimentId() { return this.experimentId; }

    @GobiiEntityColumn(columnName = "experiment_id")
    public void setExperimentId(Integer experimentId) { this.experimentId = experimentId; }

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

    @GobiiEntityParam(paramName = "datasetIds")
    public List<Integer> getDatasetIds() { return this.datasetIds; }

    @GobiiEntityColumn(columnName = "dataset_ids")
    public void setDatasetIds(List<Integer> datasetIds) { this.datasetIds = datasetIds; }

}
