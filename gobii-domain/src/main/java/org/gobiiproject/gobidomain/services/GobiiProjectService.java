/**
 * GobiiProjectService.java
 * 
 * Service interface for Gobii v3 project endpoints
 * @author Rodolfo N. Duldulao, Jr.
 */
package org.gobiiproject.gobidomain.services;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.dto.auditable.GobiiProjectDTO;
import org.gobiiproject.gobiimodel.dto.children.CvPropertyDTO;
import org.gobiiproject.gobiimodel.dto.request.GobiiProjectPatchDTO;
import org.gobiiproject.gobiimodel.dto.request.GobiiProjectRequestDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;
   
public interface GobiiProjectService{
    PagedResult<GobiiProjectDTO>  getProjects(Integer pageNum, Integer pageSize) throws GobiiDomainException;
    GobiiProjectDTO createProject(GobiiProjectRequestDTO request, String createdBy) throws Exception;
    GobiiProjectDTO patchProject(Integer projectId, GobiiProjectPatchDTO request, String editedBy) throws Exception;
    String getDefaultProjectEditor();

    //
    PagedResult<CvPropertyDTO> getProjectProperties(Integer pageNum, Integer pageSize) throws Exception;
    
}