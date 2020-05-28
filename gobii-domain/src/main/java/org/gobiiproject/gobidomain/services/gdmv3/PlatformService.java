package org.gobiiproject.gobidomain.services.gdmv3;

import org.gobiiproject.gobiimodel.dto.gdmv3.PlatformDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;

public interface PlatformService {

	PlatformDTO createPlatform(PlatformDTO request, String createdBy) throws Exception;

	PagedResult<PlatformDTO> getPlatforms(Integer page, Integer pageSize, Integer platformTypeId) throws Exception;

	PlatformDTO getPlatform(Integer platformId) throws Exception;
    
}