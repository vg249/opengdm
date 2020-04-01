/**
 * CvPropertyDTO.java
 * 
 * Replacement for EntityPropertyDTO minus the entityIdId attribute
 * @author Rodolfo N. Duldulao, Jr.
 * @since 2020-03-11
 */
package org.gobiiproject.gobiimodel.dto.children;

import javax.validation.constraints.Positive;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import org.gobiiproject.gobiimodel.dto.annotations.GobiiEntityMap;
import org.gobiiproject.gobiimodel.entity.Cv;

import lombok.Data;

@JsonIgnoreProperties({"propertyGroupType"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class CvPropertyDTO {
    private final static String PROPERTY_TYPE_SYSTEM = "system defined";
    private final static String PROPERTY_TYPE_CUSTOM = "user defined";

    @GobiiEntityMap(paramName="cvId", entity = Cv.class)
    @JsonSerialize(using = ToStringSerializer.class)
    @Positive
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

    public CvPropertyDTO() {
    }

    @JsonProperty("propertyType")
    public String getPropertyType() {
        if (this.propertyGroupType == 1) return PROPERTY_TYPE_SYSTEM;
        else if (this.propertyGroupType == 2) return PROPERTY_TYPE_CUSTOM;
        return null;
    }

}
