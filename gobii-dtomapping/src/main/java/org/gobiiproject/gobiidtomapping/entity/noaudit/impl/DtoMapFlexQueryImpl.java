package org.gobiiproject.gobiidtomapping.entity.noaudit.impl;

import org.gobiiproject.gobiidtomapping.core.GobiiDtoMappingException;
import org.gobiiproject.gobiidtomapping.entity.auditable.impl.DtoMapDataSetImpl;
import org.gobiiproject.gobiidtomapping.entity.noaudit.DtoMapFlexQuery;
import org.gobiiproject.gobiimodel.cvnames.CvGroup;
import org.gobiiproject.gobiimodel.dto.entity.children.NameIdDTO;
import org.gobiiproject.gobiimodel.dto.entity.flex.VertexDTO;
import org.gobiiproject.gobiimodel.dto.entity.flex.VertexFilterDTO;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;
import org.gobiiproject.gobiimodel.types.GobiiEntitySubType;
import org.gobiiproject.gobiimodel.types.GobiiVertexType;
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

        // mock vertices
        returnVal.add(new VertexDTO(1,
                GobiiVertexType.ENTITY,
                "Projects",
                GobiiEntityNameType.PROJECT,
                GobiiEntitySubType.UNKNOWN,
                CvGroup.UNKNOWN,
                null));

        returnVal.add(new VertexDTO(2,
                GobiiVertexType.ENTITY,
                "Experiment",
                GobiiEntityNameType.EXPERIMENT,
                GobiiEntitySubType.UNKNOWN,
                CvGroup.UNKNOWN,
                null));

        returnVal.add(new VertexDTO(3,
                GobiiVertexType.CVGROUP,
                "Dataset Type",
                GobiiEntityNameType.CV,
                GobiiEntitySubType.UNKNOWN,
                CvGroup.ANALYSIS_TYPE,
                null));

        returnVal.add(new VertexDTO(4,
                GobiiVertexType.CVGROUP,
                "Mapset Type",
                GobiiEntityNameType.CV,
                GobiiEntitySubType.UNKNOWN,
                CvGroup.MAPSET_TYPE,
                null));

        returnVal.add(new VertexDTO(5,
                GobiiVertexType.CVGROUP,
                "Calling Analysis",
                GobiiEntityNameType.CV,
                GobiiEntitySubType.UNKNOWN,
                CvGroup.ANALYSIS_TYPE,
                null));

        returnVal.add(new VertexDTO(6,
                GobiiVertexType.ENTITY,
                "Analysis",
                GobiiEntityNameType.ANALYSIS,
                GobiiEntitySubType.UNKNOWN,
                CvGroup.UNKNOWN,
                null));

        returnVal.add(new VertexDTO(7,
                GobiiVertexType.ENTITY,
                "Vendor Protocols",
                GobiiEntityNameType.VENDOR_PROTOCOL,
                GobiiEntitySubType.UNKNOWN,
                CvGroup.UNKNOWN,
                null));


        returnVal.add(new VertexDTO(8,
                GobiiVertexType.ENTITY,
                "Platforms",
                GobiiEntityNameType.PLATFORM,
                GobiiEntitySubType.UNKNOWN,
                CvGroup.UNKNOWN,
                null));

        returnVal.add(new VertexDTO(9,
                GobiiVertexType.ENTITY,
                "Mapset",
                GobiiEntityNameType.MAPSET,
                GobiiEntitySubType.UNKNOWN,
                CvGroup.UNKNOWN,
                null));


        returnVal.add(new VertexDTO(10,
                GobiiVertexType.ENTITY,
                "Linkage Group",
                GobiiEntityNameType.LINKAGE_GROUP,
                GobiiEntitySubType.UNKNOWN,
                CvGroup.UNKNOWN,
                null));


        returnVal.add(new VertexDTO(11,
                GobiiVertexType.SUBENTITY,
                "Principle Investigators",
                GobiiEntityNameType.CONTACT,
                GobiiEntitySubType.CONTACT_PRINCIPLE_INVESTIGATOR,
                CvGroup.UNKNOWN,
                null));


        returnVal.add(new VertexDTO(12,
                GobiiVertexType.CVTERM,
                "Germplasm Species",
                GobiiEntityNameType.GERMPLASM,
                GobiiEntitySubType.UNKNOWN,
                CvGroup.GERMPLASM_PROP,
                "germplasm_subsp"));

        returnVal.add(new VertexDTO(13,
                GobiiVertexType.CVGROUP,
                "Germplasm Type",
                GobiiEntityNameType.CV,
                GobiiEntitySubType.UNKNOWN,
                CvGroup.GERMPLASM_TYPE,
                null));


        returnVal.add(new VertexDTO(14,
                GobiiVertexType.CVTERM,
                "DNA Ref Sample",
                GobiiEntityNameType.DNA_SAMPLE,
                GobiiEntitySubType.UNKNOWN,
                CvGroup.DNASAMPLE_PROP,
                "ref_sample"));

        returnVal.add(new VertexDTO(15,
                GobiiVertexType.CVTERM,
                "DNA Trial",
                GobiiEntityNameType.DNA_SAMPLE,
                GobiiEntitySubType.UNKNOWN,
                CvGroup.DNASAMPLE_PROP,
                "trial_name"));

        return returnVal;

    }

    @Override
    public VertexFilterDTO getVertexValues(String jobId, VertexFilterDTO vertexFilterDTO) throws GobiiDtoMappingException {

        VertexFilterDTO returnVal = vertexFilterDTO;

//        if( vertexFilterDTO.getDestinationVertexDTO() == null || vertexFilterDTO.getDestinationVertexDTO().getEntityType().isEmpty() ) {
//            throw new GobiiDtoMappingException("Unspecified destination vertex entity type");
//        }
//

        VertexDTO destinationVertex = vertexFilterDTO.getDestinationVertexDTO();
        for(Integer idx=1; idx <= 20; idx ++ ) {


            String name = "";
            if(destinationVertex.getGobiiVertexType().equals(GobiiVertexType.ENTITY)) {

                name = destinationVertex.getEntityType().toString().toLowerCase() + ": " + idx;

            } else if(destinationVertex.getGobiiVertexType().equals(GobiiVertexType.CVGROUP)) {

                name = destinationVertex.getCvGroup().toString().toLowerCase() + ": " + idx;

            } else if(destinationVertex.getGobiiVertexType().equals(GobiiVertexType.SUBENTITY)) {

                name = destinationVertex.getEntitySubType().toString().toLowerCase() + ": " + idx;

            } else if(destinationVertex.getGobiiVertexType().equals(GobiiVertexType.CVTERM)) {

                name = destinationVertex.getCvTerm() + ": " + idx;

            }

            returnVal.getVertexValues().add(
                    new NameIdDTO(GobiiEntityNameType.valueOf(vertexFilterDTO.getDestinationVertexDTO().getEntityType().name()),
                            idx,
                            name)
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
