// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-24
// ************************************************************************
package org.gobiiproject.gobidomain.services;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.dto.container.ProjectDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.PlatformDTO;

import java.util.List;

/**
 * Created by Phil on 3/24/2016.
 */
public interface ProjectService {

    ProjectDTO processProject(ProjectDTO projectDTO);

    ProjectDTO createProject(ProjectDTO projectDTO) throws GobiiDomainException;
    ProjectDTO replaceProject(Integer projectId, ProjectDTO projectDTO) throws GobiiDomainException;
    List<ProjectDTO> getProjects() throws GobiiDomainException;
    ProjectDTO getProjectById(Integer projectId);
    
}
