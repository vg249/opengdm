/**
 * V3ProjectDTO.java
 * 
 * DTO for Project data (Gobii API V3)
 * 
 * @author Rodolfo N. Duldulao, Jr. <rnduldulaojr@gmail.com>
 * @since 2020-03-07
 */
package org.gobiiproject.gobiimodel.dto.auditable;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import org.gobiiproject.gobiimodel.dto.annotations.GobiiEntityColumn;
import org.gobiiproject.gobiimodel.dto.annotations.GobiiEntityMap;
import org.gobiiproject.gobiimodel.dto.annotations.GobiiEntityParam;
import org.gobiiproject.gobiimodel.dto.base.DTOBaseAuditable;
import org.gobiiproject.gobiimodel.dto.children.CvPropertyDTO;
import org.gobiiproject.gobiimodel.entity.Project;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;


/**
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = false, value={
    "id", "allowedProcessTypes", "entityNameType", "status"
})
@JsonInclude(JsonInclude.Include.ALWAYS)
public class GobiiProjectDTO extends DTOBaseAuditable {

   
    public GobiiProjectDTO() {
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


    // we are waiting until we a have a view to return
    // properties for that property: we don't know how to represent them yet
    @GobiiEntityMap(paramName = "projectId", entity = Project.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private Integer projectId = 0;

    @GobiiEntityMap(paramName="projectName", entity = Project.class)
    private String projectName;

    @GobiiEntityMap(paramName="projectDescription", entity = Project.class)
    private String projectDescription;

    @GobiiEntityMap(paramName="contact.contactId", entity = Project.class, deep=true)
    private Integer piContactId;
    @GobiiEntityMap(paramName="contact.username", entity = Project.class, deep=true)
    private String piContactName;

    //TODO: when the stats table is done
    private Integer experimentCount;
    private Integer datasetCount;
    private Integer markersCount;
    private Integer dnaRunsCount;


    private List<CvPropertyDTO> properties = new java.util.ArrayList<>();

    @GobiiEntityParam(paramName = "projectId")
    public Integer getProjectId() {
        return projectId;
    }

    @GobiiEntityColumn(columnName = "project_id")
    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    @GobiiEntityParam(paramName = "projectName")
    public String getProjectName() {
        return projectName;
    }

    @GobiiEntityColumn(columnName = "name")
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    @GobiiEntityParam(paramName = "projectDescription")
    public String getProjectDescription() {
        return projectDescription;
    }

    @GobiiEntityColumn(columnName = "description")
    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    @GobiiEntityParam(paramName = "piContactId")
    public Integer getPiContactId() {
        return piContactId;
    }

    @GobiiEntityColumn(columnName = "pi_contact")
    public void setPiContactId(Integer piContactId) {
        this.piContactId = piContactId;
    }

    @GobiiEntityParam(paramName="piContactName")
    public String getPiContactName() {
        return piContactName;
    }

    public void setPiContactName(String piContactName) {
        this.piContactName = piContactName;
    }

    @GobiiEntityParam(paramName="experimentCount")
    public Integer getExperimentCount() {
        return experimentCount;
    }

    public void setExperimentCount(Integer experimentCount) {
        this.experimentCount = experimentCount;
    }

    @GobiiEntityParam(paramName="datasetCount")
    public Integer getDatasetCount() {
        return datasetCount;
    }

    public void setDatasetCount(Integer datasetCount) {
        this.datasetCount = datasetCount;
    }

    @GobiiEntityParam(paramName="markersCount")
    public Integer getMarkersCount() {
        return markersCount;
    }

    public void setMarkersCount(Integer markersCount) {
        this.markersCount = markersCount;
    }


    @GobiiEntityParam(paramName="dnaRunsCount")
    public Integer getDnaRunsCount() {
        return dnaRunsCount;
    }

    public void setDnaRunsCount(Integer dnaRunsCount) {
        this.dnaRunsCount = dnaRunsCount;
    }

    public List<CvPropertyDTO> getProperties() {
        return properties;
    }

    public void setProperties(List<CvPropertyDTO> properties) {
        this.properties = properties;
    }
}
