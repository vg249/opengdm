package org.gobiiproject.gobidomain.services.brapi;

import org.gobiiproject.gobiimodel.dto.brapi.LinkageGroupBrapiDTO;

import java.util.List;

public interface LinkageGroupBrapiService {

    List<LinkageGroupBrapiDTO> getLinkageGroups(Integer pageNumber, Integer pageSize);
    List<LinkageGroupBrapiDTO> getLinkageGroupsByMapId(
            Integer mapId,
            Integer pageNumber,
            Integer pageSize);
}
