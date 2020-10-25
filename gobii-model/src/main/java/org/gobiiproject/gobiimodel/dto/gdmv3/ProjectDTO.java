package org.gobiiproject.gobiimodel.dto.gdmv3;

import static org.gobiiproject.gobiimodel.utils.LineUtils.isNullOrEmpty;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import org.gobiiproject.gobiimodel.dto.annotations.GobiiEntityMap;
import org.gobiiproject.gobiimodel.dto.base.DTOBaseAuditable;
import org.gobiiproject.gobiimodel.dto.children.CvPropertyDTO;
import org.gobiiproject.gobiimodel.entity.Project;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties({ "id", "allowedProcessTypes", "entityNameType", "status" })
@JsonInclude(JsonInclude.Include.ALWAYS)
public class ProjectDTO extends DTOBaseAuditable {

    public interface Create {}
    public interface Update {}

    @Override
    public Integer getId() {
        return null;
    }

    @Override
    public void setId(Integer id) {
    }

    public ProjectDTO() {
        super(GobiiEntityNameType.PROJECT);
    }


    // we are waiting until we a have a view to return
    // properties for that property: we don't know how to represent them yet
    @GobiiEntityMap(paramName = "projectId", entity = Project.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private Integer projectId = 0;

    @GobiiEntityMap(paramName="projectName", entity = Project.class)
    @NotEmpty(groups = {ProjectDTO.Create.class})
    private String projectName;

    @GobiiEntityMap(paramName="projectDescription", entity = Project.class)
    private String projectDescription;

    @GobiiEntityMap(paramName="contact.contactId", entity = Project.class, deep=true)
    @JsonIgnore
    private Integer contactId;

    @NotNull(groups = {ProjectDTO.Create.class})
    private String piContactId;

    @GobiiEntityMap(paramName="contact.lastName", entity = Project.class, deep=true)
    @JsonIgnore
    @Getter(AccessLevel.NONE)
    private String piContactLastName;

    @GobiiEntityMap(paramName="contact.firstName", entity = Project.class, deep=true)
    @JsonIgnore
    private String piContactFirstName;

    //TODO: when the stats table is done
    private Integer experimentCount;

    private Integer datasetCount;

    private Integer markersCount;

    private Integer dnaRunsCount;

    @Valid
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


    //custom getter
    public String getPiContactId() {
        return Optional.ofNullable(contactId).map(v -> v.toString()).orElse(piContactId);
    }

   
}