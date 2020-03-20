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

import lombok.Data;

/**
 * Model for Project Entity.
 * Represents the database table project.
 *
 * props - is a jsonb column. It is converted to jackson.fasterxml JsonNode using a
 * user defined hibernate converter class.
 */
@Entity
@Table(name = "project")
@Data
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

    public Integer getPiContactId() {
        if (this.getContact() != null) return this.getContact().getContactId();
        return null;
    }

}
