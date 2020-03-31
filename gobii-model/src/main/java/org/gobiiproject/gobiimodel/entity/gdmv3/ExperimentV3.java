package org.gobiiproject.gobiimodel.entity.gdmv3;

import javax.persistence.*;

import org.gobiiproject.gobiimodel.entity.BaseEntity;
import org.gobiiproject.gobiimodel.entity.Cv;
import org.gobiiproject.gobiimodel.entity.Project;

import lombok.Data;

/**
 * Model for Experiment Entity.
 * Represents database table Experiment.
 */
@Data
@Entity
@Table(name = "experiment")
@NamedEntityGraph(name = "experiment.vendorProtocol",
    attributeNodes = @NamedAttributeNode("vendorProtocol")
)
public class ExperimentV3 extends BaseEntity{

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
