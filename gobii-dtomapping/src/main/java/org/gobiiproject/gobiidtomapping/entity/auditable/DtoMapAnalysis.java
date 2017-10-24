package org.gobiiproject.gobiidtomapping.auditable;

import org.gobiiproject.gobiidtomapping.entity.auditable.DtoMap;
import org.gobiiproject.gobiidtomapping.core.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.headerlesscontainer.AnalysisDTO;

import java.util.List;

/**
 * Created by Phil on 4/21/2016.
 */
public interface DtoMapAnalysis extends DtoMap<AnalysisDTO> {

    //DtoMap methods
    AnalysisDTO create(AnalysisDTO  analysisDTO) throws GobiiDtoMappingException;
    AnalysisDTO replace(Integer analysisId, AnalysisDTO analysisDTO) throws GobiiDtoMappingException;
    AnalysisDTO get(Integer  analysisId) throws GobiiDtoMappingException;
    List<AnalysisDTO> getList() throws GobiiDtoMappingException;




}
