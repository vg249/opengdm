package org.gobiiproject.gobiimodel.dto.system;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PagedList<T> {


    public PagedList(Date queryTime, List<T> dtoList, Integer pageSize, Integer currentPageNo, Integer totalPages, String pagingQueryId) {
        this.queryTime = queryTime;
        this.dtoList = dtoList;
        this.pageSize = pageSize;
        this.currentPageNo = currentPageNo;
        this.totalPages = totalPages;
        this.pagingQueryId = pagingQueryId;
    }

    private Date queryTime;
    private List<T> dtoList = new ArrayList<>();
    private Integer pageSize;
    private Integer currentPageNo;
    private Integer totalPages;
    private String pagingQueryId;

    public Date getQueryTime() {
        return queryTime;
    }

    public List<T> getDtoList() {
        return dtoList;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public Integer getCurrentPageNo() {
        return currentPageNo;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public String getPagingQueryId() {
        return pagingQueryId;
    }
}
