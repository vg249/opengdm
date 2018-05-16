package org.gobiiproject.gobiidtomapping.entity.noaudit.impl;

import org.apache.commons.lang.StringUtils;
import org.gobiiproject.gobiidtomapping.core.GobiiDtoMappingException;
import org.gobiiproject.gobiidtomapping.entity.auditable.impl.DtoMapDataSetImpl;
import org.gobiiproject.gobiidtomapping.entity.noaudit.DtoMapFlexQuery;
import org.gobiiproject.gobiimodel.cvnames.CvGroup;
import org.gobiiproject.gobiimodel.dto.entity.children.NameIdDTO;
import org.gobiiproject.gobiimodel.dto.entity.flex.VertexDTO;
import org.gobiiproject.gobiimodel.dto.entity.flex.VertexFilterDTO;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Phil on 4/22/2016.
 */
public class DtoMapFlexQueryImpl implements DtoMapFlexQuery {


    Logger LOGGER = LoggerFactory.getLogger(DtoMapDataSetImpl.class);


    public List<VertexDTO> getVertices() throws GobiiDtoMappingException {

        List<VertexDTO> returnVal = new ArrayList<>();

        // mock values
        returnVal.add(new VertexDTO(1,"Projects", GobiiEntityNameType.PROJECT,null));
        returnVal.add(new VertexDTO(2,"Experiment", GobiiEntityNameType.EXPERIMENT,null));
        returnVal.add(new VertexDTO(3,"Dataset Types", GobiiEntityNameType.CV, CvGroup.CVGROUP_ANALYSIS_TYPE.getCvGroupName()));
        returnVal.add(new VertexDTO(5,"Analyses", GobiiEntityNameType.CV,CvGroup.CVGROUP_ANALYSIS_TYPE.getCvGroupName()));
        returnVal.add(new VertexDTO(6,"Vendor Protocols", GobiiEntityNameType.VENDOR_PROTOCOL,null));
        returnVal.add(new VertexDTO(7,"Platforms", GobiiEntityNameType.PLATFORM,null));
        returnVal.add(new VertexDTO(8,"Mapset/Linkage group", GobiiEntityNameType.MAPSET,null));
        returnVal.add(new VertexDTO(9,"Germplasm", GobiiEntityNameType.GERMPLASM,null));
        returnVal.add(new VertexDTO(10,"Principle Investigators", GobiiEntityNameType.CONTACT,null));

//        try {
//
//
//        } catch (SQLException e) {
//            LOGGER.error("Gobii Mapping Error", e);
//            throw new GobiiDtoMappingException(e);
//        }

        return returnVal;

    }

    @Override
    public VertexFilterDTO getVertexValues(String jobId, VertexFilterDTO vertexFilterDTO) throws GobiiDtoMappingException {

        VertexFilterDTO returnVal = vertexFilterDTO;

        if( vertexFilterDTO.getDestinationVertexDTO() == null || vertexFilterDTO.getDestinationVertexDTO().getGobiiEntityNameType() == GobiiEntityNameType.UNKNOWN ) {
            throw new GobiiDtoMappingException("Unspecified destination vertex entity type");
        }

        GobiiEntityNameType gobiiEntityNameType = vertexFilterDTO.getDestinationVertexDTO().getGobiiEntityNameType();

        for(Integer idx=0; idx < 20; idx ++ ) {

            returnVal.getVertexValues().add(
                    new NameIdDTO(gobiiEntityNameType,
                            idx,
                            String.valueOf(gobiiEntityNameType).toLowerCase() + ": " + idx)
            );
        }

        return returnVal;

    } // getVertexValues()

    public VertexFilterDTO getVertexValuesCounts(String jobId, VertexFilterDTO vertexFilterDTO) throws GobiiDtoMappingException {

        VertexFilterDTO returnVal = vertexFilterDTO;

        vertexFilterDTO.setMarkerCount(new Random().nextInt());
        vertexFilterDTO.setSampleCount(new Random().nextInt());

        return returnVal;
    }

}
