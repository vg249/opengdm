package org.gobiiproject.gobiisampletrackingdao;

import org.gobiiproject.gobiimodel.entity.Mapset;

import javax.persistence.Tuple;
import java.util.List;

public interface MapsetDao {

    List<Mapset> getMapsetsWithCountsByExperimentId(
            Integer pageSize, Integer rowOffset,
            Integer experimentId);



}
