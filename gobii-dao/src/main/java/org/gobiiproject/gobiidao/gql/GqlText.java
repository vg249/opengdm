package org.gobiiproject.gobiidao.gql;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.dto.entity.children.NameIdDTO;
import org.gobiiproject.gobiimodel.dto.entity.flex.VertexDTO;
import org.gobiiproject.gobiimodel.types.GobiiFileProcessDir;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class GqlText {

    //private final static String GQL_SCRIPT_NAME = "python gobii_gql.py";
    private final static String GQL_SCRIPT_NAME = "gobii_gql_placeholder.py";

    public static String makePathForGqlJob(String cropType, String jobId) throws Exception {

        String returnVal = null;

        returnVal = (new ConfigSettings()).getFullyQualifiedFilePath(cropType, GobiiFileProcessDir.GQL_PROCESS);
        returnVal += "/" + jobId + "/";

        return returnVal;
    }


    //python gobii_gql.py /temp/filter3.out {principal_investigator:[67,69,70], project:[3,25,30], division:[25,30]} experiment ['name']
    public static String makeCommandLine(String outputFileFqpn, List<VertexDTO> filterPath, VertexDTO destinationVertex) throws Exception {

        StringBuilder commandLineBuilder = new StringBuilder();

        // path to gql script
        String mdePath = (new ConfigSettings()).getFullyQualifiedFilePath(null, GobiiFileProcessDir.CODE_EXTRACTORS_POSTGRES_MDE);
        mdePath += GQL_SCRIPT_NAME;
        if (!new File(mdePath).exists()) {
            throw new GobiiDaoException("The python script does not exist at the specified location: " + mdePath);
        }
        commandLineBuilder.append("python " + mdePath + " ");

        // output fqpn
        Path path = Paths.get(outputFileFqpn);
        Path parent = path.getParent();
        if (!new File(parent.toUri()).exists()) {
            throw new GobiiDaoException("The specified output directory does not exist: " + parent);
        }
        commandLineBuilder.append(outputFileFqpn);

        // filter path
        commandLineBuilder.append(" {");
        for (VertexDTO currentVertex : filterPath) {

            commandLineBuilder.append(currentVertex.getVertexNameType().getVertexName());
            commandLineBuilder.append(":");
            commandLineBuilder.append("[");
            for (Integer currentFilterVal : currentVertex.getFilterVals()) {
                commandLineBuilder.append(currentFilterVal + ",");
            }
            removeFinalCommaDamnIt(commandLineBuilder);
            commandLineBuilder.append("],");
        }
        removeFinalCommaDamnIt(commandLineBuilder);
        commandLineBuilder.append("} ");


        commandLineBuilder.append(" " + destinationVertex.getVertexNameType().getVertexName() + " [");
        for (Integer idx = 1; // first column should always be ID -- which we don't need in our arg list
             idx < destinationVertex.getVertexColumns().getColumnNames().size();
             idx++) {
            String currentColumn = destinationVertex.getVertexColumns().getColumnNames().get(idx);
            String currentName = "'" + currentColumn + "',";
            commandLineBuilder.append(currentName);
        }
        removeFinalCommaDamnIt(commandLineBuilder);
        commandLineBuilder.append("]");

        return commandLineBuilder.toString();
    }


    private static void removeFinalCommaDamnIt(StringBuilder stringBuilder) {

        Integer positionOfFinalComma = stringBuilder.lastIndexOf(",");
        if (positionOfFinalComma > -1) {
            stringBuilder.replace(positionOfFinalComma, positionOfFinalComma + 1, "");
        }
    }

    public static List<NameIdDTO> makeValues(String fqpn, VertexDTO destinationVertex) throws Exception {

        List<NameIdDTO> returnVal = new ArrayList<>();

        File file = new File(fqpn);
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        StringBuffer stringBuffer = new StringBuffer();
        String line;
        bufferedReader.readLine(); // consume first header line
        while ((line = bufferedReader.readLine()) != null) {

            NameIdDTO currentNameIdDto = destinationVertex.getVertexColumns().vertexValueFromLine(line);
            currentNameIdDto.setGobiiEntityNameType(destinationVertex.getEntityType());
            returnVal.add(currentNameIdDto);
        }
        fileReader.close();

        return returnVal;

    }
}
