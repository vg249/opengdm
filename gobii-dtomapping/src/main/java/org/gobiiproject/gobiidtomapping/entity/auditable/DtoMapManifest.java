
package org.gobiiproject.gobiidtomapping.entity.auditable;

import org.gobiiproject.gobiidtomapping.core.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.dto.entity.auditable.ManifestDTO;

import java.util.List;

/**
 * Created by Angel on 5/4/2016.
 * Modified by Yanii on 1/27/2017
 */
public interface DtoMapManifest extends DtoMap<ManifestDTO> {

    ManifestDTO create(ManifestDTO manifestDTO) throws GobiiDtoMappingException;
    ManifestDTO replace(Integer manifestId, ManifestDTO manifestDTO) throws GobiiDtoMappingException;
    ManifestDTO get(Integer manifestId) throws GobiiDtoMappingException;
    List<ManifestDTO> getList() throws GobiiDtoMappingException;

}
