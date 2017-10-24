package org.gobiiproject.gobiidtomapping.entity.auditable;

import org.gobiiproject.gobiidtomapping.core.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.headerlesscontainer.PlatformDTO;

import java.util.List;

/**
 * Created by Phil on 4/27/2016.
 */
public interface DtoMapPlatform extends DtoMap<PlatformDTO> {

    PlatformDTO create(PlatformDTO platformDTO) throws GobiiDtoMappingException;
    PlatformDTO replace(Integer platformId, PlatformDTO platformDTO) throws GobiiDtoMappingException;
    PlatformDTO get(Integer platformId) throws GobiiDtoMappingException;
    List<PlatformDTO> getList() throws GobiiDtoMappingException;

}
