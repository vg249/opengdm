package org.gobiiproject.gobiidomain.services.brapi;

import java.math.BigDecimal;

import org.gobiiproject.gobiidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.dto.brapi.GenotypeCallsMatrixResult;
import org.gobiiproject.gobiimodel.dto.brapi.GenotypeCallsResult;
import org.gobiiproject.gobiimodel.dto.brapi.GenotypeCallsSearchQueryDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedResultTyped;

public interface GenotypeCallsService {

    PagedResultTyped<GenotypeCallsResult> getGenotypeCallsByCallSetId(
        Integer callSetDbId, Integer pageSize, String pageToken
    ) throws GobiiDomainException;

    PagedResultTyped<GenotypeCallsResult> getGenotypeCallsByVariantDbId(
        Integer markerId, Integer pageSize, String pageToken
    ) throws GobiiDomainException;

    PagedResultTyped<GenotypeCallsResult> getGenotypeCallsByVariantSetDbId(
        Integer datasetId,
        String mapsetName,
        String linkageGroupName,
        BigDecimal minPosition,
        BigDecimal maxPosition,
        Integer pageSize,
        String pageToken) throws GobiiDomainException;
    
    PagedResultTyped<GenotypeCallsMatrixResult> getGenotypeCallsMatrix(
        Integer datasetId,
        Integer pageSize, 
        Integer page, 
        Integer columnBin,
        Integer columnBinSize) throws GobiiDomainException;

    PagedResultTyped<GenotypeCallsMatrixResult> getGenotypeCallsByExtractQuery(
        GenotypeCallsSearchQueryDTO genotypesSearchQuery,
        Integer pageSize, 
        Integer page, 
        Integer columnBin,
        Integer columnBinSize) throws GobiiDomainException;



    String getGenotypeCallsAsString(Integer datasetId, Integer pageNum);

}
