package org.gobiiproject.gobiidao.cache;

import java.util.Date;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;

public class EntityCountState {

    private GobiiEntityNameType gobiiEntityNameType;
    private Date lastModified;
    private Integer count;

    public EntityCountState(GobiiEntityNameType gobiiEntityNameType, Date lastModified, Integer count) {
        this.gobiiEntityNameType = gobiiEntityNameType;
        this.lastModified = lastModified;
        this.count = count;
    }

    public GobiiEntityNameType getGobiiEntityNameType() {
        return gobiiEntityNameType;
    }

    public void setGobiiEntityNameType(GobiiEntityNameType gobiiEntityNameType) {
        this.gobiiEntityNameType = gobiiEntityNameType;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
