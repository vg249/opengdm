package org.gobiiproject.gobiidtomapping.entity.auditable;

import java.util.List;

import org.gobiiproject.gobiimodel.dto.auditable.V3ProjectDTO;

public interface DtoMapV3Project  extends DtoMap<V3ProjectDTO> {
    List<V3ProjectDTO> getProjects(Integer pageNum, Integer pageSize);
}