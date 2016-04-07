package org.gobiiproject.gobidomain.services.impl;

import org.gobiiproject.gobidomain.services.ProjectService;
import org.gobiiproject.gobiidtomapping.DtoMapProject;
import org.gobiiproject.gobiidtomapping.impl.DtoMapProjectImpl;
import org.gobiiproject.gobiimodel.dto.container.ProjectDTO;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Phil on 4/6/2016.
 */
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    DtoMapProject dtoMapProject = null;

    @Override
    public ProjectDTO getProject(ProjectDTO projectDTO) {
        return dtoMapProject.getProject();
    }
}
