package org.gobiiproject.gobiiprocess.digester.aspectsdigest;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.lang.ArrayUtils;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.gdmv3.GenotypeUploadRequestDTO;
import org.gobiiproject.gobiimodel.dto.instructions.loader.DigesterResult;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.ColumnAspect;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.DatasetDnaRunTable;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.DatasetMarkerTable;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.LoaderInstruction;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.MatrixAspect;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.MatrixTable;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.RangeAspect;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.RowAspect;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.Table;
import org.gobiiproject.gobiimodel.utils.AspectUtils;
import org.gobiiproject.gobiiprocess.digester.utils.GobiiFileUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HapmapsDigest extends AspectDigest {

    
    private final String headerIdentifier = "rs#";

    private final int maxLinesToLookForHeader = 1000;

    private final String[] hapMapRequiredColumns = {
        "rs#",
        "alleles",
        "chrom",
        "pos",
        "strand",
        "assembly#",
        "center",
        "protLSID",
        "assayLSID",
        "panelLSID",
        "QCcode"
    };

    private final Map<String, String> aspectTableColumnsToHapmapColumns = 
        Map.of("markerName", "rs#", 
               "linkageGroupName", "chrom", 
               "markerPositionStart", "pos",
               "markerPositionStop", "pos");

    HapmapsDigest(LoaderInstruction loaderInstruction, 
                  ConfigSettings configSettings) throws GobiiException {
        super(loaderInstruction, configSettings);
    }

    private boolean hasValidHapmaHeaders(String[] headers) {
        return false;
    }
 
    public DigesterResult digest() throws GobiiException {        

        List<File> filesToDigest = new ArrayList<>();
        Map<String, File> intermediateDigestFileMap = new HashMap<>();

        Map<String, Table> aspects = new HashMap<>();
    
        // creates new directtory or cleans one if already exists
        setupOutputDirectory();            
    
        try {

            filesToDigest = getFilesToDigest();

            // Digested files are merged for each table.
            for(File fileToDigest : filesToDigest) {

                // Ignore non text file
                if(!GobiiFileUtils.isFileTextFile(fileToDigest)) {
                    continue;
                }

                // Get file header
                String fileHeaderLine="";
                int headerLineNumberIndex = 0;
                Scanner fileScanner;
                try {
                    fileScanner = new Scanner(fileToDigest);
                }
                catch (FileNotFoundException e) {
                    throw new GobiiException("Input file does not exist");
                }
                while(fileScanner.hasNextLine() && headerLineNumberIndex < maxLinesToLookForHeader) {
                    fileHeaderLine = fileScanner.nextLine();
                    if(fileHeaderLine.startsWith(headerIdentifier)) {
                        break;
                    }
                    headerLineNumberIndex++;
                }
                fileScanner.close();

                if(headerLineNumberIndex == maxLinesToLookForHeader) {
                    throw new GobiiException("Unable to read file header. " +
                                             "Could not find hapmap header in first 1000 lines.");
                }

                String[] headerColumns = fileHeaderLine.split(GobiiFileUtils.TAB_SEP);

                if(!hasValidHapmaHeaders(headerColumns)) {

                }
                GenotypeUploadRequestDTO uploadRequest = mapper.convertValue(
                    loaderInstruction.getUserRequest(), 
                    GenotypeUploadRequestDTO.class);                    
                //
                // Create dataset marker aspects                
                //
                DatasetMarkerTable datasetMarkerTable = new DatasetMarkerTable();
                String datasetMarkerTableName = AspectUtils.getTableName(DatasetMarkerTable.class);

                datasetMarkerTable.setDatasetId(uploadRequest.getDatasetId());
                datasetMarkerTable.setPlatformId(uploadRequest.getPlatformId());

                // Set column aspect for marker name.
                int markerNameColumnIndex = 
                    ArrayUtils.indexOf(headerColumns, 
                                       aspectTableColumnsToHapmapColumns.get("markerName"));
                ColumnAspect markerNameColumn = 
                    new ColumnAspect(headerLineNumberIndex+1, markerNameColumnIndex);
                datasetMarkerTable.setMarkerName(markerNameColumn);

                // Set range aspect for hdf5 index.
                datasetMarkerTable.setDatasetMarkerIdx(new RangeAspect(1));

                //
                // Create dataset danrun aspects
                //
                DatasetDnaRunTable datasetDnaRunTable = new DatasetDnaRunTable();
                String datasetDnaRunTableName = AspectUtils.getTableName(DatasetDnaRunTable.class);

                datasetDnaRunTable.setDatasetId(uploadRequest.getDatasetId());
                datasetDnaRunTable.setPlatformId(uploadRequest.getPlatformId());

                // Dnarun names starts after required columns in the file
                RowAspect dnaRunNameRow = new RowAspect(headerLineNumberIndex, 
                                                        hapMapRequiredColumns.length);
                datasetDnaRunTable.setDnaRunName(dnaRunNameRow);
                
                // Set Range aspect for hdf5 index                   
                datasetDnaRunTable.setDatasetDnaRunIdx(new RangeAspect(1)); 

               
                MatrixTable matrixTable = new MatrixTable();
                String matrixTableName = AspectUtils.getTableName(MatrixTable.class);
                MatrixAspect matrixAspect = new MatrixAspect(headerLineNumberIndex+1, 
                                                             hapMapRequiredColumns.length);
                matrixTable.setMatrix(matrixAspect);
                
                aspects.put(datasetMarkerTableName, datasetMarkerTable);
                aspects.put(datasetDnaRunTableName, datasetDnaRunTable);
                aspects.put(matrixTableName, matrixTable);

                // Masticate and set the output.
                Map<String, File> masticatedFilesMap = masticate(aspects);

                // Update the intermediate file map incase if there is any new table
                masticatedFilesMap.forEach((table, filePath) -> {
                    intermediateDigestFileMap.put(table, filePath);
                });

            }
        }
        catch (Exception e) {
            log.error(e.getMessage());
            throw new GobiiException(e);
        }

        return null;
    }

    
}
