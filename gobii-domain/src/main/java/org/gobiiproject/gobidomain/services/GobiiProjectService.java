package org.gobiiproject.gobidomain.services;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.dto.auditable.GobiiProjectDTO;
import org.gobiiproject.gobiiapimodel.payload.sampletracking.BrApiMasterListPayload;

public interface GobiiProjectService{
    BrApiMasterListPayload<GobiiProjectDTO>  getProjects(Integer pageNum, Integer pageSize) throws GobiiDomainException;
}