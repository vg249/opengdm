package org.gobiiproject.gobidomain.services.impl;

import org.gobiiproject.gobidomain.services.AnalysisService;
import org.gobiiproject.gobidomain.services.DataSetService;
import org.gobiiproject.gobiidtomapping.DtoMapAnalysis;
import org.gobiiproject.gobiidtomapping.DtoMapDataSet;
import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.dto.container.AnalysisDTO;
import org.gobiiproject.gobiimodel.dto.container.DataSetDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Phil on 4/21/2016.
 */
public class AnalysisServiceImpl implements AnalysisService {

    Logger LOGGER = LoggerFactory.getLogger(AnalysisServiceImpl.class);


    @Autowired
    DtoMapAnalysis dtoMapAnalysis = null;

    public AnalysisDTO getAnalysisDetails(AnalysisDTO analysisDTO) {

    	AnalysisDTO returnVal = new AnalysisDTO();

        try {
            returnVal = dtoMapAnalysis.getAnalysisDetails(analysisDTO);

        } catch (GobiiDtoMappingException e) {

            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error(e.getMessage());
        }

        return returnVal;
    }
}
