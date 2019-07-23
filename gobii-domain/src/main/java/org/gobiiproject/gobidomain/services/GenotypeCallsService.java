package org.gobiiproject.gobidomain.services;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.GenotypeCallsDTO;

import java.util.List;

public interface GenotypeCallsService {

    List<GenotypeCallsDTO> getGenotypeCallsByDnarunId(
            Integer dnarunId, String pageToken,
            Integer pageSize) throws GobiiDomainException;

    List<GenotypeCallsDTO> getGenotypeCallsByMarkerId(
            Integer markerId, String pageToken,
            Integer pageSize) throws GobiiDomainException;

    List<GenotypeCallsDTO> getGenotypeCallsByDatasetId(
            Integer datasetId, String pageToken,
            Integer pageSize) throws GobiiDomainException;

    List<GenotypeCallsDTO> getGenotypeCallsByExtractQuery(
            String extractQueryFilePath, String pageToken,
            Integer pageSize) throws GobiiDomainException;

    String getGenotypeCallsAsString(Integer datasetId, String pageToken);

    String getNextPageToken();

}
