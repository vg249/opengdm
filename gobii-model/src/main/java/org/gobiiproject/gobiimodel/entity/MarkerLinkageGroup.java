package org.gobiiproject.gobiimodel.entity;


import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Model for MarkerLinkageGroup Entity.
 * Represents database table marker_linkage_group.
 *
 */
@Entity
@Table(name = "marker_linkage_group")
public class MarkerLinkageGroup {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "marker_linkage_group_id")
    private Integer makrerLinkageGroupId;

    @Column(name = "marker_id")
    private Integer markerId;

    @Column(name = "start")
    private BigDecimal start;

    @Column(name = "stop")
    private BigDecimal stop;

    // TODO: LinkageGroup entity will be referenced in future
    @Column(name = "linkage_group_id")
    private Integer linkageGroupId;

    public Integer getMakrerLinkageGroupId() {
        return makrerLinkageGroupId;
    }

    public void setMakrerLinkageGroupId(Integer makrerLinkageGroupId) {
        this.makrerLinkageGroupId = makrerLinkageGroupId;
    }

    public Integer getMarkerId() {
        return markerId;
    }

    public void setMarkerId(Integer markerId) {
        this.markerId = markerId;
    }

    public BigDecimal getStart() {
        return start;
    }

    public void setStart(BigDecimal start) {
        this.start = start;
    }

    public BigDecimal getStop() {
        return stop;
    }

    public void setStop(BigDecimal stop) {
        this.stop = stop;
    }

    public Integer getLinkageGroupId() {
        return linkageGroupId;
    }

    public void setLinkageGroupId(Integer linkageGroupId) {
        this.linkageGroupId = linkageGroupId;
    }
}

