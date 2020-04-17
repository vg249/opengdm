package org.gobiiproject.gobidomain.services.gdmv3;

import java.util.List;

import org.gobiiproject.gobiimodel.dto.gdmv3.AnalysisDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.AnalysisTypeDTO;
import org.gobiiproject.gobiimodel.dto.request.AnalysisRequest;
import org.gobiiproject.gobiimodel.dto.request.AnalysisTypeRequest;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;

public interface AnalysisService {

	PagedResult<AnalysisDTO> getAnalyses(Integer page, Integer pageSize) throws Exception;

	AnalysisDTO createAnalysis(AnalysisDTO analysisRequest, String creatorId) throws Exception;

	AnalysisTypeDTO createAnalysisType(AnalysisTypeRequest analysisTypeRequest, String creatorId) throws Exception;

	PagedResult<AnalysisTypeDTO> getAnalysisTypes(Integer page, Integer pageSize);

	AnalysisDTO updateAnalysis(Integer eq, AnalysisDTO any, String eq2) throws Exception;

	

}