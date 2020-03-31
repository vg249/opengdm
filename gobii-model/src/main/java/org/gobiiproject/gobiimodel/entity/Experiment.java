/**
 * Experiment.java
 * 
 * Experiment entity class.
 * 
 */


package org.gobiiproject.gobiimodel.entity;

import javax.persistence.*;

import org.gobiiproject.gobiimodel.entity.BaseEntity;
import org.gobiiproject.gobiimodel.entity.Cv;
import org.gobiiproject.gobiimodel.entity.Project;

import lombok.Data;

@Data
@Entity
@Table(name = "experiment")
@NamedEntityGraph(name = "experiment.vendorProtocol",
    attributeNodes = @NamedAttributeNode("vendorProtocol")
)
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

