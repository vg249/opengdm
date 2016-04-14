// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-24
// ************************************************************************
package org.gobiiproject.gobidomain.services;

import org.gobiiproject.gobiimodel.dto.container.project.ProjectDTO;

/**
 * Created by Phil on 3/24/2016.
 */
public interface ProjectService {

    ProjectDTO getProject(ProjectDTO projectDTO);
}
