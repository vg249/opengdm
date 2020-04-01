/**
 * Experiment.java
 * 
 * Experiment entity class.
 * 
 */

package org.gobiiproject.gobiimodel.entity;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "experiment")
@NamedEntityGraph(name = "experiment.vendorProtocol",
    attributeNodes = @NamedAttributeNode("vendorProtocol")
)
@EqualsAndHashCode(callSuper=false)
public class Experiment extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "experiment_id")
    @Access(AccessType.PROPERTY)
    private Integer experimentId;

    @Column(name="name")
    private String experimentName;

    @Column(name="code")
    private String experimentCode;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @Column(name="manifest_id")
    private Integer manifestId;

    @Column(name="data_file")
    private String dataFile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_protocol_id")
    private VendorProtocol vendorProtocol;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "status", referencedColumnName = "cv_id")
    private Cv status = new Cv();

}

