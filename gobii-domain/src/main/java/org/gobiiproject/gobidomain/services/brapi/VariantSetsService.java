package org.gobiiproject.gobidomain.services.brapi;

import org.gobiiproject.gobiimodel.dto.brapi.VariantSetDTO;

import java.util.List;

public interface VariantSetsService {

    //TODO: If cropType only dependency in this service. Leave it like this.
    // If more than one service happens to have cropType dependency, move it to constructor agrument
    // and pass it using dependency injection
    void setCropType(String cropType);

    List<VariantSetDTO> listVariantSets(Integer pageNum, Integer pageSize,
                                        Integer variantSetDbId);

    VariantSetDTO getVariantSetById(Integer variantSetDbId);

}
