
package org.gobiiproject.gobiimodel.dto.entity.auditable.sampletracking;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.JsonObject;
import org.gobiiproject.gobiimodel.dto.base.DTOBaseAuditable;
import org.gobiiproject.gobiimodel.dto.entity.annotations.GobiiEntityColumn;
import org.gobiiproject.gobiimodel.dto.entity.annotations.GobiiEntityParam;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;

import java.util.HashMap;
import java.util.Map;

@JsonPropertyOrder({"id", "name", "piContactId", "description"})
@JsonIgnoreProperties(ignoreUnknown = true, value={
        "allowedProcessTypes", "entityNameType",
        "getPropertiesAsString"
})
public class ProjectDTO extends DTOBaseAuditable {

    private int id;
    private Integer piContactId;
    private String name;
    private Integer status;
    private String code;
    private String description;
    private JsonNode properties;
    private String genotypingPurpose;


    public ProjectDTO() {
        super(GobiiEntityNameType.PROJECT);
    }

    @Override
    @GobiiEntityParam(paramName = "projectId")
    public Integer getId() {
        return this.id;
    }

    @Override
    @GobiiEntityColumn(columnName = "project_id")
    public void setId(Integer id) {
        this.id = id;
    }

    @GobiiEntityParam(paramName = "piContact")
    public Integer getPiContactId() {
        return this.piContactId;
    }

    @GobiiEntityColumn(columnName = "pi_contact")
    public void setPiContactId(Integer piContactId) {
        this.piContactId = piContactId;
    }

    @GobiiEntityParam(paramName = "projectName")
    public String getName() {
        return this.name;
    }

    @GobiiEntityColumn(columnName = "name")
    public void setName(String name) {
        this.name = name;
    }

    @GobiiEntityParam(paramName = "projectStatus")
    public Integer getStatus() {
        return this.status;
    }

    @GobiiEntityColumn(columnName = "status")
    public void setStatus(Integer status) {
        this.status = status;
    }

    @GobiiEntityParam(paramName = "projectCode")
    public String getCode() {
        return this.code;
    }

    @GobiiEntityColumn(columnName = "code")
    public void setCode(String code) {
        this.code = code;
    }

    @GobiiEntityParam(paramName = "projectDescription")
    public String getDescription() {
        return this.description;
    }

    @GobiiEntityColumn(columnName = "description")
    public void setDescription(String description) {
        this.description = description;
    }

    @GobiiEntityParam(paramName = "projectProperties")
    public String getPropertiesAsString() {
        return this.properties.toString();
    }

    public JsonNode getProperties() {
        return this.properties;
    }

    @GobiiEntityColumn(columnName = "props")
    public void setProperties(JsonNode properties) {
        this.properties = properties;
    }

    public String getGenotypingPurpose() {
        if(this.properties.has("genotyping_purpose")) {
            return this.properties.get("genotyping_purpose").toString();
        }
        else {
            return null;
        }
    }
}
