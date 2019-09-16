package org.gobiiproject.gobiimodel.entity;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.NullNode;
import org.gobiiproject.gobiimodel.entity.JpaConverters.JsonbConverter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;

/**
 * Model for Project Entity.
 * Represents the database table project.
 *
 * props - is a jsonb column. It is converted to jackson.fasterxml JsonNode using a
 * user defined hibernate converter class.
 */
@Entity
@Table(name = "project")
public class Project extends BaseEntity {

    @Id
    @Column(name="project_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer projectId;

    @Column(name="name")
    public String projectName;

    @Column(name="pi_contact")
    public Integer piContactId;

    @Column(name="code")
    public String projectCode;

    @Column(name="description")
    public String projectDescription;

    @Column(name="status")
    public Integer projectStatus;

    @Column(name="props", columnDefinition = "jsonb")
    @Convert(converter = JsonbConverter.class)
    public JsonNode properties = JsonNodeFactory.instance.objectNode();

    public Integer getProjectId() {
        return this.projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return this.projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }


    public Integer getPiContactId() {
        return this.piContactId;
    }

    public void setPiContactId(Integer piContactId) {
        this.piContactId = piContactId;
    }

    public String getProjectCode() {

        return this.projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getProjectDescription() {
        return this.projectDescription;
    }

    public void  setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public Integer getProjectStatus() {
        return this.projectStatus;
    }

    public void setProjectStatus(Integer projectStatus) {
        this.projectStatus = projectStatus;
    }

    public JsonNode getProperties() {
        return this.properties;
    }

    public void setProperties(JsonNode properties) {
        this.properties = properties;
    }


}
