package org.gobiiproject.gobiimodel.dto.brapi;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.gobiiproject.gobiimodel.dto.annotations.GobiiEntityMap;
import org.gobiiproject.gobiimodel.entity.Experiment;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class StudiesDTO {

    @GobiiEntityMap(paramName = "experimentId",
            entity = Experiment.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private Integer studyDbId;

    @GobiiEntityMap(paramName = "experimentName",
            entity = Experiment.class)
    private String studyName;


    @GobiiEntityMap(paramName = "project.contact.contactId",
            entity = Experiment.class, deep = true)
    @JsonSerialize(using = ToStringSerializer.class)
    private Integer contactDbId;

    @GobiiEntityMap(paramName = "project.contact.email",
            entity = Experiment.class, deep = true)
    @JsonSerialize(using = ToStringSerializer.class)
    private String email;

    @GobiiEntityMap(paramName = "experimentCode",
            entity = Experiment.class)
    private String studyCode;

    public Integer getStudyDbId() {
        return studyDbId;
    }

    public void setStudyDbId(Integer studyDbId) {
        this.studyDbId = studyDbId;
    }

    public String getStudyName() {
        return studyName;
    }

    public void setStudyName(String studyName) {
        this.studyName = studyName;
    }

    public Integer getContactDbId() {
        return contactDbId;
    }

    public void setContactDbId(Integer contactDbId) {
        this.contactDbId = contactDbId;
    }

    public String getStudyCode() {
        return studyCode;
    }

    public void setStudyCode(String studyCode) {
        this.studyCode = studyCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
