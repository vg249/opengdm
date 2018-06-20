package org.gobiiproject.gobiidtomapping.entity.noaudit.impl;

import org.gobiiproject.gobiidao.gql.GqlText;
import org.gobiiproject.gobiidao.gql.GqlWrapper;
import org.gobiiproject.gobiidtomapping.core.GobiiDtoMappingException;
import org.gobiiproject.gobiidtomapping.entity.auditable.impl.DtoMapDataSetImpl;
import org.gobiiproject.gobiidtomapping.entity.noaudit.DtoMapFlexQuery;
import org.gobiiproject.gobiimodel.dto.entity.children.NameIdDTO;
import org.gobiiproject.gobiimodel.dto.entity.flex.VertexDTO;
import org.gobiiproject.gobiimodel.dto.entity.flex.VertexFilterDTO;
import org.gobiiproject.gobiimodel.dto.entity.flex.Vertices;
import org.gobiiproject.gobiimodel.dto.instructions.extractor.GobiiExtractorInstruction;
import org.gobiiproject.gobiimodel.utils.InstructionFileAccess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by Phil on 4/22/2016.
 */
public class DtoMapFlexQueryImpl implements DtoMapFlexQuery {


    private final String OUTPT_FILE_NAME_VALUES = "gql_result_values.txt";
    private final String OUTPT_FILE_NAME_MARKERS = "gql_result_markers.txt";
    private final String OUTPT_FILE_NAME_SAMPLES = "gql_result_samples.txt";
    private final String STD_OUTPT_FILE_NAME = "gql.out";
    private final String ERR_OUTPT_FILE_NAME = "gql.err";

    private Integer maxVertexValues = 500;
    private Integer maxCount = 100000;

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


    private void makeOutputDirectory(String outputFileDirectory) throws Exception {

        if (!instructionFileAccess.doesPathExist(outputFileDirectory)) {

            instructionFileAccess.makeDirectory(outputFileDirectory);

        } else {
            instructionFileAccess.verifyDirectoryPermissions(outputFileDirectory);
        }
    }

    @Override
    public VertexFilterDTO getVertexValues(String cropType, String jobId, VertexFilterDTO vertexFilterDTO) throws GobiiDtoMappingException {

        VertexFilterDTO returnVal = vertexFilterDTO;

        try {

            GqlText gqlText = new GqlText();
            String outputFileDirectory = gqlText.makePathForGqlJob(cropType, jobId);
            this.makeOutputDirectory(outputFileDirectory);

            String outputFileFqpn = outputFileDirectory + "/" + OUTPT_FILE_NAME_VALUES;
            String stdOutFileFqpn = outputFileDirectory + "/" + STD_OUTPT_FILE_NAME;
            String stdErrFileFqpn = outputFileDirectory + "/" + ERR_OUTPT_FILE_NAME;

            String gqlScriptCommandLine = gqlText.makeCommandLine(outputFileFqpn,
                    vertexFilterDTO.getFilterVertices(),
                    vertexFilterDTO.getDestinationVertexDTO(),
                    maxVertexValues);
            GqlWrapper.run(gqlScriptCommandLine, stdOutFileFqpn, stdErrFileFqpn);

            List<NameIdDTO> values = gqlText.makeValues(outputFileFqpn, vertexFilterDTO.getDestinationVertexDTO());
            returnVal.getVertexValues().addAll(values);


        } catch (Exception e) {
            LOGGER.error("Gobii Maping Error", e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;

    } // getVertexValues()

    public VertexFilterDTO getVertexValuesCounts(String cropType, String jobId, VertexFilterDTO vertexFilterDTO) throws GobiiDtoMappingException {

        VertexFilterDTO returnVal = vertexFilterDTO;

        try {

            GqlText gqlText = new GqlText();
            String outputFileDirectory = gqlText.makePathForGqlJob(cropType, jobId);
            this.makeOutputDirectory(outputFileDirectory);

            String markerOutputFileFqpn = outputFileDirectory + "/" + OUTPT_FILE_NAME_MARKERS;
            String sampleOutputFileFqpn = outputFileDirectory + "/" + OUTPT_FILE_NAME_SAMPLES;
            String stdOutFileFqpnMarkers = outputFileDirectory + "/" + "markers_" + STD_OUTPT_FILE_NAME;
            String stdErrFileFqpnMarkers = outputFileDirectory + "/" + "markers_" + ERR_OUTPT_FILE_NAME;
            String stdOutFileFqpnSamples = outputFileDirectory + "/" + "dnasamples_" + STD_OUTPT_FILE_NAME;
            String stdErrFileFqpnSamples = outputFileDirectory + "/" + "dnasamples_" + ERR_OUTPT_FILE_NAME;

            VertexDTO destinationVertexMarkers = Vertices.makeMarkerVertex();
            VertexDTO destinationVertexSamples = Vertices.makeSampleVertex();

            // temp values to make unit test past
//            vertexFilterDTO.setMarkerCount(100);
//            vertexFilterDTO.setSampleCount(100);

            String gqlScriptCommandLineMarkers = gqlText.makeCommandLine(markerOutputFileFqpn, vertexFilterDTO.getFilterVertices(), destinationVertexMarkers,maxCount);
            GqlWrapper.run(gqlScriptCommandLineMarkers, stdOutFileFqpnMarkers, stdErrFileFqpnMarkers);

            String gqlScriptCommandLineSamples = gqlText.makeCommandLine(sampleOutputFileFqpn, vertexFilterDTO.getFilterVertices(), destinationVertexSamples,maxCount);
            GqlWrapper.run(gqlScriptCommandLineSamples, stdOutFileFqpnSamples, stdErrFileFqpnSamples);

            Long markerCount = Files.lines(Paths.get(markerOutputFileFqpn)).count();
            Long sampleCount = Files.lines(Paths.get(sampleOutputFileFqpn)).count();

            if( markerCount > Integer.MAX_VALUE) {
                throw new GobiiDtoMappingException("Number of markers is too large to fit in an Integer: " + markerCount);
            }

            if( sampleCount > Integer.MAX_VALUE) {
                throw new GobiiDtoMappingException("Number of samples is too large to fit in an Integer: " + sampleCount);
            }

            vertexFilterDTO.setMarkerCount(markerCount.intValue());
            vertexFilterDTO.setSampleCount(sampleCount.intValue());

        } catch (Exception e) {
            LOGGER.error("Gobii Maping Error", e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;
    }

}
