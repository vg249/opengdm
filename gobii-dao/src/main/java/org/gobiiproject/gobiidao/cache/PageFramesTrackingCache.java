package org.gobiiproject.gobiidao.cache;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

/***

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
