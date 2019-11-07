package org.gobiiproject.gobiimodel.entity;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import java.util.Date;

@MappedSuperclass
public class BaseEntity {

    @Column(name="created_by")
    private Integer createdBy;

    @Column(name="created_date")
    private Date createdDate;

    @Column(name="modified_by")
    private Integer modifiedBy;

    @Column(name="modified_date")
    private Date modifiedDate;

    public Cv getStatus() {
        return status;
    }

    public void setStatus(Cv status) {
        this.status = status;
    }

    @ManyToOne
    @JoinColumn(name = "status", referencedColumnName = "cv_id")
    private Cv status = new Cv();


    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Integer modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

}
