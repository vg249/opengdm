package org.gobiiproject.gobiidao.cache;

import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import java.util.Date;

/***
 * The idiom here is not obvious. There is a lot of magic going on in the background via the annotations and the
 * cachemanager configuration in the application-config.xml file, which is where we set up the "tableTracker" cache.
 * The cache is essentially a kev-value pair storage mechanism. Because we specify the gobiiEntityNameType parameter as
 * the key, the values are keyed by that value. If getLasetModified() is called first, the returned
 * value is cached indefinitely. Subsequent calls to getLastModified() will return the value from the cache
 * for that gobiiEntityNameType. This behavior is determined by the @Cacheable annotation. The @CachePut annotation on
 * setLastModified() replaces the existing value for the specified gobiiEntityNameType. If @CachePut is called first,
 * getLastModified() does not replace that value but rather gets it from the store. 
 */
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
