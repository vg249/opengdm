/**
 * GobiiProjectRequestDTO
 * 
   {
        "piContactId": "34",
        "projectName": "foo_proj_01",
        "projectDescription": "foo 01 project",
        "properties": [
            {
                "propertyId" : "1",
                "propertyValue" : "testPropValue"
            },
        ]
    }
    
 * 
 */

package org.gobiiproject.gobiimodel.dto.request;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

 import org.gobiiproject.gobiimodel.dto.children.CvPropertyDTO;

@Deprecated
public class GobiiProjectPatchDTO {
    
    private java.util.List<String> _isKeyInInput = new java.util.ArrayList<>();
    
    @Pattern(regexp="^(0|[1-9][0-9]*)$")
    private String piContactId;

    private String projectName ;

    private String projectDescription;

    @Valid
    private List<CvPropertyDTO> properties;

    public GobiiProjectPatchDTO() {
    }

    public String getPiContactId() {
        return piContactId;
    }

    public void setPiContactId(String piContactId) {
        this.piContactId = piContactId;
        this._isKeyInInput.add("piContactId");
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
        this._isKeyInInput.add("projectName");
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
        this._isKeyInInput.add("projectDescription");
    }

    public List<CvPropertyDTO> getProperties() {
        return properties;
    }

    public void setProperties(List<CvPropertyDTO> properties) {
        this.properties = properties;
        this._isKeyInInput.add("properties");
    }

    public boolean keyInPayload(String key) {
        return this._isKeyInInput.contains(key);
    }

    public List<String> payloadKeys() {
        return this._isKeyInInput;
    }

 }