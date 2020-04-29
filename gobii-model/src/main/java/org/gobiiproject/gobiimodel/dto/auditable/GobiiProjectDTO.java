/**
 * V3ProjectDTO.java
 * 
 * DTO for Project data (Gobii API V3)
 * 
 * @author Rodolfo N. Duldulao, Jr. <rnduldulaojr@gmail.com>
 * @since 2020-03-07
 */
package org.gobiiproject.gobiimodel.dto.auditable;

import static org.gobiiproject.gobiimodel.utils.LineUtils.isNullOrEmpty;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import org.gobiiproject.gobiimodel.dto.annotations.GobiiEntityMap;
import org.gobiiproject.gobiimodel.dto.base.DTOBaseAuditable;
import org.gobiiproject.gobiimodel.dto.children.CvPropertyDTO;
import org.gobiiproject.gobiimodel.entity.Project;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;

import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = false, value={
    "id", "allowedProcessTypes", "entityNameType", "status"
})
@JsonPropertyOrder({
    "projectId", 
    "projectName",
    "projectDescription",
    "piContactId",
    "piContactName",
    "experimentCount",
    "datasetCount",
    "markersCount",
    "dnaRunsCount",
    "createdBy",
    "createdDate",
    "modifiedBy",
    "modifiedDate",
    "properties"
})
@JsonInclude(JsonInclude.Include.ALWAYS)
@Data
@EqualsAndHashCode(callSuper = false)
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

    @GobiiEntityMap(paramName="contact.lastName", entity = Project.class, deep=true)
    @JsonIgnore
    private String piContactLastName;

    @GobiiEntityMap(paramName="contact.firstName", entity = Project.class, deep=true)
    @JsonIgnore
    private String piContactFirstName;

    //TODO: when the stats table is done
    private Integer experimentCount;
    private Integer datasetCount;
    private Integer markersCount;
    private Integer dnaRunsCount;

    private List<CvPropertyDTO> properties = new java.util.ArrayList<>();

    @JsonProperty("piContactName")
    public String getPiContactName() {
        if (!isNullOrEmpty(piContactFirstName) &&
            !isNullOrEmpty(piContactLastName)) {
            return String.format("%s, %s", piContactLastName, piContactFirstName);
        }
        if (!isNullOrEmpty(piContactFirstName)) {
            return piContactFirstName; //covers one-name persons
        }
        if (!isNullOrEmpty(piContactLastName)) {
            return piContactLastName;
        }
        return null;
    }

}
