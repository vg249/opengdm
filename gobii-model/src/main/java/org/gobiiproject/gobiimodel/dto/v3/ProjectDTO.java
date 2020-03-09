/**
 * ProjectDTO.java
 * 
 * GOBII API Version 3 ProjectDTO
 * @author Rodolfo N. Duldulao, Jr. <rnduldulaojr@gmail.com>
 * @since 2020-03-06
 */

package org.gobiiproject.gobiimodel.dto.v3;

import java.util.ArrayList;
import java.util.List;

import org.gobiiproject.gobiimodel.dto.base.DTOBaseAuditable;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;

public class ProjectDTO extends DTOBaseAuditable {

    private Integer projectId = 0;
    private String piContactId;
    private String piContactName;
    private String projectName;
    private String projectDescription;

    private List<Object> properties = new ArrayList<>();

    private Integer experimentCount;
    private Integer datasetCount;
    private Integer markerCount;
    private Integer dnaRunCount;


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

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getPiContactId() {
        return piContactId;
    }

    public void setPiContactId(String piContactId) {
        this.piContactId = piContactId;
    }

    public String getPiContactName() {
        return piContactName;
    }

    public void setPiContactName(String piContactName) {
        this.piContactName = piContactName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public List<Object> getProperties() {
        return properties;
    }

    public void setProperties(List<Object> properties) {
        this.properties = properties;
    }

    public Integer getExperimentCount() {
        return experimentCount;
    }

    public void setExperimentCount(Integer experimentCount) {
        this.experimentCount = experimentCount;
    }

    public Integer getDatasetCount() {
        return datasetCount;
    }

    public void setDatasetCount(Integer datasetCount) {
        this.datasetCount = datasetCount;
    }

    public Integer getMarkerCount() {
        return markerCount;
    }

    public void setMarkerCount(Integer markerCount) {
        this.markerCount = markerCount;
    }

    public Integer getDnaRunCount() {
        return dnaRunCount;
    }

    public void setDnaRunCount(Integer dnaRunCount) {
        this.dnaRunCount = dnaRunCount;
    }
}

