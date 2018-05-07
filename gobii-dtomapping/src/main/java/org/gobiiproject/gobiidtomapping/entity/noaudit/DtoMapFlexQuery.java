package org.gobiiproject.gobiidtomapping.entity.noaudit;

import org.gobiiproject.gobiidtomapping.core.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.dto.entity.flex.VertexDTO;

/**
 * Created by Phil on 4/21/2016.
 */
public interface DtoMapFlexQuery {

    //DtoMap methods
    VertexDTO getVertices() throws GobiiDtoMappingException;



}
