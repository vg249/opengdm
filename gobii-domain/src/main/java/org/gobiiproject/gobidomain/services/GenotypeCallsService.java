package org.gobiiproject.gobidomain.services;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.GenotypeCallsDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;

import java.util.List;
import java.util.Map;

public interface GenotypeCallsService {

    PagedResult<GenotypeCallsDTO> getGenotypeCallsByCallSetId(
            Integer callSetDbId, Integer pageSize,
            String pageToken) throws GobiiDomainException;

    PagedResult<GenotypeCallsDTO> getGenotypeCallsByVariantDbId(
            Integer markerId, String pageToken,
            Integer pageSize) throws GobiiDomainException;

    List<GenotypeCallsDTO> getGenotypeCallsByDatasetId(
            Integer datasetId, String pageToken,
            Integer pageSize) throws GobiiDomainException;

    List<GenotypeCallsDTO> getGenotypeCallsByExtractQuery(
            String extractQueryFilePath, String pageToken,
            Integer pageSize) throws GobiiDomainException;

    Map<String, String> getGenotypeCallsAsString(Integer datasetId, String pageToken);

}
