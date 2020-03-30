package org.gobiiproject.gobidomain.services.brapi;

import java.util.List;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.dto.brapi.GenotypeCallsDTO;
import org.gobiiproject.gobiimodel.dto.noaudit.GenotypeCallsSearchQueryDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;

public interface GenotypeCallsService {

    PagedResult<GenotypeCallsDTO> getGenotypeCallsByCallSetId(
            Integer callSetDbId,
            Integer pageSize,
            String pageToken) throws GobiiDomainException;

    PagedResult<GenotypeCallsDTO> getGenotypeCallsByVariantDbId(
            Integer markerId,
            Integer pageSize,
            String pageToken) throws GobiiDomainException;

    PagedResult<GenotypeCallsDTO> getGenotypeCallsByVariantSetDbId(
            Integer datasetId,
            Integer pageSize,
            String pageToken) throws GobiiDomainException;

    PagedResult<GenotypeCallsDTO> getGenotypeCallsByExtractQuery(
            GenotypeCallsSearchQueryDTO genotypesSearchQuery,
            Integer pageSize,
            String pageToken) throws GobiiDomainException;

    String getGenotypeCallsAsString(Integer datasetId, Integer pageNum);

}
