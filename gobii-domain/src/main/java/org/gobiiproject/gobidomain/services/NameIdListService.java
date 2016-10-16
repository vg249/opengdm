// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-24
// ************************************************************************
package org.gobiiproject.gobidomain.services;

import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.container.NameIdDTO;
import org.gobiiproject.gobiimodel.dto.container.NameIdListDTO;

import java.util.List;

/**
 * Created by Phil on 3/24/2016.
 */
public interface NameIdListService {

    NameIdListDTO getNameIdList(NameIdListDTO nameIdListDTO);

    List<NameIdDTO> getNameIdList(String entity, String filterType, String filterValue) throws GobiiException;
}
