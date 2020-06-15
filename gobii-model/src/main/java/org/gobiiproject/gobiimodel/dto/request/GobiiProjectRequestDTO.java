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
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import org.gobiiproject.gobiimodel.dto.children.CvPropertyDTO;

public class GobiiProjectRequestDTO {
       
    @NotEmpty
    @Pattern(regexp="^(0|[1-9][0-9]*)$")
    private String piContactId;

    @NotEmpty
    private String projectName;

    private String projectDescription;

    @Valid
    private List<CvPropertyDTO> properties;

    public GobiiProjectRequestDTO() {
    }

    public String getPiContactId() {
        return piContactId;
    }

    public void setPiContactId(String piContactId) {
        this.piContactId = piContactId;
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

    public List<CvPropertyDTO> getProperties() {
        return properties;
    }

    public void setProperties(List<CvPropertyDTO> properties) {
        this.properties = properties;
    }

 }