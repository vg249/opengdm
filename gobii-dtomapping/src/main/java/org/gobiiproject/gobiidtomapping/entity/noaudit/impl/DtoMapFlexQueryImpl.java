package org.gobiiproject.gobiidtomapping.entity.noaudit.impl;

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
import java.util.concurrent.ThreadLocalRandom;

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
        returnVal.add(new VertexDTO(3,"Dataset Types", GobiiEntityNameType.CV, CvGroup.ANALYSIS_TYPE.getCvGroupName()));
        returnVal.add(new VertexDTO(5,"Analyses", GobiiEntityNameType.CV,CvGroup.ANALYSIS_TYPE.getCvGroupName()));
        returnVal.add(new VertexDTO(6,"Vendor Protocols", GobiiEntityNameType.VENDOR_PROTOCOL,null));
        returnVal.add(new VertexDTO(7,"Platforms", GobiiEntityNameType.PLATFORM,null));
        returnVal.add(new VertexDTO(8,"Mapset/Linkage group", GobiiEntityNameType.MAPSET,null));
        returnVal.add(new VertexDTO(9,"Germplasm", GobiiEntityNameType.GERMPLASM,null));
        returnVal.add(new VertexDTO(10,"Principle Investigators", GobiiEntityNameType.CONTACT,null));

        return returnVal;

    }

    @Override
    public VertexFilterDTO getVertexValues(String jobId, VertexFilterDTO vertexFilterDTO) throws GobiiDtoMappingException {

        VertexFilterDTO returnVal = vertexFilterDTO;

//        if( vertexFilterDTO.getDestinationVertexDTO() == null || vertexFilterDTO.getDestinationVertexDTO().getEntityType().isEmpty() ) {
//            throw new GobiiDtoMappingException("Unspecified destination vertex entity type");
//        }
//

        for(Integer idx=1; idx <= 20; idx ++ ) {

            returnVal.getVertexValues().add(
                    new NameIdDTO(GobiiEntityNameType.valueOf(vertexFilterDTO.getDestinationVertexDTO().getEntityType().name()),
                            idx,
                            vertexFilterDTO.getDestinationVertexDTO().getEntityType().name() + ": " + idx)
            );
        }

        return returnVal;

    } // getVertexValues()

    public VertexFilterDTO getVertexValuesCounts(String jobId, VertexFilterDTO vertexFilterDTO) throws GobiiDtoMappingException {

        VertexFilterDTO returnVal = vertexFilterDTO;


        vertexFilterDTO.setMarkerCount(ThreadLocalRandom.current().nextInt(1,1000));
        vertexFilterDTO.setSampleCount(ThreadLocalRandom.current().nextInt(1,1000));

        return returnVal;
    }

}
