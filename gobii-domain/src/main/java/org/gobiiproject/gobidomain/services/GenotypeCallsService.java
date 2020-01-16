package org.gobiiproject.gobidomain.services;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.GenotypeCallsDTO;

import java.util.List;
import java.util.Map;

public interface GenotypeCallsService {

    List<GenotypeCallsDTO> getGenotypeCallsByCallSetId(
            Integer callSetDbId, Integer pageSize,
            String pageToken) throws GobiiDomainException;

    List<GenotypeCallsDTO> getGenotypeCallsByMarkerId(
            Integer markerId, String pageToken,
            Integer pageSize) throws GobiiDomainException;

    List<GenotypeCallsDTO> getGenotypeCallsByDatasetId(
            Integer datasetId, String pageToken,
            Integer pageSize) throws GobiiDomainException;

    List<GenotypeCallsDTO> getGenotypeCallsByExtractQuery(
            String extractQueryFilePath, String pageToken,
            Integer pageSize) throws GobiiDomainException;

    Map<String, String> getGenotypeCallsAsString(Integer datasetId, String pageToken);

    String getNextPageToken();

}
