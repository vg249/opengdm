package org.gobiiproject.gobiimodel.dto.system;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PagedList<T> {

    private List<T> result = new ArrayList<>();

    private Integer pageSize;

    private Integer currentPageNum;

    private String nextPageToken;

    public List<T> getResult() {
        return result;
    }

    public void setResult(List<T> result) {
        this.result = result;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getCurrentPageNum() {
        return currentPageNum;
    }

    public void setCurrentPageNum(Integer currentPageNum) {
        this.currentPageNum = currentPageNum;
    }

    public String getNextPageToken() {
        return nextPageToken;
    }

    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }
}
