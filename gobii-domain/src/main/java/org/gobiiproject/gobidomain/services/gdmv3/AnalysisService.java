package org.gobiiproject.gobidomain.services.gdmv3;

import javax.validation.Valid;

import org.gobiiproject.gobiimodel.dto.gdmv3.AnalysisDTO;
import org.gobiiproject.gobiimodel.dto.request.AnalysisRequest;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;

public interface AnalysisService {

	PagedResult<AnalysisDTO> getAnalyses(Integer page, Integer pageSize) throws Exception;

	AnalysisDTO createAnalysis(AnalysisRequest analysisRequest, String user) throws Exception;

	

}