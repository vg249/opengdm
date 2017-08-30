package org.gobiiproject.gobidomain.services.impl;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobidomain.services.StatusService;
import org.gobiiproject.gobiimodel.headerlesscontainer.StatusDTO;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by VCalaminos on 8/30/2017.
 */
public class StatusServiceImpl implements StatusService {

    @Override
    public StatusDTO createStatus(StatusDTO statusDTO) throws GobiiDomainException {

        StatusDTO returnVal;

        returnVal = statusDTO;

        returnVal.getAllowedProcessTypes().add(GobiiProcessType.READ);
        returnVal.getAllowedProcessTypes().add(GobiiProcessType.UPDATE);


        return returnVal;
    }

    @Override
    public StatusDTO replaceStatus(Integer statusId, StatusDTO statusDTO) throws GobiiDomainException {

        StatusDTO returnVal;

        returnVal = statusDTO;
        returnVal.getAllowedProcessTypes().add(GobiiProcessType.READ);
        returnVal.getAllowedProcessTypes().add(GobiiProcessType.UPDATE);


        return returnVal;
    }

    @Override
    public List<StatusDTO> getStatuses() throws GobiiDomainException {

        List<StatusDTO> returnVal = new ArrayList<>();

        for (int i = 0; i<5; i++) {

            StatusDTO newStatusDto = new StatusDTO();
            newStatusDto.setJobId(i);
            newStatusDto.setDataset(i);
            newStatusDto.setMessages("dummy_" + i);
            newStatusDto.setProcessStatus(StatusDTO.CV_PROGRESSSTATUS_COMPLETED);
            newStatusDto.setLoadType(StatusDTO.CV_LOADTYPE_MARKER);
            newStatusDto.setExtractType(StatusDTO.CV_EXTRACTTYPE_HAPMAP);
            returnVal.add(newStatusDto);
        }

        for (StatusDTO currentStatusDTO : returnVal) {

            currentStatusDTO.getAllowedProcessTypes().add(GobiiProcessType.READ);
            currentStatusDTO.getAllowedProcessTypes().add(GobiiProcessType.UPDATE);
        }

        return returnVal;

    }

    @Override
    public StatusDTO getStatusById(Integer statusId) throws GobiiDomainException {

        StatusDTO returnVal;

        StatusDTO newStatusDto = new StatusDTO();
        newStatusDto.setJobId(statusId);
        newStatusDto.setDataset(1);
        newStatusDto.setMessages("dummy");
        newStatusDto.setProcessStatus(StatusDTO.CV_PROGRESSSTATUS_COMPLETED);
        newStatusDto.setLoadType(StatusDTO.CV_LOADTYPE_MARKER);
        newStatusDto.setExtractType(StatusDTO.CV_EXTRACTTYPE_HAPMAP);

        returnVal = newStatusDto;

        returnVal.getAllowedProcessTypes().add(GobiiProcessType.READ);
        returnVal.getAllowedProcessTypes().add(GobiiProcessType.UPDATE);

        return returnVal;

    }


}
