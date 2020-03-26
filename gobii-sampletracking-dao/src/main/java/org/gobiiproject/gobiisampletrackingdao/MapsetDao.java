package org.gobiiproject.gobiisampletrackingdao;

import java.util.List;

import org.gobiiproject.gobiimodel.entity.Mapset;

public interface MapsetDao {

    List<Mapset> getMapsetsWithCounts(
            Integer pageSize, Integer rowOffset,
            Integer mapsetId, Integer experimentId);

    Mapset getMapsetWithCountsById(Integer mapsetId);


}
