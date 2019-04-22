
package org.gobiiproject.gobiimodel.dto.entity.auditable.sampletracking;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.JsonObject;
import org.gobiiproject.gobiimodel.dto.base.DTOBaseAuditable;
import org.gobiiproject.gobiimodel.dto.entity.annotations.GobiiEntityColumn;
import org.gobiiproject.gobiimodel.dto.entity.annotations.GobiiEntityParam;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true, value={
        "allowedProcessTypes", "entityNameType",
        "properties"
})
public class ProjectDTO extends DTOBaseAuditable {

    private int id;
    private Integer piContactId;
    private String name;
    private Integer status;
    private String code;
    private String description;
    private String selfLink;
    private JsonNode properties;

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
    public JsonNode getProperties() {
        return this.properties;
    }

    @GobiiEntityColumn(columnName = "mapped_properties")
    public void setProperties(JsonNode properties) {
        this.properties = properties;
    }

    public String getGenotypingPurpose() {
        if(this.properties.has("genotyping_purpose")) {
            return this.properties.get("genotyping_purpose").asText();
        }
        else {
            return null;
        }
    }

    public String getStudyName() {
        if(this.properties.has("study_name")) {
            return this.properties.get("study_name").asText();
        }
        else {
            return null;
        }
    }

    public String getDivision() {
        if(this.properties.has("division")) {
            return this.properties.get("division").asText();
        }
        else {
            return null;
        }
    }

    public String getDateSampled() {
        if(this.properties.has("date_sampled")) {
            return this.properties.get("date_sampled").asText();
        }
        else {
            return null;
        }
    }
}
