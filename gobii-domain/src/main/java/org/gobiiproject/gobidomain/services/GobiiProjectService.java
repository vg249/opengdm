package org.gobiiproject.gobidomain.services;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.dto.auditable.GobiiProjectDTO;
import org.gobiiproject.gobiimodel.dto.request.GobiiProjectPatchDTO;
import org.gobiiproject.gobiimodel.dto.request.GobiiProjectRequestDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;
   
public interface GobiiProjectService{
    PagedResult<GobiiProjectDTO>  getProjects(Integer pageNum, Integer pageSize) throws GobiiDomainException;
    GobiiProjectDTO createProject(GobiiProjectRequestDTO request, String createdBy) throws Exception;
    GobiiProjectDTO patchProject(Integer projectId, GobiiProjectPatchDTO request, String editedBy) throws Exception;
    String getDefaultProjectEditor();
    
}