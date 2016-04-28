package org.gobiiproject.gobiidtomapping.impl;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.resultset.access.RsAnalysisDao;
import org.gobiiproject.gobiidao.resultset.access.RsDataSetDao;
import org.gobiiproject.gobiidao.resultset.access.RsPlatformDao;
import org.gobiiproject.gobiidao.resultset.core.ParamExtractor;
import org.gobiiproject.gobiidao.resultset.core.ResultColumnApplicator;
import org.gobiiproject.gobiidtomapping.DtoMapAnalysis;
import org.gobiiproject.gobiidtomapping.DtoMapDataSet;
import org.gobiiproject.gobiidtomapping.DtoMapPlatform;
import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.dto.DtoMetaData;
import org.gobiiproject.gobiimodel.dto.container.AnalysisDTO;
import org.gobiiproject.gobiimodel.dto.container.DataSetDTO;
import org.gobiiproject.gobiimodel.dto.container.PlatformDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by Phil on 4/21/2016.
 */
public class DtoMapPlatformImpl implements DtoMapPlatform {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapPlatformImpl.class);


    @Autowired
    private RsPlatformDao rsPlatformDao;

    @Override
    public PlatformDTO getPlatformDetails(PlatformDTO platformDTO) throws GobiiDtoMappingException {

        PlatformDTO returnVal = platformDTO;

        try {

            ResultSet resultSet = rsPlatformDao.getPlatformDetailsByPlatformId(platformDTO.getPlatformId());

            if (resultSet.next()) {

                // apply dataset values
                ResultColumnApplicator.applyColumnValues(resultSet, returnVal);


            } // if result set has a row

        } catch (GobiiDaoException e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Error mapping result set to DTO", e);
        } catch (SQLException e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error("Error mapping result set to DTO", e);
        }

        return returnVal;

    } // getPlatformDetails()


//    @Transactional
//    @Override
//    public DataSetDTO getDataSetDetails(DataSetDTO dataSetDTO) throws GobiiDtoMappingException {
//
//        DataSetDTO returnVal = dataSetDTO;
//
//        try {
//
//            ResultSet resultSet = rsDataSetDao.getDataSetDetailsByDataSetId(dataSetDTO.getDatasetId());
//
//            if (resultSet.next()) {
//
//                // apply dataset values
//                ResultColumnApplicator.applyColumnValues(resultSet, returnVal);
//
//
//                // create calling analysis from annotations
//                if (null != returnVal.getCallingAnalysisId()) {
//
//                    ResultSet callingAnalysisResultSet =
//                            rsAnalysisDao.getAnalysisDetailsByAnalysisId(returnVal.getCallingAnalysisId());
//                    if (callingAnalysisResultSet.next()) {
//                        AnalysisDTO callingAnalysisDTO = new AnalysisDTO();
//                        ResultColumnApplicator.applyColumnValues(callingAnalysisResultSet, callingAnalysisDTO);
//                    }
//                }
//
//                // create analyses
//                for (Integer currentAnalysisId : returnVal.getAnalysesIds()) {
//                    ResultSet currentAnalysisResultSet = rsAnalysisDao.getAnalysisDetailsByAnalysisId(currentAnalysisId);
//                    if (currentAnalysisResultSet.next()) {
//                        AnalysisDTO currentAnalysisDTO = new AnalysisDTO();
//                        ResultColumnApplicator.applyColumnValues(currentAnalysisResultSet, currentAnalysisDTO);
//                        returnVal.getAnalyses().add(currentAnalysisDTO);
//                    }
//                }
//
//            } // iterate resultSet
//
//        } catch (GobiiDaoException e) {
//            returnVal.getDtoHeaderResponse().addException(e);
//            LOGGER.error("Error mapping result set to DTO", e);
//        } catch (SQLException e) {
//            returnVal.getDtoHeaderResponse().addException(e);
//            LOGGER.error("Error mapping result set to DTO", e);
//        }
//
//        return returnVal;
//
//    }
//
//    @Override
//    public DataSetDTO createDataset(DataSetDTO dataSetDTO) throws GobiiDtoMappingException {
//        DataSetDTO returnVal = dataSetDTO;
//
//        try {
//
//            AnalysisDTO analysisDTOCalling = returnVal.getCallingAnalysis();
//            if(DtoMetaData.ProcessType.CREATE == analysisDTOCalling.getProcessType()) {
//                dtoMapAnalysis.createAnalysis(analysisDTOCalling);
//            }
//
//            returnVal.setCallingAnalysisId(analysisDTOCalling.getAnalysisId());
//
//            for(AnalysisDTO currentAnalysisDTO : returnVal.getAnalyses() ) {
//                dtoMapAnalysis.createAnalysis(currentAnalysisDTO);
//                returnVal.getAnalysesIds().add(currentAnalysisDTO.getAnalysisId());
//            }
//
//            Map<String, Object> parameters = ParamExtractor.makeParamVals(dataSetDTO);
//            Integer datasetId = rsDataSetDao.createDataset(parameters);
//            returnVal.setDatasetId(datasetId);
//
//        } catch (GobiiDaoException e) {
//            returnVal.getDtoHeaderResponse().addException(e);
//            LOGGER.error("Error mapping result set to DTO", e);
//        }
//
//        return returnVal;
//    }

}
