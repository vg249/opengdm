package org.gobiiproject.gobiidao.cache;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

/***
 * As a reminder of how the voo doo underlying the cache works:
 * The cache is essentially a kev-value pair storage mechanism. Because we specify the pageQueryId parameter as
 * the key, the values are keyed by that value. If getPageFrames() is called first, you get null. The @CachePut
 * annotation on setPageFrames() makes it so that this method replaces the existing value for the specified pageQueryId.
 * Thus, getPageFrames() will  return null until setPageFrames() is called. Once setPageFrames() has been called,
 * getPageFrames() will return the instance that was set.
 *
 * NOTE: cached methods DO NOT WORK if you call them from within the class that contains them. For example, if one were to
 * write a wrapper method around getParentChildState() within this class, the method would work, but the result would not
 * be cached. Thus, the consumer of the cache _must_ be in a class outside this class.
 */

public class PageFramesTrackingCache {

    // return null until the @CachePut method is called with a page frame value
    @Cacheable(cacheNames = "pageFramesCache", key = "#pageQueryId")
    public PageFrameState getPageFrames(String pageQueryId) {
        return null;
    }

    @CachePut(cacheNames = "pageFramesCache", key = "#pageQueryId")
    public PageFrameState setPageFrames(String pageQueryId, PageFrameState pageFrameState) {
        return pageFrameState;
    }

}
