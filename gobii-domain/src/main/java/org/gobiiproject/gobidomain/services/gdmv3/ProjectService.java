/**
 * GobiiProjectService.java
 * 
 * Service interface for Gobii v3 project endpoints
 * @author Rodolfo N. Duldulao, Jr.
 */
package org.gobiiproject.gobidomain.services.gdmv3;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.dto.auditable.GobiiProjectDTO;
import org.gobiiproject.gobiimodel.dto.children.CvPropertyDTO;
import org.gobiiproject.gobiimodel.dto.request.GobiiProjectPatchDTO;
import org.gobiiproject.gobiimodel.dto.request.GobiiProjectRequestDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;
   
public interface ProjectService{
    GobiiProjectDTO getProject(Integer projectId) throws Exception;
    PagedResult<GobiiProjectDTO>  getProjects(Integer page, Integer pageSize, Integer piContactId) throws GobiiDomainException;
    GobiiProjectDTO createProject(GobiiProjectRequestDTO request, String createdBy) throws Exception;
    GobiiProjectDTO patchProject(Integer projectId, GobiiProjectPatchDTO request, String editedBy) throws Exception;
    void deleteProject(Integer projectId) throws Exception;
    String getDefaultProjectEditor();

    //
    PagedResult<CvPropertyDTO> getProjectProperties(Integer page, Integer pageSize) throws Exception;
}