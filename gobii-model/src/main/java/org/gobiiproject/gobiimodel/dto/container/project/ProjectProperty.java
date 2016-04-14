package org.gobiiproject.gobiimodel.dto.container.project;

/**
 * Created by Phil on 4/14/2016.
 */
public class ProjectProperty {

    private  Integer propertyId = null;
    private String propertyName = null;
    private String propertyValue = null;

    public ProjectProperty(){

    }

    public ProjectProperty(Integer propertyId, String propertyName, String propertyValue) {
        this.propertyId = propertyId;
        this.propertyName = propertyName;
        this.propertyValue = propertyValue;
    }

    public Integer getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Integer propertyId) {
        this.propertyId = propertyId;
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
