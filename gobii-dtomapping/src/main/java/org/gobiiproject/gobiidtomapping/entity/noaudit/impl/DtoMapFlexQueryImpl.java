package org.gobiiproject.gobiidtomapping.entity.noaudit.impl;

import org.gobiiproject.gobiidao.gql.GqlText;
import org.gobiiproject.gobiidao.gql.GqlWrapper;
import org.gobiiproject.gobiidtomapping.core.GobiiDtoMappingException;
import org.gobiiproject.gobiidtomapping.entity.auditable.impl.DtoMapDataSetImpl;
import org.gobiiproject.gobiidtomapping.entity.noaudit.DtoMapFlexQuery;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.cvnames.CvGroup;
import org.gobiiproject.gobiimodel.cvnames.VertexNameType;
import org.gobiiproject.gobiimodel.dto.entity.children.NameIdDTO;
import org.gobiiproject.gobiimodel.dto.entity.flex.VertexDTO;
import org.gobiiproject.gobiimodel.dto.entity.flex.VertexFilterDTO;
import org.gobiiproject.gobiimodel.dto.entity.flex.Vertices;
import org.gobiiproject.gobiimodel.dto.instructions.extractor.GobiiExtractorInstruction;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;
import org.gobiiproject.gobiimodel.types.GobiiEntitySubType;
import org.gobiiproject.gobiimodel.types.GobiiFileProcessDir;
import org.gobiiproject.gobiimodel.types.GobiiVertexType;
import org.gobiiproject.gobiimodel.utils.InstructionFileAccess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Phil on 4/22/2016.
 */
public class DtoMapFlexQueryImpl implements DtoMapFlexQuery {


    private final String OUTPT_FILE_NAME = "gql_result.txt";
    private final String STD_OUTPT_FILE_NAME = "gql.out";
    private final String ERR_OUTPT_FILE_NAME = "gql.err";

    Logger LOGGER = LoggerFactory.getLogger(DtoMapDataSetImpl.class);
    private InstructionFileAccess<GobiiExtractorInstruction> instructionFileAccess = new InstructionFileAccess<>(GobiiExtractorInstruction.class);


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
        return Vertices.getAll();
    }

    @Override
    public VertexFilterDTO getVertexValues(String cropType, String jobId, VertexFilterDTO vertexFilterDTO) throws GobiiDtoMappingException {

        VertexFilterDTO returnVal = vertexFilterDTO;

        try {

            String outputFileDirectory =  (new ConfigSettings()).getFullyQualifiedFilePath(cropType, GobiiFileProcessDir.GQL_PROCESS);
            outputFileDirectory += "/" + jobId;

            if (!instructionFileAccess.doesPathExist(outputFileDirectory)) {

                instructionFileAccess.makeDirectory(outputFileDirectory);

            } else {
                instructionFileAccess.verifyDirectoryPermissions(outputFileDirectory);
            }

            String outputFileFqpn = outputFileDirectory + "/" + OUTPT_FILE_NAME;
            String stdOutFileFqpn = outputFileDirectory + "/" + STD_OUTPT_FILE_NAME;
            String stdErrFileFqpn = outputFileDirectory + "/" + ERR_OUTPT_FILE_NAME;

            String gqlScriptCommandLine = GqlText.makeCommandLine(outputFileFqpn,vertexFilterDTO.getFilterVertices(),vertexFilterDTO.getDestinationVertexDTO());

            GqlWrapper.run(gqlScriptCommandLine,stdOutFileFqpn,stdErrFileFqpn);

            List<NameIdDTO> values = GqlText.makeValues(outputFileFqpn,vertexFilterDTO.getDestinationVertexDTO());



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

        } catch(Exception e) {
            LOGGER.error("Gobii Maping Error", e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;

    } // getVertexValues()

    public VertexFilterDTO getVertexValuesCounts(String cropType, String jobId, VertexFilterDTO vertexFilterDTO) throws GobiiDtoMappingException {

        VertexFilterDTO returnVal = vertexFilterDTO;


        vertexFilterDTO.setMarkerCount(ThreadLocalRandom.current().nextInt(1, 1000));
        vertexFilterDTO.setSampleCount(ThreadLocalRandom.current().nextInt(1, 1000));

        return returnVal;
    }

}
