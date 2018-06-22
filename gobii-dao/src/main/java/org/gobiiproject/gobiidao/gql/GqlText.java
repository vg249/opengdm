package org.gobiiproject.gobiidao.gql;

import org.apache.commons.io.FileUtils;
import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.dto.entity.children.NameIdDTO;
import org.gobiiproject.gobiimodel.dto.entity.flex.VertexDTO;
import org.gobiiproject.gobiimodel.types.GobiiFileProcessDir;

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

    //private final static String GQL_SCRIPT_NAME = "python gobii_gql.py";
    private final static String GQL_SCRIPT_NAME = "gobii_gql_placeholder.py";
    private static String GQL_DUMMY_SCRIPT_NAME = "gobii_gql_placeholder.py";


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


    public String makeGqlJobFileFqpn(String cropType, String jobId, GqlOFileType gqlOFileType, GqDestinationFileType gqDestinationFileType) {

        String returnVal = this.makeGqlJobPath(cropType, jobId);

        returnVal += gqDestinationFileType.getDestination();
        if (gqlOFileType.getIoName().equals(GqlOFileType.NONE.getIoName())) {
            returnVal += ".txt";
        } else {
            returnVal += gqlOFileType.getIoName();
        }

        return returnVal;
    }

    private void copyDummyScript() throws GobiiDaoException {

        try {

            String mdePathFqpn = (new ConfigSettings()).getFullyQualifiedFilePath(null, GobiiFileProcessDir.CODE_EXTRACTORS_POSTGRES_MDE);
            mdePathFqpn += GQL_DUMMY_SCRIPT_NAME;
            File dummyScriptFile = new File(mdePathFqpn);

            if (!dummyScriptFile.exists()) {
                String resourcePath = GQL_DUMMY_SCRIPT_NAME;
                InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourcePath);
                if (inputStream != null) {
                    FileUtils.copyInputStreamToFile(inputStream, dummyScriptFile);
                } else {
                    throw new GobiiDaoException("Unable to make input stream for " + resourcePath);
                }
            }

        } catch (Exception e) {
            throw new GobiiDaoException(e);
        }

    }


    //python gobii_gql.py /temp/filter3.out {principal_investigator:[67,69,70], project:[3,25,30], division:[25,30]} experiment ['name']
    public String makeCommandLine(String outputFileFqpn,
                                  List<VertexDTO> filterPath,
                                  VertexDTO destinationVertex,
                                  Integer maxResult) throws Exception {


        copyDummyScript();

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
        Iterator<VertexDTO> filterPathIterator = filterPath.iterator();
        while (filterPathIterator.hasNext()) {

            commandLineBuilder.append(filterPathIterator.next().toFIlter());
            if (filterPathIterator.hasNext()) {
                commandLineBuilder.append(",");
            }
        }
        commandLineBuilder.append("} ");


        commandLineBuilder.append(" " + destinationVertex.getVertexNameType().getVertexName() + " [");
        Iterator<String> columnsIterator = destinationVertex.getVertexColumns().getColumnNames().iterator();
        if (columnsIterator.hasNext()) {
            columnsIterator.next(); // first column should always be ID -- which we don't need in our arg list
        }
        while (columnsIterator.hasNext()) {
            commandLineBuilder.append("'" + columnsIterator.next() + "'");
            if (columnsIterator.hasNext()) {
                commandLineBuilder.append(",");
            }
        }
        commandLineBuilder.append("]");

        commandLineBuilder.append(" " + maxResult);

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
