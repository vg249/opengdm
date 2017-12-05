package org.gobiiproject.gobiimodel.dto.system;

import org.gobiiproject.gobiimodel.dto.base.DTOBase;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;

import java.util.Date;

/**
 * Created by Phil on 4/8/2016.
 */
public class EntityStatsDTO extends DTOBase {

    public enum EntityStateDateType {INSERT_UPDATE,INSERT_ONLY}



    @Override
    public Integer getId() {
        return 1;
    }

    @Override
    public void setId(Integer id) {

    }

    private Integer count = null;
    private Date lastModified = null;
    private GobiiEntityNameType entityNameType = GobiiEntityNameType.UNKNOWN;
    private EntityStateDateType entityStateDateType = EntityStateDateType.INSERT_UPDATE;


    public GobiiEntityNameType getEntityNameType() {
        return entityNameType;
    }

    public void setEntityNameType(GobiiEntityNameType entityNameType) {
        this.entityNameType = entityNameType;
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

    public EntityStateDateType getEntityStateDateType() {
        return entityStateDateType;
    }

    public void setEntityStateDateType(EntityStateDateType entityStateDateType) {
        this.entityStateDateType = entityStateDateType;
    }
}
