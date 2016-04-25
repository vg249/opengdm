package org.gobiiproject.gobiidtomapping.impl;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.resultset.access.RsAnalysisDao;
import org.gobiiproject.gobiidao.resultset.core.ResultColumnApplicator;
import org.gobiiproject.gobiidtomapping.DtoMapAnalysis;
import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.dto.container.AnalysisDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Phil on 4/22/2016.
 */
public class DtoMapAnalysisImpl implements DtoMapAnalysis {


    Logger LOGGER = LoggerFactory.getLogger(DtoMapDataSetImpl.class);


    @Autowired
    private RsAnalysisDao rsAnalysisDao;

    public AnalysisDTO getAnalysisDetails(AnalysisDTO analysisDTO) throws GobiiDtoMappingException {

        AnalysisDTO returnVal = analysisDTO;

        try {

            ResultSet resultSet = rsAnalysisDao.getAnalysisDetailsByAnalysisId(analysisDTO.getAnalysisId());

            if (resultSet.next()) {
                ResultColumnApplicator.applyColumnValues(resultSet,returnVal);
            }

        } catch (GobiiDaoException e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Error mapping result set to DTO", e);
        } catch (SQLException e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Error mapping result set to DTO", e);
        }

        return returnVal;

    }
}
