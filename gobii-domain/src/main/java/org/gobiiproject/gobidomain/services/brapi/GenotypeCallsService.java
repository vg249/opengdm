package org.gobiiproject.gobidomain.services.brapi;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.dto.brapi.GenotypeCallsDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;

import java.util.List;
import java.util.Map;

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

    List<GenotypeCallsDTO> getGenotypeCallsByExtractQuery(
            String extractQueryFilePath,
            Integer pageSize,
            String pageToken) throws GobiiDomainException;

    Map<String, String> getGenotypeCallsAsString(Integer datasetId, Integer markerRowOffset);

}
