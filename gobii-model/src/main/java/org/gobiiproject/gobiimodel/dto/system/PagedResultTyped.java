package org.gobiiproject.gobiimodel.dto.system;

import lombok.Data;

/**
 * @param <T>
 */
@Data
 public class PagedResultTyped<T> {

    private T result;
    private String nextPageToken;
    private Integer currentPageNum;
    private Integer currentPageSize;
    private Integer currentDim2PageNum;
    private Integer currentDim2PageSize;
}
