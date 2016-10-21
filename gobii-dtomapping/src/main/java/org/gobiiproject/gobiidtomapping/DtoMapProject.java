package org.gobiiproject.gobiidtomapping;

import org.gobiiproject.gobiimodel.dto.container.ProjectDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.PlatformDTO;

import java.util.List;

/**
 * Created by Phil on 4/6/2016.
 */
public interface DtoMapProject {
//    ProjectDTO getProjectDetail(ProjectDTO projectDTO) throws GobiiDtoMappingException;
//    ProjectDTO createProject(ProjectDTO projectDTO) throws GobiiDtoMappingException;
//    ProjectDTO updateProject(ProjectDTO projectDTO) throws GobiiDtoMappingException;

    List<ProjectDTO> getProjects() throws GobiiDtoMappingException;
    ProjectDTO getProjectDetails(Integer projectId) throws GobiiDtoMappingException;
    ProjectDTO createProject(ProjectDTO projectDTO) throws GobiiDtoMappingException;
    ProjectDTO replaceProject(Integer projectId, ProjectDTO projectDTO) throws GobiiDtoMappingException;
    
}
