package org.gobiiproject.gobidomain.services.gdmv3;

import org.gobiiproject.gobiimodel.dto.gdmv3.CropsDTO;

import java.util.List;

public interface CropService {

    List<CropsDTO> getCrops();
}
