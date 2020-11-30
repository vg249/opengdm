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
    private Aspect experimentName;

    @JsonProperty("status")
    private String status;

    @JsonProperty("dnasample_name")
    private Aspect dnaSampleName;

    @JsonProperty("num")
    private Aspect dnaSampleNum;

    @JsonProperty("name")
    private Aspect dnaRunName;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Aspect getDnaSampleName() {
        return dnaSampleName;
    }

    public void setDnaSampleName(Aspect dnaSampleName) {
        this.dnaSampleName = dnaSampleName;
    }

    public Aspect getDnaSampleNum() {
        return dnaSampleNum;
    }

    public void setDnaSampleNum(Aspect dnaSampleNum) {
        this.dnaSampleNum = dnaSampleNum;
    }

    public Aspect getDnaRunName() {
        return dnaRunName;
    }

    public void setDnaRunName(Aspect dnaRunName) {
        this.dnaRunName = dnaRunName;
    }
}
