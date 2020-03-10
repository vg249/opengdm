package org.gobiiproject.gobidomain.services;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.dto.auditable.V3ProjectDTO;
import org.gobiiproject.gobiiapimodel.payload.sampletracking.BrApiMasterListPayload;

public interface V3ProjectService{
    BrApiMasterListPayload<V3ProjectDTO>  getProjects(Integer pageNum, Integer pageSize) throws GobiiDomainException;
}