package org.gobiiproject.gobiisampletrackingdao;

import org.gobiiproject.gobiimodel.entity.gdmv3.ExperimentV3;

import java.util.List;

public interface ExperimentDao {

    List<ExperimentV3> getExperiments(Integer pageSize, Integer rowOffset,
                                    Integer projectId);

}
