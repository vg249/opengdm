package org.gobiiproject.gobidomain.services.gdmv3;

import org.gobiiproject.gobiimodel.dto.gdmv3.CropsDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;

import java.util.List;

public interface CropService {

    PagedResult<CropsDTO> getCrops();
}
