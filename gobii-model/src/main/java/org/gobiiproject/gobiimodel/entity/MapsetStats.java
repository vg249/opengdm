package org.gobiiproject.gobiimodel.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "mapset_stats")
@Data
@EqualsAndHashCode(callSuper=false)
public class MapsetStats {
    @Id
    @Column(name="mapset_id")
    private Integer mapsetId;


    @Column(name="marker_count")
    private Integer markerCount;

    @Column(name="linkage_group_count")
    private Integer linkageGroupCount;

    @OneToOne
    @JoinColumn(name = "mapset_id")
    @MapsId
    private Mapset mapset;
}
