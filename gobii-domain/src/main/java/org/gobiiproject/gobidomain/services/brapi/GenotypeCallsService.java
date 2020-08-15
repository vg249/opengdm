package org.gobiiproject.gobidomain.services.brapi;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.dto.brapi.*;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;
import org.gobiiproject.gobiimodel.dto.system.PagedResultTyped;

public interface GenotypeCallsService {

    PagedResultTyped<GenotypeCallsResult> getGenotypeCallsByCallSetId(
        Integer callSetDbId,
        Integer pageSize,
        String pageToken) throws GobiiDomainException;

    PagedResultTyped<GenotypeCallsResult> getGenotypeCallsByVariantDbId(
        Integer markerId,
        Integer pageSize,
        String pageToken) throws GobiiDomainException;

    PagedResultTyped<GenotypeCallsResult> getGenotypeCallsByVariantSetDbId(
        Integer datasetId,
        Integer pageSize,
        String pageToken) throws GobiiDomainException;

    PagedResultTyped<GenotypeCallsResult> getGenotypeCallsByExtractQuery(
        GenotypeCallsSearchQueryDTO genotypesSearchQuery,
        Integer pageSize,
        String pageToken) throws GobiiDomainException;



    String getGenotypeCallsAsString(Integer datasetId, Integer pageNum);

}
