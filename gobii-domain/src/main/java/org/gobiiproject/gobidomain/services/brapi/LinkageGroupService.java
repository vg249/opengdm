package org.gobiiproject.gobidomain.services.brapi;

import org.gobiiproject.gobiimodel.dto.brapi.LinkageGroupDTO;

import java.util.List;

public interface LinkageGroupService {

    List<LinkageGroupDTO> getLinkageGroups(Integer pageNumber, Integer pageSize);
    List<LinkageGroupDTO> getLinkageGroupsByMapId(
            Integer mapId,
            Integer pageNumber,
            Integer pageSize);
}
