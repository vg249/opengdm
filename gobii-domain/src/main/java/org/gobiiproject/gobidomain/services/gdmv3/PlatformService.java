package org.gobiiproject.gobidomain.services.gdmv3;

import org.gobiiproject.gobiimodel.dto.gdmv3.PlatformDTO;

public interface PlatformService {

	PlatformDTO createPlatform(PlatformDTO request, String createdBy) throws Exception;
    
}