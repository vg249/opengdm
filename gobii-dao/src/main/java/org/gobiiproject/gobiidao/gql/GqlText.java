package org.gobiiproject.gobiidao.gql;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.dto.entity.children.NameIdDTO;
import org.gobiiproject.gobiimodel.dto.entity.flex.VertexDTO;
import org.gobiiproject.gobiimodel.types.GobiiFileProcessDir;

import java.io.File;
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
        for (String currentColumn : destinationVertex.getVertexColumns().getColumnNames()) {
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


        // maket target vertex column list
        String columnList = "[";
        for (String currentColumn : destinationVertex.getVertexColumns().getColumnNames()) {
            columnList += "'" + currentColumn + "',";
        }
        columnList = columnList.substring(0, columnList.lastIndexOf(','));
        columnList += "]";

        return returnVal;

    }
}
