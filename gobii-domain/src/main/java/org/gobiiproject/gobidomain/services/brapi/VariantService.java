package org.gobiiproject.gobidomain.services.brapi;

import org.gobiiproject.gobiimodel.dto.brapi.VariantDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;

public interface VariantService {

    PagedResult<VariantDTO> getVariants(Integer pageSize, String pageToken,
                                        Integer variantDbId, Integer variantSetDbId);

    VariantDTO getVariantByVariantDbId(Integer variantDbId);

}
