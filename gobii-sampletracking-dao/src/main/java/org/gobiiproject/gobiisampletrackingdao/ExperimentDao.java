package org.gobiiproject.gobiisampletrackingdao;

import org.gobiiproject.gobiimodel.entity.Experiment;

import java.util.List;

public interface ExperimentDao {

    List<Experiment> getExperiments(Integer pageSize, Integer rowOffset,
                                    Integer projectId);

}
