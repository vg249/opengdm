package org.gobiiproject.gobidomain.services;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.MarkerBrapiDTO;

import java.util.List;

/**
 * Created by VCalaminos on 7/7/2019.
 */
public interface MarkerBrapiService {

    List<MarkerBrapiDTO> getMarkers(Integer pageToken, Integer pageSize, MarkerBrapiDTO markerBrapiDTOFilter) throws GobiiDomainException;

}
