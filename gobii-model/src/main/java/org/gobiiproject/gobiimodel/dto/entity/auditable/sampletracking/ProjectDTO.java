
package org.gobiiproject.gobiimodel.dto.entity.auditable.sampletracking;


import com.fasterxml.jackson.annotation.*;
import io.swagger.annotations.ApiModel;
import org.gobiiproject.gobiimodel.dto.base.DTOBaseAuditable;
import org.gobiiproject.gobiimodel.dto.entity.annotations.GobiiEntityColumn;
import org.gobiiproject.gobiimodel.dto.entity.annotations.GobiiEntityParam;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

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

    //System Properties
    private Map<String, String> properties = new HashMap<>();

    //private String division;
    //private String studyName;
    //private String genotypingPurpose;
    //private String dateSampled;

    //Additional Properties
    //private Map<String, String> additionalProperties = new HashMap<>();

    private SimpleDateFormat dateStringFormatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss z");

    public ProjectDTO() {
        super(GobiiEntityNameType.PROJECT);
        dateStringFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
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
    public void setProjectName(String projectName) {
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

    public Map<String, String> getProperties() {
        return this.properties;
    }

    @GobiiEntityColumn(columnName = "system_properties")
    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    //public String getDivision() {
    //    return this.systemProperties.getOrDefault("division", null);
    //}
    //public void setDivision(String division) {
    //    this.systemProperties.put("division", division);
    //}
    //public String getStudyName() {
    //   return this.systemProperties.getOrDefault("study_name", null);
    //}
    //public void setStudyName(String studyName) {
    //   this.systemProperties.put("study_name", studyName);
    //}
    //public String getGenotypingPurpose() {
    //   return this.systemProperties.getOrDefault("genotyping_purpose", null);
    //}
    //public void setGenotypingPurpose(String genotypingPurpose) {
    //    this.systemProperties.put("genotyping_purpose", genotypingPurpose);
    //}
    //public String getDateSampled() {
    //    return this.systemProperties.getOrDefault("date_sampled", null);
    //}
    //public void setDateSampled(String dateSampled) {
    // this.systemProperties.put("date_sampled", dateSampled);
    // }

}
