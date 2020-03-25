package org.gobiiproject.gobiisampletrackingdao;

import java.util.List;

import org.gobiiproject.gobiimodel.entity.Marker;

public interface MarkerDao {

    List<Marker> getMarkers(Integer pageSize, Integer rowOffset,
                            Integer markerId, Integer datasetId);

    Marker getMarkerById(Integer markerId);

    List<Marker> getMarkersByMarkerIdCursor(Integer pageSize,
                                            Integer markerIdCursor,
                                            Integer markerId,
                                            Integer datasetId);

    List<Marker> getMarkersByDatasetId(Integer datasetId,
                                       Integer pageSize, Integer rowOffset);

    List<Marker> getMarkersByMarkerIds(List<Integer> markerIds);
    List<Marker> getMarkersByMarkerNames(List<String> markerNames);

}
