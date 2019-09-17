package org.gobiiproject.gobiimodel.dto.entity.noaudit;

import org.gobiiproject.gobiimodel.dto.entity.annotations.GobiiEntityColumn;

/**
 * Brapi DTO for LinkageGroups
 */
public class LinkageGroupBrapiDTO {


    private String linkageGroupName;

    private Integer maxPosition;

    private Integer markerCount;

    public String getLinkageGroupName() {
        return linkageGroupName;
    }

    public void setLinkageGroupName(String linkageGroupName) {
        this.linkageGroupName = linkageGroupName;
    }

    public Integer getMaxPosition() {
        return maxPosition;
    }

    public void setMaxPosition(Integer maxPosition) {
        this.maxPosition = maxPosition;
    }

    public Integer getMarkerCount() {
        return markerCount;
    }

    public void setMarkerCount(Integer markerCount) {
        this.markerCount = markerCount;
    }

}
