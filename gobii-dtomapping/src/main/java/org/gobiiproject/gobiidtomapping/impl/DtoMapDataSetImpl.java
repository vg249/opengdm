package org.gobiiproject.gobiidtomapping.impl;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.resultset.access.RsAnalysisDao;
import org.gobiiproject.gobiidao.resultset.access.RsDataSetDao;
import org.gobiiproject.gobiidao.resultset.core.ResultColumnApplicator;
import org.gobiiproject.gobiidtomapping.DtoMapDataSet;
import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.dto.container.AnalysisDTO;
import org.gobiiproject.gobiimodel.dto.container.DataSetDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Phil on 4/21/2016.
 */
public class DtoMapDataSetImpl implements DtoMapDataSet {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapDataSetImpl.class);


    @Autowired
    private RsDataSetDao rsDataSetDao;

    @Autowired
    private RsAnalysisDao rsAnalysisDao;

    @Transactional
    @Override
    public DataSetDTO getDataSetDetails(DataSetDTO dataSetDTO) throws GobiiDtoMappingException {

        DataSetDTO returnVal = dataSetDTO;

        try {

            ResultSet resultSet = rsDataSetDao.getDataSetDetailsByDataSetId(dataSetDTO.getDatasetId());

            if (resultSet.next()) {

                // apply dataset values
                ResultColumnApplicator.applyColumnValues(resultSet, returnVal);


                // create calling analysis from annotations
                if (null != returnVal.getCallingAnalysisId()) {

                    ResultSet callingAnalysisResultSet =
                            rsAnalysisDao.getAnalysisDetailsByAnalysisId(returnVal.getCallingAnalysisId());
                    if (callingAnalysisResultSet.next()) {
                        AnalysisDTO callingAnalysisDTO = new AnalysisDTO();
                        ResultColumnApplicator.applyColumnValues(callingAnalysisResultSet, callingAnalysisDTO);
                    }
                }

                // create analyses
                for (Integer currentAnalysisId : returnVal.getAnalysesIds()) {
                    ResultSet currentAnalysisResultSet = rsAnalysisDao.getAnalysisDetailsByAnalysisId(currentAnalysisId);
                    if (currentAnalysisResultSet.next()) {
                        AnalysisDTO currentAnalysisDTO = new AnalysisDTO();
                        ResultColumnApplicator.applyColumnValues(currentAnalysisResultSet, currentAnalysisDTO);
                        returnVal.getAnalyses().add(currentAnalysisDTO);
                    }
                }

            } // iterate resultSet

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
