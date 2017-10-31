package org.gobiiproject.gobiidao.cache;

import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import java.util.Date;

public class TableTrackingCache {

    @Cacheable(cacheNames="tableTracker", key="#gobiiEntityNameType")
    public Date getLastModified(GobiiEntityNameType gobiiEntityNameType) {
        return new Date();
    }

    @CachePut(cacheNames="tableTracker", key="#gobiiEntityNameType")
    public Date setLastModified(GobiiEntityNameType gobiiEntityNameType, Date date) {
        return date;
    }


}
