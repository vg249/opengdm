package org.gobiiproject.gobiimodel.dto.base;

import org.gobiiproject.gobiimodel.dto.annotations.GobiiEntityColumn;
import org.gobiiproject.gobiimodel.dto.annotations.GobiiEntityParam;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * All Dto classes for tables that have the created_date, modified_date, created_by,
 * and modified_by columns, only those classes, must derive from DTOBaseAuditable.
 * This inheritance strategy makes it so that such DTOs will have the methods and column
 * name annotations will be standardized. Moreover, this interface is relied upon by
 * the DtoMapAspect class such that for the DtoMap* classes that derive from DtoMap<> and
 * live in the entity.auditable namespace, the aspect will automatically call these methods
 * to maintain the date and user columns systematically.
 */
public abstract class DTOBaseAuditable extends DTOBase {

    private Integer createdBy = null;
    private Date createdDate = null;
    private Integer modifiedBy = null;
    private Date modifiedDate = null;

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
