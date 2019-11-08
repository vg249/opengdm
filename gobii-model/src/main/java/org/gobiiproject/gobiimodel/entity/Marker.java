package org.gobiiproject.gobiimodel.entity;


import com.fasterxml.jackson.databind.JsonNode;
import org.gobiiproject.gobiimodel.entity.JpaConverters.JsonbConverter;

import javax.persistence.*;
import java.util.List;

/**
 * Model for Marker Entity.
 * Represents the database table project.
 *
 * props - is a jsonb column. It is converted to jackson.fasterxml JsonNode using a
 * user defined hibernate converter class.
 */
@Entity
@Table(name = "marker")
public class Marker extends BaseEntity {

    @Id
    @Column(name="marker_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer markerId;

    @ManyToOne
    @JoinColumn(name = "platform_id")
    private Platform platform = new Platform();

    @Column(name="variant_id")
    private Integer variantId;

    @Column(name="name")
    private String markerName;

    @Column(name="ref")
    private String ref;

    @Column(name="alts")
    private List<String> alts;

    @Column(name="sequence")
    private String sequence;

    @ManyToOne
    @JoinColumn(name = "reference_id")
    private Reference reference = new Reference();

    @ManyToOne
    @JoinColumn(name = "strand_id", referencedColumnName = "cv_id")
    private Cv strand = new Cv();

    @Column(name="primers", columnDefinition = "jsonb")
    @Convert(converter = JsonbConverter.class)
    private JsonNode primers;

    @Column(name="probsets", columnDefinition = "jsonb")
    @Convert(converter = JsonbConverter.class)
    private JsonNode probsets;

    @Column(name="dataset_marker_idx", columnDefinition = "jsonb")
    @Convert(converter = JsonbConverter.class)
    private JsonNode datasetMarkerIdx;

    @Column(name="props", columnDefinition = "jsonb")
    @Convert(converter = JsonbConverter.class)
    private JsonNode properties;

    public Integer getMarkerId() {
        return markerId;
    }

    public void setMarkerId(Integer markerId) {
        this.markerId = markerId;
    }

    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    public Integer getVariantId() {
        return variantId;
    }

    public void setVariantId(Integer variantId) {
        this.variantId = variantId;
    }

    public String getMarkerName() {
        return markerName;
    }

    public void setMarkerName(String markerName) {
        this.markerName = markerName;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public List<String> getAlts() {
        return alts;
    }

    public void setAlts(List<String> alts) {
        this.alts = alts;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public Cv getStrand() {
        return strand;
    }

    public void setStrand(Cv strand) {
        this.strand = strand;
    }

    public JsonNode getPrimers() {
        return primers;
    }

    public void setPrimers(JsonNode primers) {
        this.primers = primers;
    }

    public JsonNode getProbsets() {
        return probsets;
    }

    public void setProbsets(JsonNode probsets) {
        this.probsets = probsets;
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

    public Reference getReference() {
        return reference;
    }

    public void setReference(Reference reference) {
        this.reference = reference;
    }
}
