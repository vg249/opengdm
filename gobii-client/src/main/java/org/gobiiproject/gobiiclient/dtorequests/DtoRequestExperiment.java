// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests;

import org.gobiiproject.gobiiclient.core.restmethods.dtopost.DtoRequestProcessor;
import org.gobiiproject.gobiimodel.dto.container.ExperimentDTO;
import org.gobiiproject.gobiiapimodel.types.ControllerType;
import org.gobiiproject.gobiiapimodel.types.ServiceRequestId;

public class DtoRequestExperiment {

    public ExperimentDTO process(ExperimentDTO experimentDTO) throws Exception {

        return new DtoRequestProcessor<ExperimentDTO>().process(experimentDTO,
                ExperimentDTO.class,
                ControllerType.LOADER,
                ServiceRequestId.URL_EXPERIMENT);

    } // getPing()


}
