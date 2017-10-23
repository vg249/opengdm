package org.gobiiproject.gobiimodel.dto.base;

import org.gobiiproject.gobiimodel.dto.annotations.GobiiEntityColumn;
import org.gobiiproject.gobiimodel.dto.annotations.GobiiEntityParam;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Phil on 9/25/2016.
 */
public abstract class DTOBase {

    private Integer createdBy;
    private Date createdDate;
    private Integer modifiedBy;
    private Date modifiedDate;

    private Set<GobiiProcessType> allowedProcessTypes = new HashSet<>();

    public abstract Integer getId();
    public abstract void setId(Integer id);

    public Set<GobiiProcessType> getAllowedProcessTypes() {
        return allowedProcessTypes;
    }

    public void setAllowedProcessTypes(Set<GobiiProcessType> allowedProcessTypes) {
        this.allowedProcessTypes = allowedProcessTypes;
    }

    @GobiiEntityParam(paramName = "createdBy")
    public Integer getCreatedBy() {
        return createdBy;
    }

    @GobiiEntityColumn(columnName = "created_by")
    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    @GobiiEntityParam(paramName = "createdDate")
    public Date getCreatedDate() {
        return createdDate;
    }

    @GobiiEntityColumn(columnName = "created_date")
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @GobiiEntityParam(paramName = "modifiedBy")
    public Integer getModifiedBy() {
        return modifiedBy;
    }

    @GobiiEntityColumn(columnName = "modified_by")
    public void setModifiedBy(Integer modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    @GobiiEntityParam(paramName = "modifiedDate")
    public Date getModifiedDate() {
        return modifiedDate;
    }

    @GobiiEntityColumn(columnName = "modified_date")
    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }



}
