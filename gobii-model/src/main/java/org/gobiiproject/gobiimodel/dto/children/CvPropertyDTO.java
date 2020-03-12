/**
 * CvPropertyDTO.java
 * 
 * Replacement for EntityPropertyDTO minus the entityIdId attribute
 * @author Rodolfo N. Duldulao, Jr.
 * @since 2020-03-11
 */
package org.gobiiproject.gobiimodel.dto.children;


public class CvPropertyDTO {

    private Integer propertyId = null;
    private String propertyName = null;
    private String propertyValue = null;

    public CvPropertyDTO() {

    }

    public CvPropertyDTO(Integer propertyId,
                             String propertyName,
                             String propertyValue) {
        this.propertyId = propertyId;
        this.propertyName = propertyName;
        this.propertyValue = propertyValue;
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


}
