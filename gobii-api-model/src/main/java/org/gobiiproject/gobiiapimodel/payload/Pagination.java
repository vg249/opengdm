package org.gobiiproject.gobiiapimodel.payload;

import java.util.Date;

/**
 * Created by Phil on 9/6/2016.
 */
public class Pagination {


    public Pagination() {}

    public Pagination(String queryId,
                      Date queryTime,
                      Integer pageSize,
                      Integer totalPages,
                      Integer currentPage
    ) {
        this.queryId = queryId;
        this.queryTime = queryTime;
        this.pageSize= pageSize;
        this.totalPages= totalPages;
        this.currentPage= currentPage;

    }

    private String queryId;
    private Date queryTime;
    private Integer pageSize;
    private Integer totalPages;
    private Integer currentPage;


    public String getQueryId() {
        return queryId;
    }

    public Date getQueryTime() {
        return queryTime;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }
}
