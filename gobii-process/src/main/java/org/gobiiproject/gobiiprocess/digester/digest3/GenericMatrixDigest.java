package org.gobiiproject.gobiiprocess.digester.digest3;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.gdmv3.templates.GenotypeMatrixTemplateDTO;
import org.gobiiproject.gobiimodel.dto.instructions.loader.DigesterResult;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.DatasetDnaRunTable;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.FileHeader;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.LoaderInstruction3;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.RangeAspect;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.RowAspect;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.Table;
import org.gobiiproject.gobiimodel.entity.Experiment;
import org.gobiiproject.gobiimodel.utils.AspectUtils;

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
        Map<String, Integer> totalLinesWrittenForEachTable = new HashMap<>();

        Map<String, Table> aspects = new HashMap<>();
                
        try {

            filesToDigest = getFilesToDigest(uploadRequest.getInputFiles());

            // Digested files are merged for each table.
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
            
        }
        return intermediateDigestFileMap;
    }


    private Map<String, File> processVarinatAsTopLabel() throws GobiiException {
        Map<String, File> intermediateDigestFileMap = new HashMap<>();
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
    
    private DatasetDnaRunTable getDatasetDnaRunTable(FileHeader fileHeader) throws GobiiException {
        
       DatasetDnaRunTable datasetDnaRunTable = new DatasetDnaRunTable();
        
       datasetDnaRunTable.setDatasetId(uploadRequest.getDatasetId());

       // Get Experiment id
       Experiment experiment = getExperiment();
       datasetDnaRunTable.setExperimentId(experiment.getExperimentId().toString());
       
       datasetDnaRunTable.setPlatformId(getPlatform().getPlatformId().toString());

       // Dnarun names starts after required columns in the file
       Integer dnaRunNamesStartIndex = this.matrixTemplate.getTopLabelIdHeaderStartPosition();
       if(dnaRunNamesStartIndex == null || dnaRunNamesStartIndex < 0) {
           dnaRunNamesStartIndex = fileHeader.getHeaderColumnIndex(this.matrixTemplate.getTopLabelIdHeaderColumnName());
       }
       if(dnaRunNamesStartIndex != null && dnaRunNamesStartIndex > 0) {
           RowAspect dnaRunNameRow = new RowAspect(fileHeader.getHeaderLineNumber(), dnaRunNamesStartIndex);
           datasetDnaRunTable.setDnaRunName(dnaRunNameRow);
       }
       else {
           throw new GobiiException("Invalid file header dnarun name start position");
       }
        
       // Set Range aspect for hdf5 index                   
       datasetDnaRunTable.setDatasetDnaRunIdx(new RangeAspect(1));
       
       return datasetDnaRunTable;

    }

}
