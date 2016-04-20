package org.gobiiproject.gobidomain.services.impl;

import org.gobiiproject.gobidomain.services.ExperimentService;
import org.gobiiproject.gobidomain.services.ProjectService;
import org.gobiiproject.gobiidtomapping.DtoMapExperiment;
import org.gobiiproject.gobiidtomapping.DtoMapProject;
import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.dto.container.ExperimentDTO;
import org.gobiiproject.gobiimodel.dto.container.project.ProjectDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Angel on 4/19/2016.
 */
public class ExperimentServiceImpl implements ExperimentService {


    Logger LOGGER = LoggerFactory.getLogger(ExperimentServiceImpl.class);

    @Autowired
    private DtoMapExperiment dtoMapExperiment = null;



	@Override
	public ExperimentDTO getExperiment(ExperimentDTO experimentDTO) {
		// TODO Auto-generated method stub

        ExperimentDTO returnVal = null;
        try {

            returnVal  = dtoMapExperiment.getExperiment(experimentDTO);

        } catch (GobiiDtoMappingException e) {

            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error(e.getMessage());
        }

        return  returnVal;
	}

} // ProjectServiceImpl
