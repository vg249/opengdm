/**
 * CvPropertyDTO.java
 * 
 * Replacement for EntityPropertyDTO minus the entityIdId attribute
 * @author Rodolfo N. Duldulao, Jr.
 * @since 2020-03-11
 */
package org.gobiiproject.gobiimodel.dto.children;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;


public class CvPropertyDTO {
    private final static String PROPERTY_TYPE_SYSTEM = "system";
    private final static String PROPERTY_TYPE_CUSTOM = "user defined";

    @JsonSerialize(using = ToStringSerializer.class)
    private Integer propertyId = null;
    private String propertyName = null;
    private String propertyValue = null;

    @JsonSerialize(using = ToStringSerializer.class)
    private Integer propertyGroupId;
    private String propertyGroupName;
    private String propertyType;

    public CvPropertyDTO() {

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

    public Integer getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Integer propertyId) {
        this.propertyId = propertyId;
    }

    public Integer getPropertyGroupId() {
        return propertyGroupId;
    }

    public void setPropertyGroupId(Integer propertyGroupId) {
        this.propertyGroupId = propertyGroupId;
    }

    public String getPropertyGroupName() {
        return propertyGroupName;
    }

    public void setPropertyGroupName(String propertyGroupName) {
        this.propertyGroupName = propertyGroupName;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public void setPropertyType(Integer propertyType) {
        if (propertyType == 1) this.propertyType = PROPERTY_TYPE_SYSTEM;
        else if (propertyType == 2) this.propertyType = PROPERTY_TYPE_CUSTOM;
    }

}
