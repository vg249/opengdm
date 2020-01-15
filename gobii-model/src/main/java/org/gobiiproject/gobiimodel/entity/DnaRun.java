package org.gobiiproject.gobiimodel.entity;


import com.fasterxml.jackson.databind.JsonNode;
import org.gobiiproject.gobiimodel.entity.JpaConverters.JsonbConverter;
import org.gobiiproject.gobiimodel.entity.JpaConverters.StringArrayConverter;

import javax.persistence.*;

/**
 * Model for Dnarun(dnarun) Entity in database.
 *
 * props - is a jsonb column. It is converted to jackson.fasterxml JsonNode using a
 * user defined hibernate converter.
 */
@Entity
@Table(name = "dnarun")
public class DnaRun {

    @Id
    @Column(name="dnarun_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer dnaRunId;

    @ManyToOne
    @JoinColumn(name = "experiment_id")
    private Experiment experiment  = new Experiment();

    @ManyToOne
    @JoinColumn(name = "dnasample_id")
    private DnaSample dnaSample = new DnaSample();

    @Column(name="name")
    private String dnaRunName;

    @Column(name="dataset_dnarun_idx", columnDefinition = "jsonb")
    @Convert(converter = JsonbConverter.class)
    private JsonNode datasetMarkerIdx;

    @Column(name="props", columnDefinition = "jsonb")
    @Convert(converter = JsonbConverter.class)
    private JsonNode properties;


    public Integer getDnaRunId() {
        return dnaRunId;
    }

    public void setDnaRunId(Integer dnaRunId) {
        this.dnaRunId = dnaRunId;
    }

    public String getDnaRunName() {
        return dnaRunName;
    }

    public void setDnaRunName(String dnaRunName) {
        this.dnaRunName = dnaRunName;
    }

    public Experiment getExperiment() {
        return experiment;
    }

    public void setExperiment(Experiment experiment) {
        this.experiment = experiment;
    }

    public DnaSample getDnaSample() {
        return dnaSample;
    }

    public void setDnaSample(DnaSample dnaSample) {
        this.dnaSample = dnaSample;
    }


    public JsonNode getDatasetMarkerIdx() {
        return datasetMarkerIdx;
    }

    public void setDatasetMarkerIdx(JsonNode datasetMarkerIdx) {
        this.datasetMarkerIdx = datasetMarkerIdx;
    }

    public JsonNode getProperties() {
        return properties;
    }

    public void setProperties(JsonNode properties) {
        this.properties = properties;
    }
}
