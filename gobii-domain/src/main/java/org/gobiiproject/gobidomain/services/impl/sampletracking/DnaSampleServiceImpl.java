package org.gobiiproject.gobidomain.services.impl.sampletracking;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobidomain.services.DnaSampleService;
import org.gobiiproject.gobiidtomapping.entity.auditable.sampletracking.DtoMapDnaSample;
import org.gobiiproject.gobiimodel.dto.entity.auditable.sampletracking.DnaSampleDTO;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.ProjectSamplesDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by VCalaminos on 5/2/2019.
 */
public class DnaSampleServiceImpl {

    Logger LOGGER = LoggerFactory.getLogger(DnaSampleServiceImpl.class);

    //@Autowired
    //private DtoMapDnaSample dtoMapSampleTrackingDnaSample = null;

    public ProjectSamplesDTO createSamples(ProjectSamplesDTO projectSamplesDTO)  throws GobiiDomainException {

        ProjectSamplesDTO returnVal = null;

        try {

            //returnVal = dtoMapSampleTrackingDnaSample.createSamples(projectSamplesDTO);
            return projectSamplesDTO;

        } catch (Exception e) {
            LOGGER.error("Gobii service error", e);
            throw new GobiiDomainException(e);
        }

    }


}
