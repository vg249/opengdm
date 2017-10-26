package org.gobiiproject.gobiimodel.dto.system;

import org.gobiiproject.gobiimodel.dto.base.DTOBase;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;

import java.util.Date;

/**
 * Created by Phil on 4/8/2016.
 */
public class EntityStatsDTO extends DTOBase {

    @Override
    public Integer getId() {
        return 1;
    }

    @Override
    public void setId(Integer id) {

    }

    GobiiEntityNameType entityNameType;
    GobiiEntityNameType parentNameType;
    GobiiEntityNameType childNameType;
    Integer count = null;
    Date lastModified = null;

    public GobiiEntityNameType getEntityNameType() {
        return entityNameType;
    }

    public void setEntityNameType(GobiiEntityNameType entityNameType) {
        this.entityNameType = entityNameType;
    }

    public GobiiEntityNameType getParentNameType() {
        return parentNameType;
    }

    public void setParentNameType(GobiiEntityNameType parentNameType) {
        this.parentNameType = parentNameType;
    }

    public GobiiEntityNameType getChildNameType() {
        return childNameType;
    }

    public void setChildNameType(GobiiEntityNameType childNameType) {
        this.childNameType = childNameType;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }
}
