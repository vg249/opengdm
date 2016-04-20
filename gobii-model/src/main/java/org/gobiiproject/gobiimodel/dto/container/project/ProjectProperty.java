package org.gobiiproject.gobiimodel.dto.container.project;

/**
 * Created by Phil on 4/14/2016.
 */
public class ProjectProperty {

    private Integer projectId = null;
    private String propertyName = null;
    private String propertyValue = null;

    public ProjectProperty() {

    }

    public ProjectProperty(Integer projectId,
                           String propertyName,
                           String propertyValue) {
        this.projectId = projectId;
        this.propertyName = propertyName;
        this.propertyValue = propertyValue;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(String propertyValue) {
        this.propertyValue = propertyValue;
    }

}
