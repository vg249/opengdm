package org.gobiiproject.gobiidtomapping;

import org.gobiiproject.gobiimodel.headerlesscontainer.StatusDTO;

import java.util.List;

/**
 * Created by VCalaminos on 9/4/2017.
 */
public interface DtoMapStatus {

    List<StatusDTO> getStatuses() throws GobiiDtoMappingException;
    StatusDTO getStatusDetails(Integer jobId) throws GobiiDtoMappingException;
    StatusDTO createStatus(StatusDTO statusDTO) throws GobiiDtoMappingException;
    StatusDTO replaceStatus(Integer jobId, StatusDTO statusDTO) throws GobiiDtoMappingException;

}
