package org.gobiiproject.gobiidtomapping;

import org.gobiiproject.gobiidtomapping.impl.DtoMapNameIds.DtoMapNameIdParams;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.container.NameIdDTO;
import org.gobiiproject.gobiimodel.dto.container.NameIdListDTO;

import java.util.List;

/**
 * Created by Phil on 4/6/2016.
 */
public interface DtoMapNameIdList {
    NameIdListDTO getNameIdList(NameIdListDTO nameIdListDTO);

    List<NameIdDTO> getNameIdList(DtoMapNameIdParams dtoMapNameIdParams) throws GobiiException;
}
