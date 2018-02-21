package org.gobiiproject.gobiidao.cache;

import org.gobiiproject.gobiidao.resultset.core.listquery.DtoListQueryColl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

/***

 */
public class PageFramesTrackingCache {

    @Autowired
    private DtoListQueryColl dtoListQueryColl;

    // return null until the @CachePut method is called with a page frame value
    @Cacheable(cacheNames = "pageFramesTrackingCache", key = "#pageQueryId")
    public PageFrameState getPageFrames(String pageQueryId) {
        return null;
    }

    @CachePut(cacheNames = "pageFramesTrackingCache", key = "#pageQueryId")
    public PageFrameState setPageFrames(String pageQueryId, PageFrameState pageFrameState) {
        return pageFrameState;
    }

}
