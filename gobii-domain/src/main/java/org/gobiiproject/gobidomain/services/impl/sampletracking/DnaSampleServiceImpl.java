package org.gobiiproject.gobidomain.services.impl.sampletracking;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobidomain.services.DnaSampleService;
import org.gobiiproject.gobiimodel.dto.entity.auditable.sampletracking.DnaSampleDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by VCalaminos on 5/2/2019.
 */
public class DnaSampleServiceImpl implements DnaSampleService<DnaSampleDTO> {

    Logger LOGGER = LoggerFactory.getLogger(DnaSampleServiceImpl.class);

    @Override
    public List<DnaSampleDTO> createSamples(List<DnaSampleDTO> dnaSampleDTOList, Integer projectId)  throws GobiiDomainException {

        List<DnaSampleDTO> returnVal = null;

        return returnVal;

    }


}
