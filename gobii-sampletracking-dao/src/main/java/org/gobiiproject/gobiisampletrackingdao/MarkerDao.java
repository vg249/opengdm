package org.gobiiproject.gobiisampletrackingdao;

import org.gobiiproject.gobiimodel.entity.Marker;

import java.util.List;

public interface MarkerDao {

    List<Marker> getMarkers(Integer pageNum, Integer pageSize,
                            Integer markerId, Integer datasetId);

}
