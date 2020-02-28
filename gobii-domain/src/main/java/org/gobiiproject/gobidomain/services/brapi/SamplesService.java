package org.gobiiproject.gobidomain.services.brapi;

import org.gobiiproject.gobiimodel.dto.brapi.SamplesBrapiDTO;

import java.util.List;

public interface SamplesBrapiService {

    List<SamplesBrapiDTO> getSamples(Integer pageNum, Integer pageSize,
                                     Integer sampleDbId, Integer germplasmDbId,
                                     String observationUnitDbId);

}
