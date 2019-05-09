
package org.gobiiproject.gobiimodel.dto.entity.auditable.sampletracking;


import com.fasterxml.jackson.annotation.*;
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
        "id", "allowedProcessTypes", "entityNameType"
})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectDTO extends DTOBaseAuditable {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int projectId;
    private Integer piContactId;
    private String projectName;
    private Integer projectStatus;
    private String projectCode;
    private String projectDescription;
    private Map<String, Object> projectProperties = new HashMap<>();

    public ProjectDTO() {
        super(GobiiEntityNameType.PROJECT);
    }

    @Override
    public Integer getId() {
        return this.projectId;
    }

    @Override
    public void setId(Integer id) {
        this.projectId = id;
    }

    @GobiiEntityParam(paramName = "projectId")
    public Integer getProjectId() {
        return this.projectId;
    }

    @GobiiEntityColumn(columnName = "project_id")
    public void setProjectId(Integer id) {
        this.projectId = id;
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
    public String getProjectName() {
        return this.projectName;
    }

    @GobiiEntityColumn(columnName = "name")
    public void setName(String projectName) {
        this.projectName = projectName;
    }

    @GobiiEntityParam(paramName = "projectStatus")
    public Integer getProjectStatus() {
        return this.projectStatus;
    }

    @GobiiEntityColumn(columnName = "status")
    public void setProjectStatus(Integer status) {
        this.projectStatus = status;
    }

    @GobiiEntityParam(paramName = "projectCode")
    public String getProjectCode() {
        return this.projectCode;
    }

    @GobiiEntityColumn(columnName = "code")
    public void setProjectCode(String code) {
        this.projectCode = code;
    }

    @GobiiEntityParam(paramName = "projectDescription")
    public String getProjectDescription() {
        return this.projectDescription;
    }

    @GobiiEntityColumn(columnName = "description")
    public void setProjectDescription(String description) {
        this.projectDescription = description;
    }

    public Map<String, Object> getProjectProperties() {
        return this.projectProperties;
    }

    @GobiiEntityColumn(columnName = "mapped_properties")
    public void setProjectProperties(Map<String, Object> properties) {
        this.projectProperties = properties;
    }

}
