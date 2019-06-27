package org.gobiiproject.gobidomain.services;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.GenotypeCallsDTO;

import java.util.List;

public interface GenotypeCallsService {

    List<GenotypeCallsDTO> getGenotypeCallsByDnarunId(
            Integer dnarunId,
            Integer pageToken,
            Integer pageSize) throws GobiiDomainException;

}
