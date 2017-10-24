package org.gobiiproject.gobiidtomapping.entity.auditable;

import org.gobiiproject.gobiidtomapping.core.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.headerlesscontainer.ProjectDTO;

import java.util.List;

/**
 * Created by Phil on 4/6/2016.
 */
public interface DtoMapProject extends DtoMap<ProjectDTO> {

    ProjectDTO create(ProjectDTO projectDTO) throws GobiiDtoMappingException;
    ProjectDTO replace(Integer projectId, ProjectDTO projectDTO) throws GobiiDtoMappingException;
    ProjectDTO get(Integer projectId) throws GobiiDtoMappingException;
    List<ProjectDTO> getList() throws GobiiDtoMappingException;

}
