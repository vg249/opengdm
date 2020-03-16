/**
 * CvPropertyDTO.java
 * 
 * Replacement for EntityPropertyDTO minus the entityIdId attribute
 * @author Rodolfo N. Duldulao, Jr.
 * @since 2020-03-11
 */
package org.gobiiproject.gobiimodel.dto.children;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import org.gobiiproject.gobiimodel.dto.annotations.GobiiEntityMap;
import org.gobiiproject.gobiimodel.entity.Cv;

@JsonIgnoreProperties({"propertyGroupType"})
public class CvPropertyDTO {
    private final static String PROPERTY_TYPE_SYSTEM = "system";
    private final static String PROPERTY_TYPE_CUSTOM = "user defined";

    @GobiiEntityMap(paramName="cvId", entity = Cv.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private Integer propertyId = null;

    @GobiiEntityMap(paramName="term", entity = Cv.class)
    private String propertyName = null;

    //this is from the props column of Project
    private String propertyValue = null;

    @GobiiEntityMap(paramName="cvGroup.cvGroupId", entity = Cv.class, deep = true)
    @JsonSerialize(using = ToStringSerializer.class)
    private Integer propertyGroupId;

    @GobiiEntityMap(paramName="cvGroup.cvGroupName", entity = Cv.class, deep = true)
    private String propertyGroupName;

    @GobiiEntityMap(paramName="cvGroup.cvGroupType", entity = Cv.class, deep = true)
    private Integer propertyGroupType;

    private String propertyType; //alias

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
        return this.propertyType;
    }

    public void setPropertyType(Integer propertyType) {
        if (propertyType == 1) this.propertyType =  PROPERTY_TYPE_SYSTEM;
        else if (propertyType == 2) this.propertyType =  PROPERTY_TYPE_CUSTOM;
    }

    public Integer getPropertyGroupType() {
        return this.propertyGroupType;
    }
    public void setPropertyGroupType(Integer propertyType) {
        this.propertyGroupType = propertyType;
        this.setPropertyType(propertyType);
    }

}
