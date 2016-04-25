package org.gobiiproject.gobiidtomapping.impl;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.resultset.access.RsAnalysisDao;
import org.gobiiproject.gobiidao.resultset.access.RsDataSetDao;
import org.gobiiproject.gobiidao.resultset.core.ResultColumnApplicator;
import org.gobiiproject.gobiidtomapping.DtoMapDataSet;
import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiidtomapping.makers.DtoFieldMapperAnalysis;
import org.gobiiproject.gobiimodel.dto.container.AnalysisDTO;
import org.gobiiproject.gobiimodel.dto.container.DataSetDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Array;
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
                    if(callingAnalysisResultSet.next()) {
                        AnalysisDTO analysisDTO = DtoFieldMapperAnalysis.make(callingAnalysisResultSet);
                        returnVal.setCallingAnalysis(analysisDTO);
                    }
                }

                // create analyses
                for (Integer currentAnalysisId : returnVal.getAnalysesIds()) {
                    ResultSet analysisResultSet = rsAnalysisDao.getAnalysisDetailsByAnalysisId(currentAnalysisId);
                    if (analysisResultSet.next()) {
                        AnalysisDTO analysisDTO = DtoFieldMapperAnalysis.make(analysisResultSet);
                        returnVal.getAnalyses().add(analysisDTO);
                    }
                }

//                returnVal.setDatasetId(resultSet.getInt("dataset_id"));
//                returnVal.setExperimentId(resultSet.getInt("experiment_id"));
//                returnVal.setDataTable(resultSet.getString("data_table"));
//                returnVal.setDataFile(resultSet.getString("data_file"));
//                returnVal.setQualityTable(resultSet.getString("quality_table"));
//                returnVal.setQualityFile(resultSet.getString("quality_file"));
//                returnVal.setCreatedBy(resultSet.getString("created_by"));
//                returnVal.setCreatedDate(resultSet.getDate("created_date"));
//                returnVal.setModifiedBy(resultSet.getString("modified_by"));
//                returnVal.setModifiedDate(resultSet.getDate("modified_date"));
//                returnVal.setStatus(resultSet.getInt("status"));
//
//                Integer callingAnalysisId = resultSet.getInt("callinganalysis_id");
//                if (!resultSet.wasNull()) {
//
//                    AnalysisDTO analysisDTO = DtoFieldMapperAnalysis.make(resultSet);
//                    returnVal.setCallingAnalysis(analysisDTO);
//                }
//
//                Array sqlArray = resultSet.getArray("analyses");
//                if (!resultSet.wasNull()) {
//                    Integer[] anayses = (Integer[]) sqlArray.getArray();
//                    for (int idx = 0; idx < anayses.length; idx++) {
//                        Integer currentAnalysisId = anayses[idx];
//                        ResultSet analysisResultSet = rsAnalysisDao.getAnalysisDetailsByAnalysisId(currentAnalysisId);
//                        if( analysisResultSet.next() ) {
//                            AnalysisDTO analysisDTO = DtoFieldMapperAnalysis.make(analysisResultSet);
//                            returnVal.getAnalyses().add(analysisDTO);
//                        }
//                    }
//                }

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
