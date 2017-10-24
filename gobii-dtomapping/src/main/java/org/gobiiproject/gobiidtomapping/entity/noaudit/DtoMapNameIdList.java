package org.gobiiproject.gobiidtomapping.entity.noaudit;

import org.gobiiproject.gobiidtomapping.entity.noaudit.impl.DtoMapNameIds.DtoMapNameIdParams;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.entity.children.NameIdDTO;

import java.util.List;

/**
 * Created by Phil on 4/6/2016.
 */
public interface DtoMapNameIdList {
    List<NameIdDTO> getNameIdList(DtoMapNameIdParams dtoMapNameIdParams) throws GobiiException;
}
