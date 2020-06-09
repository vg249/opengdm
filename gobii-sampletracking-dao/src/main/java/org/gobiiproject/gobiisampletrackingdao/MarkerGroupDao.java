package org.gobiiproject.gobiisampletrackingdao;

import java.util.List;

import org.gobiiproject.gobiimodel.entity.MarkerGroup;

public interface MarkerGroupDao {

	MarkerGroup createMarkerGroup(MarkerGroup markerGroup);

	List<MarkerGroup> getMarkerGroups(Integer offset, Integer pageSize);

	MarkerGroup getMarkerGroup(Integer markerGroupId);

	MarkerGroup updateMarkerGroup(MarkerGroup markerGroup);
    
}