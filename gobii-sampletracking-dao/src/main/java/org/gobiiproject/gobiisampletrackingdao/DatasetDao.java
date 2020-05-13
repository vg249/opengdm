package org.gobiiproject.gobiisampletrackingdao;


import java.util.List;

import org.gobiiproject.gobiimodel.entity.Dataset;

public interface DatasetDao {

    List<Dataset> getDatasets(Integer pageSize, Integer rowOffset,
                              Integer datasetId, String datasetName,
                              Integer experimentId, String experimentName);

    Dataset getDatasetById(Integer datasetId);

    List<Object[]> getDatasetsWithAnalysesAndCounts(Integer pageSize, Integer rowOffset,
                                                    Integer datasetId, String datasetName,
                                                    Integer experimentId, String experimentName);

    Dataset createDataset(Dataset datasetToCreate);
}
