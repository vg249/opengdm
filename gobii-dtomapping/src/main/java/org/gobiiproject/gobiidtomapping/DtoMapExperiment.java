package org.gobiiproject.gobiidtomapping;

import org.gobiiproject.gobiimodel.dto.container.ExperimentDTO;
import org.gobiiproject.gobiimodel.dto.container.project.ProjectDTO;

/**
 * Created by Angel on 4/19/2016.
 */
public interface DtoMapExperiment {
    ExperimentDTO getExperiment( ExperimentDTO experimentDTO) throws GobiiDtoMappingException;
}
