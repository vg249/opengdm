package org.gobiiproject.gobidomain.services.brapi;

import org.gobiiproject.gobiimodel.dto.brapi.SamplesDTO;

import java.util.List;

public interface SamplesService {

    List<SamplesDTO> getSamples(Integer pageNum, Integer pageSize,
                                Integer sampleDbId, Integer germplasmDbId,
                                String observationUnitDbId);

}
