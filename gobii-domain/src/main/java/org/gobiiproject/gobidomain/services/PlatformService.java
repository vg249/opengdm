package org.gobiiproject.gobidomain.services;

import java.util.List;
import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.dto.auditable.PlatformDTO;

/**
 * Created by Phil on 4/27/2016.
 */
public interface PlatformService {

    PlatformDTO createPlatform(PlatformDTO platformDTO) throws GobiiDomainException;
    PlatformDTO replacePlatform(Integer platformId, PlatformDTO platformDTO) throws GobiiDomainException;
    List<PlatformDTO> getPlatforms() throws GobiiDomainException;
    PlatformDTO getPlatformById(Integer platformId) throws GobiiDomainException;
    PlatformDTO getPlatformDetailsByVendorProtocolId(Integer vendorProtocolId) throws GobiiDomainException;
}
