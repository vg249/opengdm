package org.gobiiproject.gobiimodel.entity;

import com.fasterxml.jackson.databind.JsonNode;
import org.gobiiproject.gobiimodel.entity.JpaConverters.JsonbConverter;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.FetchProfile;

import javax.persistence.*;

@Entity
@Table(name = "mapset")
@FetchProfile(name = "mapset-typecv", fetchOverrides = {
        @FetchProfile.FetchOverride(entity = Mapset.class, association = "type", mode = FetchMode.JOIN),
})
public class Mapset extends BaseEntity {

    @Id
    @Column(name="mapset_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer mapSetId;

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
    @Convert(converter = JsonbConverter.class)
    private JsonNode properties;

    @Column(name = "status")
    private Integer status;

    @Transient
    private Integer markerCount;

    @Transient
    private Integer linkageGroupCount;

    public Integer getMapSetId() {
        return mapSetId;
    }

    public void setMapSetId(Integer mapSetId) {
        this.mapSetId = mapSetId;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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
}
