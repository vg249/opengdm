/**
 * 
 */

package org.gobiiproject.gobiisampletrackingdao;

import java.util.List;

public interface ExperimentDao {

    List<Experiment> getExperiments(Integer page, Integer pageSize, Integer projectId);
    
}