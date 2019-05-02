package org.gobiiproject.gobidomain.services.impl.sampletracking;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobidomain.services.DnaSampleService;
import org.gobiiproject.gobiidtomapping.entity.auditable.sampletracking.DtoMapDnaSample;
import org.gobiiproject.gobiimodel.dto.entity.auditable.sampletracking.DnaSampleDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by VCalaminos on 5/2/2019.
 */
public class DnaSampleServiceImpl implements DnaSampleService<DnaSampleDTO> {

    Logger LOGGER = LoggerFactory.getLogger(DnaSampleServiceImpl.class);

    @Autowired
    private DtoMapDnaSample dtoMapSampleTrackingDnaSample = null;

    @Override
    public List<DnaSampleDTO> createSamples(List<DnaSampleDTO> dnaSampleDTOList)  throws GobiiDomainException {

        List<DnaSampleDTO> returnVal = null;

        try {

            returnVal = dtoMapSampleTrackingDnaSample.createSamples(dnaSampleDTOList);

        } catch (Exception e) {
            LOGGER.error("Gobii service error", e);
            throw new GobiiDomainException(e);
        }


        return returnVal;

    }


}
