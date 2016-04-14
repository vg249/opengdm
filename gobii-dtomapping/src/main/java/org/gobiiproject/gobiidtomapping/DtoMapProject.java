package org.gobiiproject.gobiidtomapping;

import org.gobiiproject.gobiimodel.dto.container.project.ProjectDTO;

/**
 * Created by Phil on 4/6/2016.
 */
public interface DtoMapProject {
    ProjectDTO getProject( ProjectDTO projectDTO) throws GobiiDtoMappingException;
}
