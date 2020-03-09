package org.gobiiproject.gobidomain.services.brapi;

import org.gobiiproject.gobiimodel.dto.brapi.VariantSetDTO;

import java.util.List;

public interface VariantSetsService {

    List<VariantSetDTO> getVariantSets(Integer pageNum, Integer pageSize,
                                        Integer variantSetDbId);

    VariantSetDTO getVariantSetById(Integer variantSetDbId);

}
