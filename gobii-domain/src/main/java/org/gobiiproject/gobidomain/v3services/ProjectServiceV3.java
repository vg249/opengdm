package org.gobiiproject.gobidomain.v3services;

import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;

public interface  ProjectServiceV3<T> {
   PagedResult<T> getProjects(Integer pageNum, Integer pageSize) throws GobiiException;
}