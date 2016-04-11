package org.gobiiproject.gobiidtomapping;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiimodel.dto.container.ProjectDTO;

/**
 * Created by Phil on 4/6/2016.
 */
public interface DtoMapProject {
    ProjectDTO getProject( ProjectDTO projectDTO) throws GobiiDtoMappingException;
}
