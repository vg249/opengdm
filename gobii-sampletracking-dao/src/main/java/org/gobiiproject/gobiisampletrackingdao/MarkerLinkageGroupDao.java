package org.gobiiproject.gobiisampletrackingdao;

import org.gobiiproject.gobiimodel.entity.MarkerLinkageGroup;

import java.math.BigDecimal;
import java.util.List;

public interface MarkerLinkageGroupDao {

    List<MarkerLinkageGroup>
    getMarkerLinkageGroups(Integer pageSize , Integer rowOffset,
                           Integer mapsetId, String mapsetName,
                           Integer linkageGroupId, String linkageGroupName,
                           Integer markerId, String markerName,
                           BigDecimal minPosition, BigDecimal maxPosition,
                           Integer datasetId);


}
