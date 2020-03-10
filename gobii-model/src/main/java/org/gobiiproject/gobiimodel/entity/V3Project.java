package org.gobiiproject.gobiimodel.entity;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

import org.gobiiproject.gobiimodel.entity.BaseEntity;
import org.gobiiproject.gobiimodel.entity.Cv;
import org.gobiiproject.gobiimodel.entity.JpaConverters.JsonbConverter;

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
public class V3Project extends BaseEntity {

    @Id
    @Column(name="project_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer projectId;

    @Column(name="name")
    private String projectName;

    @OneToOne
    @JoinColumn(name="pi_contact", referencedColumnName="contact_id")
    private Contact contact;

    @Column(name="code")
    private String projectCode;

    @Column(name="description")
    private String projectDescription;

    @Column(name="props", columnDefinition = "jsonb")
    @Convert(converter = JsonbConverter.class)
    private JsonNode properties = JsonNodeFactory.instance.objectNode();

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

    public JsonNode getProperties() {
        return this.properties;
    }

    public void setProperties(JsonNode properties) {
        this.properties = properties;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public Integer getPiContactId() {
        if (this.contact == null) return null;
        return this.contact.getContactId();
    }

    public String getPiContactName() {
        if (this.contact == null) return null;
        return this.contact.getUsername();
    }

    
    public Integer getExperimentCount() {
        return null;
    }

    public Integer getDatasetCount() {
        return null;
    }

    public Integer getDnaRunsCount() {
        return null;
    }

    public Integer getMarkersCount() {
        return null;
    }
}
