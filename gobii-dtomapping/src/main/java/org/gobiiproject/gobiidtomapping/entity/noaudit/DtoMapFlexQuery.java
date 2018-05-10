package org.gobiiproject.gobiidtomapping.entity.noaudit;

import org.gobiiproject.gobiidtomapping.core.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.dto.entity.flex.VertexDTO;

import java.util.List;

/**
 * Created by Phil on 4/21/2016.
 */
public interface DtoMapFlexQuery {

    //DtoMap methods
    List<VertexDTO> getVertices() throws GobiiDtoMappingException;



}
