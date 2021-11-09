package org.gobiiproject.gobiiprocess.digester.digest3;

import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.gdmv3.GenotypeUploadRequestDTO;
import org.gobiiproject.gobiimodel.dto.instructions.loader.DigesterResult;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.LoaderInstruction3;
import org.gobiiproject.gobiimodel.entity.Dataset;
import org.gobiiproject.gobiimodel.entity.Experiment;
import org.gobiiproject.gobiimodel.entity.Platform;
import org.gobiiproject.gobiimodel.entity.Protocol;
import org.gobiiproject.gobiimodel.entity.VendorProtocol;
import org.gobiiproject.gobiiprocess.spring.SpringContextLoaderSingleton;
import org.gobiiproject.gobiisampletrackingdao.DatasetDao;

public abstract class GenotypeMatrixDigest extends Digest3 {
   
    
    protected GenotypeUploadRequestDTO uploadRequest;
    protected Dataset dataset;
    protected Experiment experiment;
    protected String datasetType;
    protected Platform platform;
    
    /**
     * Construct Matrix digest.
     */
    public GenotypeMatrixDigest(LoaderInstruction3 loaderInstruction3, 
        ConfigSettings configSettings) throws GobiiException {

        super(loaderInstruction3, configSettings);

        this.uploadRequest = mapper.convertValue(loaderInstruction.getUserRequest(), GenotypeUploadRequestDTO.class);

        // Get dataset
        if(uploadRequest.getDatasetId() == null) {
            throw new GobiiException("Required Dataset Id");
        }

        Integer datasetId;
        try {
            datasetId = Integer.parseInt(uploadRequest.getDatasetId());
        }
        catch(NumberFormatException e) {
            throw new GobiiException("Invalid dataset id");
        }
        DatasetDao datasetDao = SpringContextLoaderSingleton.getInstance().getBean(DatasetDao.class);

        this.dataset = datasetDao.getDataset(datasetId);
        this.experiment = this.dataset.getExperiment();
        this.datasetType = this.dataset.getType().getTerm();
        VendorProtocol vendorProtocol = this.experiment.getVendorProtocol();
        Protocol protocol = vendorProtocol.getProtocol();

        this.platform = protocol.getPlatform();
        
    }

    abstract public DigesterResult digest();

}
