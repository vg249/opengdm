package org.gobiiproject.gobiidtomapping.entity.noaudit.impl;

import org.gobiiproject.gobiidtomapping.core.GobiiDtoMappingException;
import org.gobiiproject.gobiidtomapping.entity.auditable.impl.DtoMapDataSetImpl;
import org.gobiiproject.gobiidtomapping.entity.noaudit.DtoMapFlexQuery;
import org.gobiiproject.gobiimodel.cvnames.CvGroup;
import org.gobiiproject.gobiimodel.dto.entity.flex.Vertex;
import org.gobiiproject.gobiimodel.dto.entity.flex.VertexDTO;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;
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

        // mock values
        returnVal.getVertices().add(new Vertex(0,"Projects", GobiiEntityNameType.PROJECT,null));
        returnVal.getVertices().add(new Vertex(1,"Principle Investigators", GobiiEntityNameType.CONTACT,null));
        returnVal.getVertices().add(new Vertex(2,"Experiment", GobiiEntityNameType.EXPERIMENT,null));
        returnVal.getVertices().add(new Vertex(3,"Dataset Types", GobiiEntityNameType.CV, CvGroup.CVGROUP_ANALYSIS_TYPE.getCvGroupName()));
        returnVal.getVertices().add(new Vertex(5,"Analyses", GobiiEntityNameType.CV,CvGroup.CVGROUP_ANALYSIS_TYPE.getCvGroupName()));
        returnVal.getVertices().add(new Vertex(6,"Vendor Protocols", GobiiEntityNameType.VENDOR_PROTOCOL,null));
        returnVal.getVertices().add(new Vertex(7,"Platforms", GobiiEntityNameType.PLATFORM,null));
        returnVal.getVertices().add(new Vertex(8,"Mapset/Linkage group", GobiiEntityNameType.MAPSET,null));
        returnVal.getVertices().add(new Vertex(9,"Germplasm", GobiiEntityNameType.GERMPLASM,null));

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
