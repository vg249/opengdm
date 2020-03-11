package org.gobiiproject.gobiimodel.entity;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

import org.gobiiproject.gobiimodel.entity.JpaConverters.JsonbConverter;

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

    @ManyToOne
    @JoinColumn(name = "status", referencedColumnName = "cv_id")
    private Cv status = new Cv();

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

    public Cv getStatus() {
        return status;
    }

    public void setStatus(Cv status) {
        this.status = status;
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
