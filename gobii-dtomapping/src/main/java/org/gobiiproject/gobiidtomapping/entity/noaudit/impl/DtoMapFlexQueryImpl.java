package org.gobiiproject.gobiidtomapping.entity.noaudit.impl;

import org.gobiiproject.gobiidtomapping.core.GobiiDtoMappingException;
import org.gobiiproject.gobiidtomapping.entity.auditable.impl.DtoMapDataSetImpl;
import org.gobiiproject.gobiidtomapping.entity.noaudit.DtoMapFlexQuery;
import org.gobiiproject.gobiimodel.dto.entity.flex.VertexDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

/**
 * Created by Phil on 4/22/2016.
 */
public class DtoMapFlexQueryImpl implements DtoMapFlexQuery {


    Logger LOGGER = LoggerFactory.getLogger(DtoMapDataSetImpl.class);


    public VertexDTO getVertices() throws GobiiDtoMappingException {

        VertexDTO returnVal = new VertexDTO();

//        try {
//
//
//        } catch (SQLException e) {
//            LOGGER.error("Gobii Mapping Error", e);
//            throw new GobiiDtoMappingException(e);
//        }

        return returnVal;

    }

}
