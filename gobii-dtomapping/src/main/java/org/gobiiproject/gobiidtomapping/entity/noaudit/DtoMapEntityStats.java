package org.gobiiproject.gobiidtomapping.entity.noaudit;

import java.util.List;
import org.gobiiproject.gobiidtomapping.core.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.dto.system.EntityStatsDTO;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;

/**
 * Created by Angel on 4/29/2016.
 */
public interface DtoMapEntityStats {

    List<EntityStatsDTO> getAll() throws GobiiDtoMappingException;
    EntityStatsDTO getEntityLastModified(GobiiEntityNameType gobiiEntityNameType) throws GobiiDtoMappingException;
    EntityStatsDTO getEntityCount(GobiiEntityNameType gobiiEntityNameType) throws GobiiDtoMappingException;
    EntityStatsDTO getEntityCountOfChildren(GobiiEntityNameType gobiiEntityNameTypeParent,
                             Integer parentId,
                             GobiiEntityNameType gobiiEntityNameTypeChild ) throws GobiiDtoMappingException;
}
