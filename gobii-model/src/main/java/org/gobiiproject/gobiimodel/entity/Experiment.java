package org.gobiiproject.gobiimodel.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Model for Experiment Entity.
 * Represents database table Experiment.
 */
public class Experiment extends BaseEntity{


    public Integer getExperimentId() {
        return experimentId;
    }

    public void setExperimentId(Integer experimentId) {
        this.experimentId = experimentId;
    }

    public String getExperimentName() {
        return experimentName;
    }

    public void setExperimentName(String experimentName) {
        this.experimentName = experimentName;
    }

    public String getExperimentCode() {
        return experimentCode;
    }

    public void setExperimentCode(String experimentCode) {
        this.experimentCode = experimentCode;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getManifestId() {
        return manifestId;
    }

    public void setManifestId(Integer manifestId) {
        this.manifestId = manifestId;
    }

    public String getDataFile() {
        return dataFile;
    }

    public void setDataFile(String dataFile) {
        this.dataFile = dataFile;
    }

    public Integer getVendorProtocolId() {
        return vendorProtocolId;
    }

    public void setVendorProtocolId(Integer vendorProtocolId) {
        this.vendorProtocolId = vendorProtocolId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "experiment_name")
    public Integer experimentId;

    @Column(name="name")
    public String experimentName;

    @Column(name="code")
    public String experimentCode;

    @Column(name="project_id")
    public Integer projectId;

    @Column(name="manifest_id")
    public Integer manifestId;

    @Column(name="data_file")
    public String dataFile;

    @Column(name="vendor_protocol_id")
    public Integer vendorProtocolId;

}
