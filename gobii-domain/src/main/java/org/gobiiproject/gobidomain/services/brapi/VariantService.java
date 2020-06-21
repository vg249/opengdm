package org.gobiiproject.gobidomain.services.brapi;

import org.gobiiproject.gobiimodel.dto.brapi.VariantDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;
import org.gobiiproject.gobiimodel.entity.Marker;

import java.util.List;

public interface VariantService {

    PagedResult<VariantDTO> getVariants(Integer pageSize, String pageToken,
                                        Integer variantDbId, Integer variantSetDbId);

    VariantDTO getVariantByVariantDbId(Integer variantDbId);

    List<VariantDTO> mapMarkersToVariant(List<Marker> markers);

}
