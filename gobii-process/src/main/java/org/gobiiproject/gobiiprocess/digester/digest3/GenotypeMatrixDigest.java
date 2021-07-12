package org.gobiiproject.gobiiprocess.digester.digest3;

import org.apache.commons.lang.ArrayUtils;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.gdmv3.GenotypeUploadRequestDTO;
import org.gobiiproject.gobiimodel.dto.instructions.loader.DigesterResult;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.ColumnAspect;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.DatasetDnaRunTable;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.DatasetMarkerTable;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.LoaderInstruction3;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.RangeAspect;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.RowAspect;
import org.gobiiproject.gobiimodel.entity.Dataset;
import org.gobiiproject.gobiimodel.entity.Experiment;
import org.gobiiproject.gobiimodel.entity.Platform;
import org.gobiiproject.gobiiprocess.spring.SpringContextLoaderSingleton;
import org.gobiiproject.gobiisampletrackingdao.DatasetDao;
import org.gobiiproject.gobiisampletrackingdao.ExperimentDao;

public abstract class GenotypeMatrixDigest extends Digest3 {
   
    
    protected DatasetDao datasetDao;
    protected ExperimentDao experimentDao;
    protected GenotypeUploadRequestDTO uploadRequest;
    protected Dataset dataset;
    protected Experiment experiment;
    protected Platform platform;
    
    public GenotypeMatrixDigest(
        LoaderInstruction3 loaderInstruction3,
        ConfigSettings configSettings) throws GobiiException {
        super(loaderInstruction3, configSettings);
        this.uploadRequest = 
            mapper.convertValue(loaderInstruction.getUserRequest(), 
                                GenotypeUploadRequestDTO.class);                    
        this.datasetDao = SpringContextLoaderSingleton.getInstance().getBean(DatasetDao.class);
        this.experimentDao = SpringContextLoaderSingleton.getInstance().getBean(ExperimentDao.class);
        if(uploadRequest.getDatasetId() == null) {
            throw new GobiiException("Required Dataset Id");
        }
    }

    abstract public DigesterResult digest();
    
    protected Dataset getDataset() throws GobiiException {
       if(dataset == null) {
           Integer datasetId;
           try {
               datasetId = Integer.parseInt(uploadRequest.getDatasetId());
           }
           catch(NumberFormatException e) {
               throw new GobiiException("Invalid dataset id");
           }
           dataset = datasetDao.getDataset(datasetId);
        }
        return dataset;
    }

    protected Experiment getExperiment() throws GobiiException {
        if (experiment == null) {
            experiment = experimentDao.getExperiment(getDataset().getExperiment().getExperimentId());
        }
        return experiment;
    }

    protected Platform getPlatform() {
        return getExperiment().getVendorProtocol().getProtocol().getPlatform();
    }

    protected String getDataType() {
        // Type is not a required property.
        if(getDataset().getType() == null) {
            return null;
        }
        return getDataset().getType().getTerm();
    }

    /**
     * Gets the Aspect table for loading MarkerTable with dataset index.
     * 
     * @param headers Array of header tokens
     * @param headerLineNumber Line number of header in the file
     * @param indexColumnHeaderPosition Index of column in header which from which marker table index needs to be
     * extracted
     * @param indexColumnHeaderName To identify index column by its header name
     * @param hdf5Start Start index of hdf5 rows
     * @param isColumnOriented Is the index data is row oriented or column oriented.
     * 
     * @return {@link DatasetMarkerTable} object. 
     */
    protected DatasetMarkerTable getDatasetMarkerTable(
        String[] headers, 
        int headerLineNumber,
        Integer indexColumnHeaderPosition,
        String indexColumnHeaderName,
        int hdf5Start,
        boolean isColumnOriented) {

        DatasetMarkerTable datasetMarkerTable = new DatasetMarkerTable();

        datasetMarkerTable.setDatasetId(uploadRequest.getDatasetId());

        String platformId = getPlatform().getPlatformId().toString(); 
        datasetMarkerTable.setPlatformId(platformId);

        if(indexColumnHeaderPosition != null) {
            indexColumnHeaderPosition = ArrayUtils.indexOf(headers, indexColumnHeaderName);  
        }

        if(isColumnOriented) {
            // data lines starts after header line
            ColumnAspect markerNameColumn = new ColumnAspect(headerLineNumber+1, indexColumnHeaderPosition);
            datasetMarkerTable.setMarkerName(markerNameColumn);
        }
        else {
            RowAspect markerNameRow = new RowAspect(headerLineNumber, indexColumnHeaderPosition);
            datasetMarkerTable.setMarkerName(markerNameRow);
        }
        
        // Set range aspect for hdf5 index.
        datasetMarkerTable.setDatasetMarkerIdx(new RangeAspect(hdf5Start));
        
        return datasetMarkerTable;
    }

    protected DatasetDnaRunTable getDatasetDnaRunTable(
        String[] headers, 
        int headerLineNumber,
        Integer indexColumnHeaderPosition,
        String indexColumnHeaderName,
        int hdf5Start,
        boolean isColumnOriented) throws GobiiException {
        
        DatasetDnaRunTable datasetDnaRunTable = new DatasetDnaRunTable();
        
        datasetDnaRunTable.setDatasetId(uploadRequest.getDatasetId());

        // Get Experiment id
        Experiment experiment = getExperiment();
        datasetDnaRunTable.setExperimentId(experiment.getExperimentId().toString());
       
        datasetDnaRunTable.setPlatformId(getPlatform().getPlatformId().toString());
       
        if(indexColumnHeaderPosition != null) {
            indexColumnHeaderPosition = ArrayUtils.indexOf(headers, indexColumnHeaderName);  
        }
        
        if(isColumnOriented) {
            // data lines starts after header line
            ColumnAspect dnaRunNameColumn = new ColumnAspect(headerLineNumber+1, indexColumnHeaderPosition);
            datasetDnaRunTable.setDnaRunName(dnaRunNameColumn);
        }
        else {
            RowAspect dnaRunNameRow = new RowAspect(headerLineNumber, indexColumnHeaderPosition);
            datasetDnaRunTable.setDnaRunName(dnaRunNameRow);
        }
        
        // Set Range aspect for hdf5 index                   
        datasetDnaRunTable.setDatasetDnaRunIdx(new RangeAspect(hdf5Start));
       
        return datasetDnaRunTable;

    }

}
