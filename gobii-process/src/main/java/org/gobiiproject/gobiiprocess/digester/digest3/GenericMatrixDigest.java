package org.gobiiproject.gobiiprocess.digester.digest3;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.gdmv3.templates.GenotypeMatrixTemplateDTO;
import org.gobiiproject.gobiimodel.dto.instructions.loader.DigesterResult;
import org.gobiiproject.gobiimodel.dto.instructions.loader.MasticatorResult;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.ColumnAspect;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.DatasetDnaRunTable;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.DatasetMarkerTable;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.FileHeader;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.LoaderInstruction3;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.MatrixAspect;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.MatrixTable;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.MatrixTransformAspect;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.MatrixTransformTable;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.RangeAspect;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.RowAspect;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.Table;
import org.gobiiproject.gobiimodel.entity.Experiment;
import org.gobiiproject.gobiimodel.utils.AspectUtils;
import org.gobiiproject.gobiimodel.utils.GobiiFileUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GenericMatrixDigest extends GenotypeMatrixDigest {

    
    private GenotypeMatrixTemplateDTO matrixTemplate;

    GenericMatrixDigest(LoaderInstruction3 loaderInstruction, 
                        ConfigSettings configSettings) throws GobiiException {
        super(loaderInstruction, configSettings);
        this.matrixTemplate = (GenotypeMatrixTemplateDTO) getLoaderTemplate(
            uploadRequest.getGenotypeTemplateId(), GenotypeMatrixTemplateDTO.class);
    }

 
    public DigesterResult digest() throws GobiiException {        

        List<File> filesToDigest = new ArrayList<>();
        Map<String, File> intermediateDigestFileMap = new HashMap<>();

        try {
            filesToDigest = getFilesToDigest(uploadRequest.getInputFiles());

            if(this.matrixTemplate.isAreLeftLabelsVariantLabels()) {
                processVariantAsLeftLabel(filesToDigest);
            }
            else {
                processVarinatAsTopLabel(filesToDigest);
            }
        }
        catch (Exception e) {
            log.error(e.getMessage());
            throw new GobiiException(e);
        }
        
        // Get the load order from ifl config
        List<String> loadOrder = getLoadOrder(intermediateDigestFileMap.keySet());

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
                .setDatasetType(getDataType())
                .setJobStatusObject(jobStatus)
                .setDatasetId(getDataset().getDatasetId())
                .setJobName(loaderInstruction.getJobName())
                .setContactEmail(loaderInstruction.getContactEmail())
                .build();

        return digesterResult;
    }


    private Map<String, File> processVariantAsLeftLabel(List<File> filesToDigest) throws GobiiException {

        Map<String, File> intermediateDigestFileMap = new HashMap<>();
        Map<String, Table> aspects = new HashMap<>();
        Map<String, Integer> totalLinesWrittenForEachTable = new HashMap<>();
        
        // To keep track of files having uniform dnarun names columns across files
        String[] previousFileHeaders = {};
        
        for(File fileToDigest : filesToDigest) {

            FileHeader fileHeader = getFileHeader(fileToDigest);
            
            if(previousFileHeaders.length == 0) {
                previousFileHeaders = fileHeader.getHeaders();
                
                // Set Dna run aspect only once as dnarun names will be same across 
                // all the files.
                String datasetDnaRunTableName = AspectUtils.getTableName(DatasetDnaRunTable.class);
                aspects.put(datasetDnaRunTableName, getDatasetDnaRunTable(fileHeader));
            }
            else if(!Arrays.equals(fileHeader.getHeaders(), previousFileHeaders)) {
                String errorMessage = "Files dont have same columns";
                log.error(errorMessage, new GobiiException(errorMessage));
            }
           
            // Set dataset_marker table aspect               
            String datasetMarkerTableName = AspectUtils.getTableName(DatasetMarkerTable.class);
            Integer hdf5MarkerIndexStart = 1;
            if(totalLinesWrittenForEachTable.containsKey(datasetMarkerTableName)) {
                hdf5MarkerIndexStart = totalLinesWrittenForEachTable.get(datasetMarkerTableName) + 1;
            }
            DatasetMarkerTable datasetMarkerTable = getDatasetMarkerTable(fileHeader, hdf5MarkerIndexStart);
            aspects.put(datasetMarkerTableName, datasetMarkerTable); 
            
            String dataType = getDataType();
            // Set Transform or normal matrix aspect based on data type
            if(StringUtils.isNotEmpty(dataType) &&
                dataTypeToTransformType.containsKey(dataType)) {
                String matrixTableName = AspectUtils.getTableName(MatrixTransformTable.class);
                aspects.put(matrixTableName, getMatrixTransformTable(fileHeader, dataType));
            }
            else {
                String matrixTableName = AspectUtils.getTableName(MatrixTable.class);
                aspects.put(matrixTableName, getMatrixTable(fileHeader));
            }
            // Masticate and set the output.
            Map<String, MasticatorResult> masticatedFilesMap = masticate(fileToDigest, GobiiFileUtils.TAB_SEP, aspects);

            // Update the intermediate file map incase if there is any new table
            masticatedFilesMap.forEach((table, masticatorResult) -> {
                intermediateDigestFileMap.put(table, masticatorResult.getOutputFile());
                int updatedCount = masticatorResult.getTotalLinesWritten(); 
                if(totalLinesWrittenForEachTable.containsKey(table)) {
                    updatedCount += totalLinesWrittenForEachTable.get(table);
                }
                totalLinesWrittenForEachTable.put(table, updatedCount);
            });

        }
        return intermediateDigestFileMap;
    }


    private Map<String, File> processVarinatAsTopLabel(List<File> filesToDigest) throws GobiiException {
        Map<String, File> intermediateDigestFileMap = new HashMap<>();
        Map<String, Table> aspects = new HashMap<>();
        Map<String, Integer> totalLinesWrittenForEachTable = new HashMap<>();
        
        
        for(File fileToDigest : filesToDigest) {

            FileHeader fileHeader = getFileHeader(fileToDigest);
            String datasetDnaRunTableName = AspectUtils.getTableName(DatasetDnaRunTable.class);
            
            // Set Dna run aspect only once as dnarun names will be same across 
            // all the files. Checks if this is first iteration.
            if(!totalLinesWrittenForEachTable.containsKey(datasetDnaRunTableName)) {
                aspects.put(datasetDnaRunTableName, getDatasetDnaRunTable(fileHeader));
            }
           
            // Set dataset_marker table aspect               
            String datasetMarkerTableName = AspectUtils.getTableName(DatasetMarkerTable.class);
            Integer hdf5MarkerIndexStart = 1;
            if(totalLinesWrittenForEachTable.containsKey(datasetMarkerTableName)) {
                hdf5MarkerIndexStart = totalLinesWrittenForEachTable.get(datasetMarkerTableName) + 1;
            }
            DatasetMarkerTable datasetMarkerTable = getDatasetMarkerTable(fileHeader, hdf5MarkerIndexStart);
            aspects.put(datasetMarkerTableName, datasetMarkerTable); 
            
            String dataType = getDataType();
            String matrixTableName;
            // Set Transform or normal matrix aspect based on data type
            if(StringUtils.isNotEmpty(dataType) && dataTypeToTransformType.containsKey(dataType)) {
                matrixTableName = AspectUtils.getTableName(MatrixTransformTable.class);
                aspects.put(matrixTableName, getMatrixTransformTable(fileHeader, dataType));
            }
            else {
                matrixTableName = AspectUtils.getTableName(MatrixTable.class);
                aspects.put(matrixTableName, getMatrixTable(fileHeader));
            }
            // Masticate and set the output.
            Map<String, MasticatorResult> masticatedFilesMap = masticate(fileToDigest, GobiiFileUtils.TAB_SEP, aspects);

            // Update the intermediate file map incase if there is any new table
            masticatedFilesMap.forEach((table, masticatorResult) -> {

                intermediateDigestFileMap.put(table, masticatorResult.getOutputFile());
                int updatedCount = masticatorResult.getTotalLinesWritten(); 

                // Make sure number of matrix rows equals the num of dnaruns from the first file
                if(table.equals(matrixTableName) 
                    && updatedCount != totalLinesWrittenForEachTable.get(datasetDnaRunTableName)) {
                    throw new GobiiException("Number of dnaruns has to be same across the files.");
                }

                if(totalLinesWrittenForEachTable.containsKey(table)) {
                    updatedCount += totalLinesWrittenForEachTable.get(table);
                }
                totalLinesWrittenForEachTable.put(table, updatedCount);
            });

        }
        return intermediateDigestFileMap;
    }

    private FileHeader getFileHeader(File fileToDigest) throws GobiiException {
        
        FileHeader fileHeader;

        if(this.matrixTemplate.getHeaderLineNumber() != null) {
            fileHeader = this.getFileHeaderByLineNumber(
                fileToDigest, this.matrixTemplate.getHeaderLineNumber());
        }
        else if(this.matrixTemplate.getHeaderStartsWith() != null) {
            fileHeader = this.getFileHeaderByIdentifier(
                fileToDigest, this.matrixTemplate.getHeaderStartsWith());
        }
        else {
            throw new GobiiException("Invalid header identifier");
        }
        return fileHeader;
    }

    private Integer getStartPositionForTopLabels(FileHeader fileHeader) throws GobiiException {

        Integer startIndex = this.matrixTemplate.getTopLabelIdHeaderStartPosition();
        if(startIndex != null && startIndex > 0) {
            startIndex = fileHeader.getHeaderColumnIndex(this.matrixTemplate.getTopLabelIdHeaderColumnName());
        }                

        if(startIndex != null && startIndex > 0) {
            return startIndex;
        }
        else {
            throw new GobiiException("Invalid file header top label start position");
        }
    }
    
    private Integer getStartPositionForLeftLables(FileHeader fileHeader) throws GobiiException {

        Integer startIndex = this.matrixTemplate.getLeftLabelIdHeaderPosition();
        if(startIndex != null && startIndex > 0) {
            startIndex = fileHeader.getHeaderColumnIndex(this.matrixTemplate.getLeftLabelIdHeaderColumnName());
        }                

        if(startIndex != null && startIndex > 0) {
            return startIndex;
        }
        else {
            throw new GobiiException("Invalid file header top label start position");
        }

    }

    private DatasetDnaRunTable getDatasetDnaRunTable(FileHeader fileHeader) throws GobiiException {

        DatasetDnaRunTable datasetDnaRunTable = new DatasetDnaRunTable();
        
        datasetDnaRunTable.setDatasetId(uploadRequest.getDatasetId());

        // Get Experiment id
        Experiment experiment = getExperiment();
        datasetDnaRunTable.setExperimentId(experiment.getExperimentId().toString());
       
        datasetDnaRunTable.setPlatformId(getPlatform().getPlatformId().toString());
        Integer dnaRunNamesStartIndex;      
        if(this.matrixTemplate.isAreLeftLabelsVariantLabels()) {
            dnaRunNamesStartIndex = getStartPositionForTopLabels(fileHeader);
            RowAspect dnaRunNameRow = new RowAspect(fileHeader.getHeaderLineNumber(), dnaRunNamesStartIndex);
            datasetDnaRunTable.setDnaRunName(dnaRunNameRow);
        }
        else {
            dnaRunNamesStartIndex = getStartPositionForLeftLables(fileHeader);
            ColumnAspect dnaRunNameColumn = new ColumnAspect(
                fileHeader.getHeaderLineNumber() + 1, dnaRunNamesStartIndex);
            datasetDnaRunTable.setDnaRunName(dnaRunNameColumn);

        }

        // Set Range aspect for hdf5 index                   
        datasetDnaRunTable.setDatasetDnaRunIdx(new RangeAspect(1));
       
        return datasetDnaRunTable;

    }
    
    private DatasetMarkerTable getDatasetMarkerTable(FileHeader fileHeader, int hdf5Start) throws GobiiException {

        DatasetMarkerTable datasetMarkerTable = new DatasetMarkerTable();

        datasetMarkerTable.setDatasetId(uploadRequest.getDatasetId());

        String platformId = getPlatform().getPlatformId().toString(); 
        datasetMarkerTable.setPlatformId(platformId);

        Integer markerNameStartIndex;

        if(this.matrixTemplate.isAreLeftLabelsVariantLabels()) {
            // Set column aspect for marker name.
            markerNameStartIndex = getStartPositionForLeftLables(fileHeader);

            ColumnAspect markerNameColumn = new ColumnAspect(
                this.matrixTemplate.getHeaderLineNumber() + 1,
                markerNameStartIndex
            );
            datasetMarkerTable.setMarkerName(markerNameColumn);
        }
        else {
            
            // Set row aspect for marker name.
            markerNameStartIndex = this.matrixTemplate.getTopLabelIdHeaderStartPosition();

            RowAspect markerNameRow = new RowAspect(
                this.matrixTemplate.getHeaderLineNumber(),
                markerNameStartIndex
            );
            datasetMarkerTable.setMarkerName(markerNameRow);

        }
        
        // Set range aspect for hdf5 index.
        datasetMarkerTable.setDatasetMarkerIdx(new RangeAspect(hdf5Start));
        
        return datasetMarkerTable;
    }
    
    private MatrixTransformTable getMatrixTransformTable(
        FileHeader fileHeader, String dataType) throws GobiiException {

        Integer matrixDataStartIndex = getStartPositionForTopLabels(fileHeader);
        MatrixTransformTable matrixTable = new MatrixTransformTable();
        MatrixTransformAspect matrixAspect = new MatrixTransformAspect(
            dataTypeToTransformType.get(dataType),
            fileHeader.getHeaderLineNumber() + 1, 
            matrixDataStartIndex);
        matrixTable.setMatrix(matrixAspect);
        return matrixTable;
    }

    private MatrixTable getMatrixTable(FileHeader fileHeader) throws GobiiException {

        MatrixTable matrixTable = new MatrixTable();
        Integer matrixDataStartIndex = getStartPositionForTopLabels(fileHeader);
        MatrixAspect matrixAspect = new MatrixAspect(fileHeader.getHeaderLineNumber() + 1, matrixDataStartIndex);
        matrixTable.setMatrix(matrixAspect);
        return matrixTable;
    }

}
