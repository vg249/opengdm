package org.gobiiproject.gobiidtomapping.entity.auditable.impl.sampletracking;

import org.gobiiproject.gobiidtomapping.core.GobiiDtoMappingException;
import org.gobiiproject.gobiidtomapping.entity.auditable.sampletracking.DtoMapDnaSample;
import org.gobiiproject.gobiimodel.dto.entity.auditable.sampletracking.DnaSampleDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by VCalaminos on 5/2/2019.
 */
public class DtoMapDnaSampleImpl implements DtoMapDnaSample{

    Logger LOGGER = LoggerFactory.getLogger(DtoMapDnaSampleImpl.class);

    @Override
    public List<DnaSampleDTO> createSamples(List<DnaSampleDTO> dnaSampleDTOList, Integer projectId) throws GobiiDtoMappingException {

        List<DnaSampleDTO> returnVal = null;

        return  returnVal;

    }

    public DnaSampleDTO replace(Integer dnaSampleId, DnaSampleDTO dnaSampleDTO) throws GobiiDtoMappingException {

        DnaSampleDTO returnVal = null;

        return returnVal;

    }

    public DnaSampleDTO get(Integer dnaSampleId) throws GobiiDtoMappingException {

        DnaSampleDTO returnVal = null;

        return returnVal;

    }

    public List<DnaSampleDTO> getList() throws GobiiDtoMappingException {

        List<DnaSampleDTO> returnVal = null;

        return returnVal;

    }

    public DnaSampleDTO create(DnaSampleDTO dnaSampleDTO) throws GobiiDtoMappingException {

        return dnaSampleDTO;

    }


}
