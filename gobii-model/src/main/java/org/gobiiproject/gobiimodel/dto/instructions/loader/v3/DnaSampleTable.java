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
    private CoordinatesAspect dnaSampleName;

    @JsonProperty("num")
    private CoordinatesAspect dnaSampleNum;

    @JsonProperty("external_code")
    private CoordinatesAspect germplasmExternalCode;

    @JsonProperty("platename")
    private CoordinatesAspect dnaSamplePlateName;

    @JsonProperty("well_row")
    private CoordinatesAspect dnaSampleWellRow;

    @JsonProperty("well_col")
    private CoordinatesAspect dnaSampleWellCol;

    @JsonProperty("uuid")
    private CoordinatesAspect dnaSampleUuid;

    @JsonProperty("props")
    private JsonAspect dnaSampleProperties;

    private String status;

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
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

    public CoordinatesAspect getGermplasmExternalCode() {
        return germplasmExternalCode;
    }

    public void setGermplasmExternalCode(CoordinatesAspect germplasmExternalCode) {
        this.germplasmExternalCode = germplasmExternalCode;
    }

    public CoordinatesAspect getDnaSamplePlateName() {
        return dnaSamplePlateName;
    }

    public void setDnaSamplePlateName(CoordinatesAspect dnaSamplePlateName) {
        this.dnaSamplePlateName = dnaSamplePlateName;
    }

    public CoordinatesAspect getDnaSampleWellRow() {
        return dnaSampleWellRow;
    }

    public void setDnaSampleWellRow(CoordinatesAspect dnaSampleWellRow) {
        this.dnaSampleWellRow = dnaSampleWellRow;
    }

    public CoordinatesAspect getDnaSampleWellCol() {
        return dnaSampleWellCol;
    }

    public void setDnaSampleWellCol(CoordinatesAspect dnaSampleWellCol) {
        this.dnaSampleWellCol = dnaSampleWellCol;
    }

    public CoordinatesAspect getDnaSampleUuid() {
        return dnaSampleUuid;
    }

    public void setDnaSampleUuid(CoordinatesAspect dnaSampleUuid) {
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
