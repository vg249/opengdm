package org.gobiiproject.gobiimodel.dto.entity.noaudit;

import org.gobiiproject.gobiimodel.dto.entity.annotations.GobiiEntityColumn;

/**
 * Brapi DTO for LinkageGroups
 */
public class LinkageGroupBrapiDTO {


    private String linkageGroupName;

    private Integer maxPosition;

    private Long markerCount;

    public String getLinkageGroupName() {
        return linkageGroupName;
    }

    @GobiiEntityColumn(columnName = "linkage_group_name")
    public void setLinkageGroupName(String linkageGroupName) {
        this.linkageGroupName = linkageGroupName;
    }

    public Integer getMaxPosition() {
        return maxPosition;
    }

    @GobiiEntityColumn(columnName = "max_position")
    public void setMaxPosition(Integer maxPosition) {
        this.maxPosition = maxPosition;
    }

    public Long getMarkerCount() {
        return markerCount;
    }

    @GobiiEntityColumn(columnName = "marker_count")
    public void setMarkerCount(Long markerCount) {
        this.markerCount = markerCount;
    }

}
