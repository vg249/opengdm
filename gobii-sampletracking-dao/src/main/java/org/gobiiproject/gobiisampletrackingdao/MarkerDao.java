package org.gobiiproject.gobiisampletrackingdao;

import org.gobiiproject.gobiimodel.entity.Marker;

import javax.persistence.Tuple;
import java.util.List;

public interface MarkerDao {

    List<Marker> getMarkers(Integer pageSize, Integer rowOffset,
                            Integer markerId, Integer datasetId);

    List<Marker> getMarkersByDatasetId(Integer datasetId,
                                       Integer pageSize, Integer rowOffset);

    List<Marker> getMarkersWithStartAndStop(Integer pageSize, Integer rowOffset,
                            Integer markerId, Integer datasetId);

}
