package org.gobiiproject.gobiiprocess.digester.digest3;

import static org.mockito.ArgumentMatchers.booleanThat;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.gdmv3.GenotypeUploadRequestDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.templates.GenotypeMatrixTemplateDTO;
import org.gobiiproject.gobiimodel.dto.instructions.loader.DigesterResult;
import org.gobiiproject.gobiimodel.dto.instructions.loader.MasticatorResult;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.ColumnAspect;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.DatasetDnaRunTable;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.DatasetMarkerTable;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.DnaRunTable;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.DnaSampleTable;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.FileHeader;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.GermplasmTable;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.LoaderInstruction3;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.MarkerTable;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.MatrixAspect;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.MatrixTable;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.MatrixTransformAspect;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.MatrixTransformTable;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.RangeAspect;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.RowAspect;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.Table;
import org.gobiiproject.gobiimodel.entity.Dataset;
import org.gobiiproject.gobiimodel.entity.Experiment;
import org.gobiiproject.gobiimodel.entity.Project;
import org.gobiiproject.gobiimodel.utils.AspectUtils;
import org.gobiiproject.gobiimodel.utils.GobiiFileUtils;
import org.gobiiproject.gobiiprocess.spring.SpringContextLoaderSingleton;
import org.gobiiproject.gobiisampletrackingdao.DatasetDao;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GenericMatrixDigest extends GenotypeMatrixDigest {

    
    private GenotypeUploadRequestDTO uploadRequest;
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
            for(File fileToDigest : filesToDigest) {
                // Ignore non text file
                if(!GobiiFileUtils.isFileTextFile(fileToDigest)) {
                    continue;
                }

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
                
                if(this.matrixTemplate.isAreLeftLabelsVariantLabels()) {
                    String datasetDnaRunTableName = AspectUtils.getTableName(DatasetDnaRunTable.class);
                    boolean isDnaRunIdColumnOriented = true;
                    if(this.matrixTemplate.isAreLeftLabelsVariantLabels()) {
                        isDnaRunIdColumnOriented = false;
                    }
                    aspects.put(
                        datasetDnaRunTableName, 
                        this.getDatasetDnaRunTable(
                            fileHeader.getHeaders(),
                            this.matrixTemplate.getHeaderLineNumber(), 
                            this.matrixTemplate.getLeftLabelIdHeaderPosition(),
                            this.matrixTemplate.getLeftLabelIdHeaderColumnName(),
                            1,
                            this.matrixTemplate.isAreLeftLabelsVariantLabels()));
                }
                else {
                    String datasetMarkerTableName = AspectUtils.getTableName(DatasetMarkerTable.class);
                    aspects.put(
                        datasetMarkerTableName, 
                        this.getDatasetMarkerTable(
                            fileHeader.getHeaders(),
                            this.matrixTemplate.getHeaderLineNumber(), 
                            this.matrixTemplate.getLeftLabelIdHeaderPosition(),
                            this.matrixTemplate.getLeftLabelIdHeaderColumnName(),
                            1,
                            this.matrixTemplate.isAreLeftLabelsVariantLabels()));

                }
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
                .setDatasetType(getDataType())
                .setJobStatusObject(jobStatus)
                .setDatasetId(getDataset().getDatasetId())
                .setJobName(loaderInstruction.getJobName())
                .setContactEmail(loaderInstruction.getContactEmail())
                .build();

        return digesterResult;
    }



}
