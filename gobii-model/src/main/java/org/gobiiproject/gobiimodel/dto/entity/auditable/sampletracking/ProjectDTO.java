
package org.gobiiproject.gobiimodel.dto.entity.auditable.sampletracking;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.JsonObject;
import org.gobiiproject.gobiimodel.dto.base.DTOBaseAuditable;
import org.gobiiproject.gobiimodel.dto.entity.annotations.GobiiEntityColumn;
import org.gobiiproject.gobiimodel.dto.entity.annotations.GobiiEntityParam;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true, value={
        "allowedProcessTypes", "entityNameType", "properties"
})
public class ProjectDTO extends DTOBaseAuditable {

    private int id;
    private Integer piContactId;
    private String name;
    private Integer status;
    private String code;
    private String description;
    private Map<String, Object> properties = new HashMap<>();

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
    public Map<String, Object> getProperties() {
        return this.properties;
    }

    @GobiiEntityColumn(columnName = "mapped_properties")
    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    public Object getGenotypingPurpose() {
        return this.properties.getOrDefault("genotyping_purpose", null);
    }

    public Object getStudyName() {
        return this.properties.getOrDefault("study_name", null);
    }

    public Object getDivision() {
        return this.properties.getOrDefault("division", null);
    }

    public Object getDateSampled() {
        return this.properties.getOrDefault("date_sampled", null);
    }

    public void setGenotypingPurpose(String genotypingPurpose) {
        this.properties.put("genotyping_purpose", genotypingPurpose);
    }

    public void setStudyName(String studyName) {
        this.properties.put("study_name", studyName);
    }

    public void setDivision(String division) {
        this.properties.put("division", division);
    }

    public void setDateSampled(String dateSampled) {
        this.properties.put("date_sampled", dateSampled);
    }

}
