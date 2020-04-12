package org.gobiiproject.gobiiapimodel.payload.sampletracking;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@SuppressWarnings("rawtypes")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class BrApiServerInfoPayload<T>  extends BrApiMasterPayload {

    private BrApiResult<T> result = new BrApiResult<>();

    public BrApiServerInfoPayload() {
    }

    public BrApiServerInfoPayload(List<T> listData) {
        this.result.setData(listData);
    }


    public BrApiServerInfoPayload(List<T> listData, Integer pageSize,
                                  Integer currentPage) {
        this.result.setData(listData);
        this.getMetadata().getPagination().setPageSize(pageSize);
        this.getMetadata().getPagination().setCurrentPage(currentPage);
    }

    public BrApiServerInfoPayload(List<T> listData, Integer pageSize,
                                  String nextPageToken) {

        this.result.setData(listData);
        this.getMetadata().getPagination().setPageSize(pageSize);
        this.getMetadata().getPagination().setNextPageToken(nextPageToken);
    }


    public BrApiResult<T> getResult() {
        return this.result;
    }

    public void setResult(BrApiResult<T> result) {
        this.result = result;
    }


}
