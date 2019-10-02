package org.gobiiproject.gobidomain.services.impl.sampletracking;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobidomain.services.ContactService;
import org.gobiiproject.gobidomain.services.ExperimentService;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.entity.auditable.sampletracking.ExperimentDTO;
import org.gobiiproject.gobiimodel.entity.Experiment;
import org.gobiiproject.gobiimodel.modelmapper.ModelMapper;
import org.gobiiproject.gobiisampletrackingdao.CvDao;
import org.gobiiproject.gobiisampletrackingdao.ExperimentDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class ExperimentServiceImpl implements ExperimentService<ExperimentDTO> {

    Logger LOGGER = LoggerFactory.getLogger(ExperimentServiceImpl.class);

    @Autowired
    private ContactService contactService;


    @Autowired
    private ExperimentDao experimentDao;

    @Autowired
    private CvDao cvDao;


    @Override
    public ExperimentDTO createExperiment(ExperimentDTO newExperimentDTO) throws GobiiDomainException {

        ExperimentDTO returnVal = new ExperimentDTO();

        try {

            Experiment experiment = new Experiment();

            ModelMapper.mapDtoToEntity(newExperimentDTO, experiment);



        }
        catch(GobiiException gE) {
            LOGGER.error(gE.getMessage(), gE.getMessage());
            throw new GobiiDomainException(
                    gE.getGobiiStatusLevel(),
                    gE.getGobiiValidationStatusType(),
                    gE.getMessage()
            );
        }
        catch(Exception e) {
            LOGGER.error("Gobii service error", e);
            throw new GobiiDomainException(e);
        }

        return returnVal;
    }

    @Override
    public ExperimentDTO replaceExperiment(Integer experimentId, ExperimentDTO experimentDTO) throws GobiiDomainException {
        return experimentDTO;
    }

    @Override
    public List<ExperimentDTO> getExperiments() throws GobiiDomainException {
        List<ExperimentDTO> returnVal = new ArrayList<>();


        return returnVal;
    }

    @Override
    public ExperimentDTO getExperimentById(Integer experimentId) throws GobiiDomainException {

        ExperimentDTO returnVal = new ExperimentDTO();


        return returnVal;

    }

    @Override
    public List<ExperimentDTO> getExperimentsByProjectIdForLoadedDatasets(Integer projectId) throws GobiiDomainException {
        List<ExperimentDTO> returnVal = null;

        return returnVal;
    }

}
