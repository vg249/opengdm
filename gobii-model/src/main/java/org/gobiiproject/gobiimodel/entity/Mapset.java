package org.gobiiproject.gobiimodel.entity;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.databind.JsonNode;

import org.gobiiproject.gobiimodel.entity.JpaConverters.JsonbConverter;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "mapset")
public class Mapset extends BaseEntity {

    @Id
    @Column(name="mapset_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer mapsetId;

    @Column(name="name")
    private String mapsetName;

    @Column(name = "code")
    private String mapSetCode;

    @Column(name="description")
    private String mapSetDescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reference_id")
    private Reference reference;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id")
    private Cv type;

    @Column(name="props", columnDefinition = "jsonb")
    @Type(type = "jsonb")
    private JsonNode properties;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status", referencedColumnName = "cv_id")
    private Cv status;

    @Transient
    private Integer markerCount;

    @Transient
    private Integer linkageGroupCount;

    public Integer getMapsetId() {
        return mapsetId;
    }

    public void setMapsetId(Integer mapsetId) {
        this.mapsetId = mapsetId;
    }

    public String getMapsetName() {
        return mapsetName;
    }

    public void setMapsetName(String mapsetName) {
        this.mapsetName = mapsetName;
    }

    public String getMapSetCode() {
        return mapSetCode;
    }

    public void setMapSetCode(String mapSetCode) {
        this.mapSetCode = mapSetCode;
    }

    public String getMapSetDescription() {
        return mapSetDescription;
    }

    public void setMapSetDescription(String mapSetDescription) {
        this.mapSetDescription = mapSetDescription;
    }

    public Reference getReference() {
        return reference;
    }

    public void setReference(Reference reference) {
        this.reference = reference;
    }

    public Cv getType() {
        return type;
    }

    public void setType(Cv type) {
        this.type = type;
    }

    public JsonNode getProperties() {
        return properties;
    }

    public void setProperties(JsonNode properties) {
        this.properties = properties;
    }

    public Integer getMarkerCount() {
        return markerCount;
    }

    public void setMarkerCount(Integer markerCount) {
        this.markerCount = markerCount;
    }

    public Integer getLinkageGroupCount() {
        return linkageGroupCount;
    }

    public void setLinkageGroupCount(Integer linkageGroupCount) {
        this.linkageGroupCount = linkageGroupCount;
    }

    public Cv getStatus() {
        return status;
    }

    public void setStatus(Cv status) {
        this.status = status;
    }
}
