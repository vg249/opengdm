package org.gobiiproject.gobidomain.services.gdmv3;

import java.util.ArrayList;
import java.util.List;

import org.gobiiproject.gobiimodel.dto.gdmv3.AnalysisDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;
import org.gobiiproject.gobiimodel.entity.Analysis;
import org.gobiiproject.gobiimodel.modelmapper.ModelMapper;
import org.gobiiproject.gobiisampletrackingdao.AnalysisDao;
import org.springframework.beans.factory.annotation.Autowired;

public class AnalysisServiceImpl implements AnalysisService {

    @Autowired
    private AnalysisDao analysisDao;

    public PagedResult<AnalysisDTO> getAnalyses(Integer page, Integer pageSize) {
        List<Analysis> analyses = analysisDao.getAnalyses(page * pageSize, pageSize);
        List<AnalysisDTO> dtos = new ArrayList<>();

        analyses.forEach( (analysis) -> {
            AnalysisDTO dto = new AnalysisDTO();
            ModelMapper.mapEntityToDto(analysis, dto);
            dtos.add(dto);
        });

        PagedResult<AnalysisDTO> result = new PagedResult<>();
        result.setCurrentPageNum(page);
        result.setCurrentPageSize(analyses.size());
        result.setResult(dtos);
        return result;
	}
}