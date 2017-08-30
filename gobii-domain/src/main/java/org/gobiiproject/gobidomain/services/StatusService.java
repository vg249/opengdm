package org.gobiiproject.gobidomain.services;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.headerlesscontainer.StatusDTO;

import java.util.List;

/**
 * Created by VCalaminos on 8/30/2017.
 */
public interface StatusService {

    StatusDTO createStatus(StatusDTO statusDTO) throws GobiiDomainException;
    StatusDTO replaceStatus(Integer statusId, StatusDTO statusDTO) throws GobiiDomainException;
    List<StatusDTO> getStatuses() throws GobiiDomainException;
    StatusDTO getStatusById(Integer statusId) throws GobiiDomainException;

}
