package org.gobiiproject.gobiidao.gql;

import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.dto.entity.children.NameIdDTO;
import org.gobiiproject.gobiimodel.dto.entity.flex.VertexDTO;
import org.gobiiproject.gobiimodel.types.GobiiFileProcessDir;

import java.util.ArrayList;
import java.util.List;

public class GqlText {

    public static String makePathForGqlJob(String cropType, String jobId) throws Exception{

        String returnVal = null;

        returnVal = (new ConfigSettings()).getFullyQualifiedFilePath(cropType, GobiiFileProcessDir.GQL_PROCESS);
        returnVal += "/" + jobId + "/";

        return  returnVal;
    }


    public static String makeCommandLine(String fqpn, List<VertexDTO> filterPath, VertexDTO destinationVertex) throws Exception {

        String returnVal = (new ConfigSettings()).getFullyQualifiedFilePath(null, GobiiFileProcessDir.CODE_EXTRACTORS_POSTGRES_MDE);
        returnVal += " python gobii_gql.py ";

        return returnVal;
    }


    public static List<NameIdDTO> makeValues(String fqpn, VertexDTO destinationVertex) throws Exception {

        List<NameIdDTO> returnVal = new ArrayList<>();


        // maket target vertex column list
        String columnList = "[";
        for(String currentColumn: destinationVertex.getVertexColumns().getColumnNames()) {
            columnList += "'" + currentColumn  + "',";
        }
        columnList = columnList.substring(0,columnList.lastIndexOf(','));
        columnList += "]";

        return returnVal;

    }
}
