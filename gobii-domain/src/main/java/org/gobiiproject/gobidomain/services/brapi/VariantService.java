package org.gobiiproject.gobidomain.services.brapi;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.dto.brapi.GenotypeCallsSearchQueryDTO;
import org.gobiiproject.gobiimodel.dto.brapi.VariantDTO;
import org.gobiiproject.gobiimodel.dto.brapi.VariantsSearchQueryDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;
import org.gobiiproject.gobiimodel.entity.Marker;

import java.util.List;

public interface VariantService {

    PagedResult<VariantDTO> getVariants(Integer pageSize, String pageToken,
                                        Integer variantDbId, Integer variantSetDbId);

    VariantDTO getVariantByVariantDbId(Integer variantDbId);

    PagedResult<VariantDTO> getVariantsByGenotypesExtractQuery(
        GenotypeCallsSearchQueryDTO genotypesSearchQuery,
        Integer pageSize,
        String pageToken) throws GobiiDomainException;

    PagedResult<VariantDTO> getVariantsByVariantSearchQuery(
        VariantsSearchQueryDTO variantsSearchQuery,
        Integer pageSize, String pageToken) throws GobiiDomainException;

}
