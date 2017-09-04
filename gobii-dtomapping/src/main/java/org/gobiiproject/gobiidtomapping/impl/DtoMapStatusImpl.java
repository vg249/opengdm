package org.gobiiproject.gobiidtomapping.impl;

import org.gobiiproject.gobiidao.resultset.access.RsStatusDao;
import org.gobiiproject.gobiidao.resultset.core.ParamExtractor;
import org.gobiiproject.gobiidao.resultset.core.ResultColumnApplicator;
import org.gobiiproject.gobiidtomapping.DtoMapStatus;
import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.headerlesscontainer.StatusDTO;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by VCalaminos on 9/4/2017.
 */
public class DtoMapStatusImpl implements DtoMapStatus{

    Logger LOGGER = LoggerFactory.getLogger(DtoMapStatusImpl.class);

    @Autowired
    private RsStatusDao rsStatusDao;

    @Override
    public List<StatusDTO> getStatuses() throws GobiiDtoMappingException {

        List<StatusDTO> returnVal = new ArrayList<>();

        try {

            ResultSet resultSet = rsStatusDao.getStatuses();

            while (resultSet.next()) {

                StatusDTO currentStatusDTO = new StatusDTO();
                ResultColumnApplicator.applyColumnValues(resultSet, currentStatusDTO);

                returnVal.add(currentStatusDTO);

            }


        } catch (SQLException e) {
            LOGGER.error("GObii Mapping Error", e);
            throw new GobiiDtoMappingException(e);
        }


        return returnVal;
    }

    @Override
    public StatusDTO getStatusDetails(Integer jobId) throws GobiiDtoMappingException {

        StatusDTO returnVal = new StatusDTO();


        ResultSet resultSet = rsStatusDao.getStatusDetailsForJobId(jobId);

        boolean retrievedOneRecord = false;

        try {

            while (resultSet.next()) {

                if (true == retrievedOneRecord) {

                    throw (new GobiiDtoMappingException(GobiiStatusLevel.ERROR,
                            GobiiValidationStatusType.VALIDATION_NOT_UNIQUE,
                            "There are more than one job records for job id: " + jobId));
                }

                retrievedOneRecord = true;

                ResultColumnApplicator.applyColumnValues(resultSet, returnVal);

            }


        } catch (SQLException e) {
            LOGGER.error("Gobii Mapping Error", e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;

    }

    @Override
    public StatusDTO createStatus(StatusDTO statusDTO) throws GobiiDtoMappingException {

        StatusDTO returnVal = statusDTO;

        Map<String, Object> parameters = ParamExtractor.makeParamVals(returnVal);
        Integer jobId = rsStatusDao.createStatus(parameters);
        returnVal.setJobId(jobId);

        return returnVal;

    }

    @Override
    public StatusDTO replaceStatus(Integer jobId, StatusDTO statusDTO) throws GobiiDtoMappingException {

        StatusDTO returnVal = statusDTO;

        Map<String, Object> parameters = ParamExtractor.makeParamVals(returnVal);
        parameters.put("jobId", jobId);
        rsStatusDao.updateStatus(parameters);

        return returnVal;

    }

}
