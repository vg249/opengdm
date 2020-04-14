package org.gobiiproject.gobidomain.services.gdmv3;

import org.gobiiproject.gobiimodel.dto.gdmv3.AnalysisDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;

public interface AnalysisService {

	PagedResult<AnalysisDTO> getAnalyses(Integer page, Integer pageSize);

	

}