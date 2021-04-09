package org.gobiiproject.gobiiprocess.digester.digest3;

import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.gdmv3.GenotypeUploadRequestDTO;
import org.gobiiproject.gobiimodel.dto.instructions.loader.DigesterResult;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.LoaderInstruction3;
import org.gobiiproject.gobiimodel.entity.Dataset;
import org.gobiiproject.gobiimodel.entity.Experiment;
import org.gobiiproject.gobiimodel.entity.Platform;
import org.gobiiproject.gobiiprocess.spring.SpringContextLoaderSingleton;
import org.gobiiproject.gobiisampletrackingdao.DatasetDao;

public abstract class GenotypeMatrixDigest extends Digest3 {
   
    
    protected DatasetDao datasetDao;
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
        if(uploadRequest.getDatasetId() == null) {
            throw new GobiiException("Required Dataset Id");
        }
    }

    abstract public DigesterResult digest();
    
    protected Dataset getDataset() {
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

    protected Experiment getExperiment() {
        return getDataset().getExperiment();
    }

    protected Platform getPlatform() {
        return getExperiment().getVendorProtocol().getProtocol().getPlatform();
    }

    protected String getDataType() {
        return getDataset().getType().getTerm();
    }

}
