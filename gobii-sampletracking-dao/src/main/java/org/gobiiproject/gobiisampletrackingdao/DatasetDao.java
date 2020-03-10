package org.gobiiproject.gobiisampletrackingdao;


import org.gobiiproject.gobiimodel.entity.Dataset;
import org.gobiiproject.gobiimodel.entity.Project;

import java.util.List;
import java.util.Map;

public interface DatasetDao {

    List<Dataset> getDatasets(Integer pageSize, Integer rowOffset,
                              Integer datasetId, String datasetName,
                              Integer experimentId, String experimentName);

    Dataset getDatasetById(Integer datasetId);

}
