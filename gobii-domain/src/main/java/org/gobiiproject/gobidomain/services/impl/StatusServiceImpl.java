package org.gobiiproject.gobidomain.services.impl;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobidomain.services.StatusService;
import org.gobiiproject.gobiidtomapping.DtoMapStatus;
import org.gobiiproject.gobiimodel.headerlesscontainer.StatusDTO;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by VCalaminos on 8/30/2017.
 */
public class StatusServiceImpl implements StatusService {

    Logger LOGGER = LoggerFactory.getLogger(StatusServiceImpl.class);

    @Autowired
    private DtoMapStatus dtoMapStatus = null;


    @Override
    public StatusDTO createStatus(StatusDTO statusDTO) throws GobiiDomainException {

        StatusDTO returnVal;

        returnVal = dtoMapStatus.createStatus(statusDTO);

        returnVal.getAllowedProcessTypes().add(GobiiProcessType.READ);
        returnVal.getAllowedProcessTypes().add(GobiiProcessType.UPDATE);


        return returnVal;
    }

    @Override
    public StatusDTO replaceStatus(Integer jobId, StatusDTO statusDTO) throws GobiiDomainException {

        StatusDTO returnVal;

        if (null == statusDTO.getJobId() || statusDTO.getJobId().equals(jobId)) {

            StatusDTO existingStatusDTO = dtoMapStatus.getStatusDetails(jobId);

            if (null != existingStatusDTO.getJobId() && existingStatusDTO.getJobId().equals(jobId)) {

                returnVal = dtoMapStatus.replaceStatus(jobId, statusDTO);
                returnVal.getAllowedProcessTypes().add(GobiiProcessType.READ);
                returnVal.getAllowedProcessTypes().add(GobiiProcessType.UPDATE);

            } else {

                throw new GobiiDomainException(GobiiStatusLevel.VALIDATION,
                        GobiiValidationStatusType.BAD_REQUEST,
                        "The jobId specified in the dto ("
                                + statusDTO.getJobId()
                                + ") does not match the jobId passed as a parameter "
                                + "("
                                + jobId
                                + ")");

            }

        } else {

            throw new GobiiDomainException(GobiiStatusLevel.VALIDATION,
                    GobiiValidationStatusType.BAD_REQUEST,
                    "The jobId specified in the dto ("
                            + statusDTO.getJobId()
                            + ") does not match the jobId passed as a parameter "
                            + "("
                            + jobId
                            + ")");

        }

        return returnVal;
    }

    @Override
    public List<StatusDTO> getStatuses() throws GobiiDomainException {

        List<StatusDTO> returnVal;

        returnVal = dtoMapStatus.getStatuses();

        for (StatusDTO currentStatusDTO : returnVal) {

            currentStatusDTO.getAllowedProcessTypes().add(GobiiProcessType.READ);
            currentStatusDTO.getAllowedProcessTypes().add(GobiiProcessType.UPDATE);
        }

        if (null == returnVal) {

            returnVal = new ArrayList<>();

        }

        return returnVal;

    }

    @Override
    public StatusDTO getStatusByJobId(Integer jobId) throws GobiiDomainException {

        StatusDTO returnVal;

        returnVal = dtoMapStatus.getStatusDetails(jobId);

        returnVal.getAllowedProcessTypes().add(GobiiProcessType.READ);
        returnVal.getAllowedProcessTypes().add(GobiiProcessType.UPDATE);


        if (null == returnVal) {
            throw new GobiiDomainException(GobiiStatusLevel.VALIDATION,
                    GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                    "The specified jobId ("
                            + jobId
                            + ") does not match an existing job");

        }

        return returnVal;

    }


}
