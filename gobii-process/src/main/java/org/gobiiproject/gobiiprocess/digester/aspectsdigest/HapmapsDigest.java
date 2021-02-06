package org.gobiiproject.gobiiprocess.digester.aspectsdigest;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.docx4j.model.datastorage.XPathEnhancerParser.primaryExpr_return;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.cvnames.DatasetType;
import org.gobiiproject.gobiimodel.dto.gdmv3.GenotypeUploadRequestDTO;
import org.gobiiproject.gobiimodel.dto.instructions.loader.DigesterResult;
import org.gobiiproject.gobiimodel.dto.instructions.loader.MasticatorResult;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.ColumnAspect;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.DatasetDnaRunTable;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.DatasetMarkerTable;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.LoaderInstruction;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.MarkerTable;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.MatrixAspect;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.MatrixTable;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.MatrixTransformAspect;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.MatrixTransformTable;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.RangeAspect;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.RowAspect;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.Table;
import org.gobiiproject.gobiimodel.entity.Dataset;
import org.gobiiproject.gobiimodel.utils.AspectUtils;
import org.gobiiproject.gobiiprocess.digester.utils.GobiiFileUtils;
import org.gobiiproject.gobiiprocess.spring.SpringContextLoaderSingleton;
import org.gobiiproject.gobiisampletrackingdao.DatasetDao;
import org.gobiiproject.gobiisampletrackingdao.DatasetDaoImpl;

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

    private GenotypeUploadRequestDTO uploadRequest;

    private DatasetDao datasetDao;

    HapmapsDigest(LoaderInstruction loaderInstruction, 
                  ConfigSettings configSettings) throws GobiiException {
        super(loaderInstruction, configSettings);
        this.datasetDao = SpringContextLoaderSingleton.getInstance().getBean(DatasetDao.class);
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
                
        uploadRequest = mapper.convertValue(
            loaderInstruction.getUserRequest(), 
            GenotypeUploadRequestDTO.class);                    
                
        Map<String, MasticatorResult> masticatedFilesMap = new HashMap<>();

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
                while(fileScanner.hasNextLine() && 
                      headerLineNumberIndex < maxLinesToLookForHeader) {
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
                
                // Set dataset_marker table aspect               
                String datasetMarkerTableName = AspectUtils.getTableName(DatasetMarkerTable.class);
                int hdf5Start = 1;
                if(masticatedFilesMap.containsKey(datasetMarkerTableName)) {
                    hdf5Start = 
                        masticatedFilesMap.get(datasetMarkerTableName).getTotalLinesWritten();
                }
                aspects.put(datasetMarkerTableName, 
                            getDatasetMarkerTable(headerColumns, headerLineNumberIndex, hdf5Start));

                // Create dataset danrun aspects
                String datasetDnaRunTableName = AspectUtils.getTableName(DatasetDnaRunTable.class);
                aspects.put(datasetDnaRunTableName, 
                            getDatasetDnaRunTable(headerLineNumberIndex, hdf5Start));

                if(StringUtils.isNotEmpty(uploadRequest.getDataType()) &&
                    dataTypeToTransformType.containsKey(uploadRequest.getDataType())) {
                    String matrixTableName = AspectUtils.getTableName(MatrixTransformTable.class);
                    aspects.put(matrixTableName, 
                                getMatrixTransformTable(headerLineNumberIndex, 
                                                        uploadRequest.getDataType()));
                }
                else {
                    String matrixTableName = AspectUtils.getTableName(MatrixTable.class);
                    aspects.put(matrixTableName, getMatrixTable(headerLineNumberIndex));
                }
                

                // Masticate and set the output.
                masticatedFilesMap = masticate(aspects);

                // Update the intermediate file map incase if there is any new table
                masticatedFilesMap.forEach((table, masticatorResult) -> {
                    intermediateDigestFileMap.put(table, masticatorResult.getOutputFile());
                });

            }
        }
        catch (Exception e) {
            log.error(e.getMessage());
            throw new GobiiException(e);
        }
        
        // Get the load order from ifl config
        List<String> loadOrder = getLoadOrder();
        if(loadOrder == null || loadOrder.size() <= 0) {
            loadOrder = new ArrayList<>(intermediateDigestFileMap.keySet());
        }

        DigesterResult digesterResult = new DigesterResult
                .Builder()
                .setSuccess(true)
                .setSendQc(false)
                .setCropType(loaderInstruction.getCropType())
                .setCropConfig(cropConfig)
                .setIntermediateFilePath(loaderInstruction.getOutputDir())
                .setLoadType(loaderInstruction.getLoadType())
                .setLoaderInstructionsMap(intermediateDigestFileMap)
                .setLoaderInstructionsList(loadOrder)
                .setDatasetType(uploadRequest.getDataType())
                .setJobStatusObject(jobStatus)
                .setDatasetId(null)
                .setJobName(loaderInstruction.getJobName())
                .setContactEmail(loaderInstruction.getContactEmail())
                .build();

        return digesterResult;
    }

    private DatasetMarkerTable getDatasetMarkerTable(
        String[] headers, 
        int headerLineNumber,
        int hdf5Start) {

        DatasetMarkerTable datasetMarkerTable = new DatasetMarkerTable();

        datasetMarkerTable.setDatasetId(uploadRequest.getDatasetId());
        datasetMarkerTable.setPlatformId(uploadRequest.getPlatformId());

        // Set column aspect for marker name.
        int markerNameColumnIndex = 
            ArrayUtils.indexOf(headers, 
                               aspectTableColumnsToHapmapColumns.get("markerName"));
        // data lines starts after header line
        ColumnAspect markerNameColumn = 
            new ColumnAspect(headerLineNumber+1, markerNameColumnIndex);
        datasetMarkerTable.setMarkerName(markerNameColumn);
        
        // Set range aspect for hdf5 index.
        datasetMarkerTable.setDatasetMarkerIdx(new RangeAspect(hdf5Start));
        
        return datasetMarkerTable;
    }

    private DatasetDnaRunTable getDatasetDnaRunTable(int headerLineNumber,
                                                     int hdf5Start) throws GobiiException {
        
       DatasetDnaRunTable datasetDnaRunTable = new DatasetDnaRunTable();
        
       datasetDnaRunTable.setDatasetId(uploadRequest.getDatasetId());
       datasetDnaRunTable.setPlatformId(uploadRequest.getPlatformId());

       // Get Experiment id
       Integer datasetId;
       try {
           datasetId = Integer.parseInt(uploadRequest.getDatasetId());
       }
       catch(NumberFormatException e) {
           throw new GobiiException("Invalid dataset id");
       }
       Dataset dataset = datasetDao.getDataset(datasetId);
       datasetDnaRunTable.setExperimentId(dataset.getExperiment().getExperimentId().toString());

       // Dnarun names starts after required columns in the file
       RowAspect dnaRunNameRow = new RowAspect(headerLineNumber, 
                                               hapMapRequiredColumns.length);
       datasetDnaRunTable.setDnaRunName(dnaRunNameRow);
        
       // Set Range aspect for hdf5 index                   
       datasetDnaRunTable.setDatasetDnaRunIdx(new RangeAspect(1));
       
       return datasetDnaRunTable;

    }

    private MatrixTransformTable getMatrixTransformTable(int headerLineNumber, 
                                                         String dataType) {
        MatrixTransformTable matrixTable = new MatrixTransformTable();
        MatrixTransformAspect matrixAspect = new MatrixTransformAspect(
            dataTypeToTransformType.get(dataType),
            headerLineNumber+1, 
            hapMapRequiredColumns.length);
        matrixTable.setMatrix(matrixAspect);
        return matrixTable;
    }

    private MatrixTable getMatrixTable(int headerLineNumber) {
        MatrixTable matrixTable = new MatrixTable();
        MatrixAspect matrixAspect = new MatrixAspect(headerLineNumber+1, 
                                                     hapMapRequiredColumns.length);

        matrixTable.setMatrix(matrixAspect);
        return matrixTable;
    }

}
