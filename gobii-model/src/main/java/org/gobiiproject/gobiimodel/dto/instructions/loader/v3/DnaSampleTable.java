package org.gobiiproject.gobiimodel.dto.instructions.loader.v3;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.gobiiproject.gobiimodel.dto.annotations.GobiiAspectTable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@GobiiAspectTable(name = "dnasample")
public class DnaSampleTable implements Table {

    @JsonProperty("project_id")
    private String projectId;

    @JsonProperty("name")
    private Aspect dnaSampleName;

    @JsonProperty("num")
    private Aspect dnaSampleNum;

    @JsonProperty("external_code")
    private Aspect germplasmExternalCode;

    @JsonProperty("platename")
    private Aspect dnaSamplePlateName;

    @JsonProperty("well_row")
    private Aspect dnaSampleWellRow;

    @JsonProperty("well_col")
    private Aspect dnaSampleWellCol;

    @JsonProperty("uuid")
    private Aspect dnaSampleUuid;

    @JsonProperty("props")
    private JsonAspect dnaSampleProperties;

    private String status;

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
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

    public Aspect getGermplasmExternalCode() {
        return germplasmExternalCode;
    }

    public void setGermplasmExternalCode(Aspect germplasmExternalCode) {
        this.germplasmExternalCode = germplasmExternalCode;
    }

    public Aspect getDnaSamplePlateName() {
        return dnaSamplePlateName;
    }

    public void setDnaSamplePlateName(Aspect dnaSamplePlateName) {
        this.dnaSamplePlateName = dnaSamplePlateName;
    }

    public Aspect getDnaSampleWellRow() {
        return dnaSampleWellRow;
    }

    public void setDnaSampleWellRow(Aspect dnaSampleWellRow) {
        this.dnaSampleWellRow = dnaSampleWellRow;
    }

    public Aspect getDnaSampleWellCol() {
        return dnaSampleWellCol;
    }

    public void setDnaSampleWellCol(Aspect dnaSampleWellCol) {
        this.dnaSampleWellCol = dnaSampleWellCol;
    }

    public Aspect getDnaSampleUuid() {
        return dnaSampleUuid;
    }

    public void setDnaSampleUuid(Aspect dnaSampleUuid) {
        this.dnaSampleUuid = dnaSampleUuid;
    }

    public JsonAspect getDnaSampleProperties() {
        return dnaSampleProperties;
    }

    public void setDnaSampleProperties(JsonAspect dnaSampleProperties) {
        this.dnaSampleProperties = dnaSampleProperties;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
