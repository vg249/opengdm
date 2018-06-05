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


    /**
     * This method returns a list of vertices. Nominally, the vertices returned are those defined in the vertex table.
     * However, the intention of this method is to return only those vertices for which is_entry is true -- i.e., the
     * vertices that are intended to be supplied, by ID, as inputs to the search algorithm. The semantics
     * of the vertex table are table are intended to be consumed primarily by the the search algorithm, the purview
     * of which lies outside the immediate scope of web services. Rather, it is the purpose of this method to provide
     * to the web client the vertex list that it requires to, in effect, enable the user to formulate a search that
     * can be serviced by the algorithm. Accordingly, the VertexDTO represents a vertex in semantics that are necessary for
     * proper display in the web application. To this end, the list returned by this method is essentially static. The only
     * relationship that the VertexDTO list need have with the vertex table is that the vertex IDs must match the values
     * in the vertex table for the intended entity. The downside to this approach is that when new vertices for which is_entry
     * is true are added, this code will ened to be updated manually. The infrequency of such changes makes this approach
     * more economical than would be the engineering effort of correlating the vertex table's semantics with those that
     * are required by the web application.
     *
     * @return
     * @throws GobiiDtoMappingException
     */
    public List<VertexDTO> getVertices() throws GobiiDtoMappingException {

        List<VertexDTO> returnVal = new ArrayList<>();

        // for now, until we have an actual vertex table, we are making up the IDs. However, the list of vertices
        // should be complete based on the specification
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
                GobiiVertexType.ENTITY,
                "Analysis",
                GobiiEntityNameType.ANALYSIS,
                GobiiEntitySubType.UNKNOWN,
                CvGroup.UNKNOWN,
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
                "Germplasm Subspecies",
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

        returnVal.add(new VertexDTO(16,
                GobiiVertexType.CVTERM,
                "Date Sampled",
                GobiiEntityNameType.PROJECT,
                GobiiEntitySubType.UNKNOWN,
                CvGroup.PROJECT_PROP,
                "date_sampled"));

        returnVal.add(new VertexDTO(17,
                GobiiVertexType.CVTERM,
                "Genotyping Purpose",
                GobiiEntityNameType.PROJECT,
                GobiiEntitySubType.UNKNOWN,
                CvGroup.PROJECT_PROP,
                "genotyping_purpose"));

        returnVal.add(new VertexDTO(18,
                GobiiVertexType.CVTERM,
                "Division",
                GobiiEntityNameType.PROJECT,
                GobiiEntitySubType.UNKNOWN,
                CvGroup.PROJECT_PROP,
                "division"));

        returnVal.add(new VertexDTO(19,
                GobiiVertexType.CVGROUP,
                "Dataset Type",
                GobiiEntityNameType.CV,
                GobiiEntitySubType.UNKNOWN,
                CvGroup.DATASET_TYPE,
                null));

        returnVal.add(new VertexDTO(20,
                GobiiVertexType.CVTERM,
                "Reference Sample",
                GobiiEntityNameType.DNA_SAMPLE,
                GobiiEntitySubType.UNKNOWN,
                CvGroup.DNASAMPLE_PROP,
                "ref_sample"));

        returnVal.add(new VertexDTO(21,
                GobiiVertexType.ENTITY,
                "Vendor",
                GobiiEntityNameType.VENDOR,
                GobiiEntitySubType.UNKNOWN,
                CvGroup.UNKNOWN,
                null));

        returnVal.add(new VertexDTO(22,
                GobiiVertexType.ENTITY,
                "Protocol",
                GobiiEntityNameType.PROTOCOL,
                GobiiEntitySubType.UNKNOWN,
                CvGroup.UNKNOWN,
                null));

        returnVal.add(new VertexDTO(23,
                GobiiVertexType.CVTERM,
                "Germplasm Species",
                GobiiEntityNameType.GERMPLASM,
                GobiiEntitySubType.UNKNOWN,
                CvGroup.GERMPLASM_PROP,
                "germplasm_species"));

        returnVal.add(new VertexDTO(24,
                GobiiVertexType.ENTITY,
                "Dataset",
                GobiiEntityNameType.DATASET,
                GobiiEntitySubType.UNKNOWN,
                CvGroup.UNKNOWN,
                null));

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
        for (Integer idx = 1; idx <= 20; idx++) {


            String typeName = "";

            if (destinationVertex.getGobiiVertexType().equals(GobiiVertexType.ENTITY)) {

                typeName = destinationVertex.getEntityType().toString().toLowerCase();

            } else if (destinationVertex.getGobiiVertexType().equals(GobiiVertexType.CVGROUP)) {

                typeName = destinationVertex.getCvGroup().toString().toLowerCase();

            } else if (destinationVertex.getGobiiVertexType().equals(GobiiVertexType.SUBENTITY)) {

                typeName = destinationVertex.getEntitySubType().toString().toLowerCase();

            } else if (destinationVertex.getGobiiVertexType().equals(GobiiVertexType.CVTERM)) {

                typeName = destinationVertex.getCvTerm();

            }

            String numberString = String.format("%02d", idx);
            String name = "dummy " + typeName + " # " + numberString;
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


        vertexFilterDTO.setMarkerCount(ThreadLocalRandom.current().nextInt(1, 1000));
        vertexFilterDTO.setSampleCount(ThreadLocalRandom.current().nextInt(1, 1000));

        return returnVal;
    }

}
