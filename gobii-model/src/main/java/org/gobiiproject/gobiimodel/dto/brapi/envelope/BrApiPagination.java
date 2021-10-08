package org.gobiiproject.gobiimodel.dto.brapi.envelope;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class BrApiPagination {

    @ApiModelProperty(hidden = true)
    private Integer totalCount;
    @ApiModelProperty(example = "1000")
    private Integer pageSize;
    @ApiModelProperty(hidden = true)
    private Integer totalPages;
    @ApiModelProperty(example = "0")
    private Integer currentPage;
    @ApiModelProperty(example = "123!")
    private String nextPageToken;

    private Integer dim2PageSize;
    private Integer dim2CurrentPage;

}
