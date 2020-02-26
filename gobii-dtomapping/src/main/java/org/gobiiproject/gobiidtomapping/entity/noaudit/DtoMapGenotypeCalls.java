package org.gobiiproject.gobiidtomapping.entity.noaudit;

import org.gobiiproject.gobiimodel.dto.noaudit.*;

import java.util.List;
import java.util.Map;

public interface DtoMapGenotypeCalls {


    List<GenotypeCallsDTO> getGenotypeCallsList(CallSetBrapiDTO dnarun, String pageToken,
                                                Integer pageSize);

    List<GenotypeCallsDTO> getGenotypeCallsList(MarkerBrapiDTO marker, String pageToken,
                                                Integer pageSize);

    List<GenotypeCallsDTO> getGenotypeCallsList(Integer datasetId, String pageToken,
                                                Integer pageSize);

    Map<String, String> getGenotypeCallsAsString(Integer datasetId, String pageToken);

    List<GenotypeCallsDTO> getGenotypeCallsListByExtractQuery(
            String extractQueryFilePath, String pageToken, Integer pageSize);

    String getNextPageOffset();

    String getNextColumnOffset();

}
