package org.gobiiproject.gobidomain.services.gdmv3;

import org.gobiiproject.gobiimodel.dto.gdmv3.DatasetDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.DatasetRequestDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;

public interface DatasetService {

	DatasetDTO createDataset(DatasetRequestDTO request, String user) throws Exception;

	PagedResult<DatasetDTO> getDatasets(Integer page, Integer pageSize, Integer experimentId, Integer datasetTypeId);

}