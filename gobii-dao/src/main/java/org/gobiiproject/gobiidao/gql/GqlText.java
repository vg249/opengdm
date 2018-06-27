package org.gobiiproject.gobiidao.gql;

import org.apache.commons.io.FileUtils;
import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.entity.children.NameIdDTO;
import org.gobiiproject.gobiimodel.dto.entity.flex.VertexDTO;
import org.gobiiproject.gobiimodel.types.GobiiFileProcessDir;
import org.gobiiproject.gobiimodel.utils.HelperFunctions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GqlText {


    private final String connectionString;
    private String mdePathFqpn;

    public GqlText(String cropType) throws GobiiException {
        try {

            ConfigSettings configSettings = new ConfigSettings();
            this.connectionString = HelperFunctions.getPostgresConnectionString(configSettings.getCropConfig(cropType));

            this.mdePathFqpn = configSettings
                    .getFullyQualifiedFilePath(null, GobiiFileProcessDir.CODE_EXTRACTORS_POSTGRES_MDE)
                    + GQL_SCRIPT_NAME;

            if (!new File(this.mdePathFqpn).exists()) {

                this.mdePathFqpn = configSettings
                        .getFullyQualifiedFilePath(null, GobiiFileProcessDir.CODE_EXTRACTORS_POSTGRES_MDE)
                        + GQL_DUMMY_SCRIPT_NAME;

                File dummyScriptFile = new File(this.mdePathFqpn);
                if (!dummyScriptFile.exists()) {
                    String resourcePath = GQL_DUMMY_SCRIPT_NAME;
                    InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourcePath);
                    if (inputStream != null) {
                        FileUtils.copyInputStreamToFile(inputStream, dummyScriptFile);
                    } else {
                        throw new GobiiDaoException("Unable to make input stream for " + resourcePath);
                    }
                }
            }

        } catch (Exception e) {
            throw new GobiiException(e);
        }
    }

    private final static String GQL_SCRIPT_NAME = "python gobii_gql.py";
    private final String GQL_DUMMY_SCRIPT_NAME = "gobii_gql_placeholder.py";


    private final String GQL_PARM_CONNECTION_STRING = "-c";
    private final String GQL_PARM_OUTPUT_FILE_PATH = "-o";
    private final String GQL_PARM_SUB_GRAPH_PATH = "-g"; // OPTIONAL: JSON string of key-value-pairs of this format: {vertex_name1:[value_id1, value_id2], vertex_name2:[value_id1], ...}.
    private final String GQL_PARM_TARGET_VERTEX_NAME = "-t";
    private final String GQL_PARM_TARGET_VERTEX_COLUMNS = "-f"; // OPTIONAL
    private final String GQL_PARM_COUNT_LIMIT = "-l";

    public String makeGqlJobPath(String cropType, String jobId) throws GobiiDaoException {

        String returnVal = null;

        try {

            returnVal = (new ConfigSettings()).getFullyQualifiedFilePath(cropType, GobiiFileProcessDir.GQL_PROCESS);
            returnVal += "/" + jobId + "/";
        } catch (Exception e) {
            throw new GobiiDaoException(e);
        }

        return returnVal;
    }


    public String makeGqlJobFileFqpn(String cropType, String jobId, GqlOFileType gqlOFileType, GqlDestinationFileType gqlDestinationFileType) {

        String returnVal = this.makeGqlJobPath(cropType, jobId);

        returnVal += gqlDestinationFileType.getDestination();
        if (gqlOFileType.getIoName().equals(GqlOFileType.NONE.getIoName())) {
            returnVal += ".txt";
        } else {
            returnVal += gqlOFileType.getIoName();
        }

        return returnVal;
    }

    private String makeArg(String gqlFlag, String argVal, boolean addSingleQuotes) {

        String quoteChar = addSingleQuotes ? "'" : "";
        String space = " ";
        return gqlFlag + space + quoteChar + argVal + quoteChar + space;
    }

    //python gobii_gql.py /temp/filter3.out {principal_investigator:[67,69,70], project:[3,25,30], division:[25,30]} experiment ['name']
    public String makeCommandLine(String outputFileFqpn,
                                  List<VertexDTO> subGraphVertices,
                                  VertexDTO destinationVertex,
                                  Integer maxResult) throws Exception {


        StringBuilder commandLineBuilder = new StringBuilder();

        // path to gql script
        commandLineBuilder.append("python " + mdePathFqpn + " ");

        commandLineBuilder.append(this.makeArg(GQL_PARM_CONNECTION_STRING,
                this.connectionString,
                false));

        // output fqpn
        Path path = Paths.get(outputFileFqpn);
        Path parent = path.getParent();
        if (!new File(parent.toUri()).exists()) {
            throw new GobiiDaoException("The specified output directory does not exist: " + parent);
        }


        commandLineBuilder.append(this.makeArg(GQL_PARM_OUTPUT_FILE_PATH,
                outputFileFqpn,
                false));

        // filter path
        if( subGraphVertices.size() > 0 ) {
            StringBuilder subGraphPath = new StringBuilder();
            subGraphPath.append("{");
            Iterator<VertexDTO> filterPathIterator = subGraphVertices.iterator();
            while (filterPathIterator.hasNext()) {

                subGraphPath.append(filterPathIterator.next().toGqlSubPathElement());
                if (filterPathIterator.hasNext()) {
                    subGraphPath.append(",");
                }
            }
            subGraphPath.append("}");
            commandLineBuilder.append(this.makeArg(GQL_PARM_SUB_GRAPH_PATH,
                    subGraphPath.toString(),
                    true));
        } // if there is a filter path

        commandLineBuilder.append(this.makeArg(GQL_PARM_TARGET_VERTEX_NAME,
                destinationVertex.getVertexNameType().getVertexName(),
                false));

        StringBuilder columnList = new StringBuilder();
        columnList.append("[");
        Iterator<String> columnsIterator = destinationVertex.getVertexColumns().getColumnNames().iterator();
        if (columnsIterator.hasNext()) {
            columnsIterator.next(); // first column should always be ID -- which we don't need in our arg list
        }
        while (columnsIterator.hasNext()) {
            columnList.append("\"" + columnsIterator.next() + "\"");
            if (columnsIterator.hasNext()) {
                columnList.append(",");
            }
        }
        columnList.append("]");
        commandLineBuilder.append(this.makeArg(GQL_PARM_TARGET_VERTEX_COLUMNS,
                columnList.toString(),
                true));


        commandLineBuilder.append(this.makeArg(GQL_PARM_COUNT_LIMIT,
                maxResult.toString(),
                false));

        return commandLineBuilder.toString();
    }


    public List<NameIdDTO> makeValues(String fqpn, VertexDTO destinationVertex) throws Exception {

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
