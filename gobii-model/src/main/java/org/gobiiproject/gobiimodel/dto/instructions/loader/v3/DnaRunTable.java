package org.gobiiproject.gobiimodel.dto.instructions.loader.v3;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.gobiiproject.gobiimodel.dto.annotations.GobiiAspectTable;

import javax.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
@GobiiAspectTable(name = "dnarun")
public class DnaRunTable implements Table {

    @JsonProperty("project_id")
    private String projectId;

    @JsonProperty("experiment_id")
    private String experimentId;

    @JsonProperty("experiment_name")
    private CoordinatesAspect experimentName;

    @JsonProperty("status")
    private String status;

    @JsonProperty("dnasample_name")
    private CoordinatesAspect dnaSampleName;

    @JsonProperty("num")
    private CoordinatesAspect dnaSampleNum;

    @JsonProperty("name")
    private CoordinatesAspect dnaRunName;

    @JsonProperty("props")
    private JsonAspect dnaRunProperties;

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getExperimentId() {
        return experimentId;
    }

    public void setExperimentId(String experimentId) {
        this.experimentId = experimentId;
    }

    public CoordinatesAspect getExperimentName() {
        return experimentName;
    }

    public void setExperimentName(CoordinatesAspect experimentName) {
        this.experimentName = experimentName;
    }

    public JsonAspect getDnaRunProperties() {
        return dnaRunProperties;
    }

    public void setDnaRunProperties(JsonAspect dnaRunProperties) {
        this.dnaRunProperties = dnaRunProperties;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public CoordinatesAspect getDnaSampleName() {
        return dnaSampleName;
    }

    public void setDnaSampleName(CoordinatesAspect dnaSampleName) {
        this.dnaSampleName = dnaSampleName;
    }

    public CoordinatesAspect getDnaSampleNum() {
        return dnaSampleNum;
    }

    public void setDnaSampleNum(CoordinatesAspect dnaSampleNum) {
        this.dnaSampleNum = dnaSampleNum;
    }

    public CoordinatesAspect getDnaRunName() {
        return dnaRunName;
    }

    public void setDnaRunName(CoordinatesAspect dnaRunName) {
        this.dnaRunName = dnaRunName;
    }
}
