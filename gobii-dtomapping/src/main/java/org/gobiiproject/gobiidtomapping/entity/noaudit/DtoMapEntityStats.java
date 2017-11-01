package org.gobiiproject.gobiidtomapping.entity.noaudit;

import org.gobiiproject.gobiidtomapping.core.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.CvDTO;
import org.gobiiproject.gobiimodel.dto.system.EntityStatsDTO;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;

import java.util.List;

/**
 * Created by Angel on 4/29/2016.
 */
public interface DtoMapEntityStats {

    EntityStatsDTO getEntityLastModified(GobiiEntityNameType gobiiEntityNameType) throws GobiiDtoMappingException;
    EntityStatsDTO getEntityCount(GobiiEntityNameType gobiiEntityNameType) throws GobiiDtoMappingException;
}
