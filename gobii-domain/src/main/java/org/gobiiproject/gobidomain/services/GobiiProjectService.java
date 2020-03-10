package org.gobiiproject.gobidomain.services;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.dto.auditable.GobiiProjectDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;


public interface GobiiProjectService{
    PagedResult<GobiiProjectDTO>  getProjects(Integer pageNum, Integer pageSize) throws GobiiDomainException;
}