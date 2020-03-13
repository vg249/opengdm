package org.gobiiproject.gobiimodel.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.gobiiproject.gobiimodel.entity.pgsql.ProjectProperties;
import org.hibernate.annotations.Type;
import static org.gobiiproject.gobiimodel.utils.LineUtils.isNullOrEmpty;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="pi_contact", referencedColumnName="contact_id")
    private Contact contact;

    @Column(name="code")
    private String projectCode;

    @Column(name="description")
    private String projectDescription;

    @Column(name="props")
    @Type(type = "ProjectPropertiesType")
    private ProjectProperties properties;

    @ManyToOne(fetch = FetchType.LAZY)
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

    public ProjectProperties getProperties() {
        return this.properties;
    }

    public void setProperties(ProjectProperties properties) {
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
        if (!isNullOrEmpty(this.contact.getFirstName()) &&
            !isNullOrEmpty(this.contact.getLastName())) {
                return String.format("%s, %s", this.contact.getLastName(), this.contact.getFirstName());
        }
        if (!isNullOrEmpty(this.contact.getFirstName())) {
            return this.contact.getFirstName(); //covers one-name persons
        }
        if (!isNullOrEmpty(this.contact.getLastName())) {
            return this.contact.getLastName();
        }
        return null;
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
