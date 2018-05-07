package org.gobiiproject.gobidomain.services;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.dto.entity.children.NameIdDTO;
import org.gobiiproject.gobiimodel.dto.entity.flex.VertexDTO;
import org.gobiiproject.gobiimodel.dto.system.EntityStatsDTO;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;

import java.util.List;

/**
 * Created by Phil on 4/21/2016.
 * Modified by Yanii on 1/27/2017
 */
public interface FlexQueryService {

    VertexDTO getVertices() throws GobiiDomainException;
}
