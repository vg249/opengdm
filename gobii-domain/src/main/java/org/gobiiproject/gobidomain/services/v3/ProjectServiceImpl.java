/**
 * ProjectServiceImpl.java
 * 
 * Project service implementation v3
 * @author Rodolfo N. Duldulao, Jr. <rnduldulaojr@gmail.com>
 * @since 2020-03-07
 */
package org.gobiiproject.gobidomain.services.v3;

import java.util.List;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobidomain.services.ProjectService;
import org.gobiiproject.gobiimodel.dto.v3.ProjectDTO;

public class ProjectServiceImpl implements ProjectService<ProjectDTO> {

    @Override
    public ProjectDTO createProject(ProjectDTO projectDTO) throws GobiiDomainException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ProjectDTO getProjectById(Integer projectId) throws GobiiDomainException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<ProjectDTO> getProjects() throws GobiiDomainException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<ProjectDTO> getProjects(Integer pageNum, Integer pageSize) throws GobiiDomainException {
        // TODO Auto-generated method stub
        
        return null;
    }

    @Override
    public List<ProjectDTO> getProjectsForLoadedDatasets() throws GobiiDomainException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ProjectDTO replaceProject(Integer projectId, ProjectDTO projectDTO) throws GobiiDomainException {
        // TODO Auto-generated method stub
        return null;
    }

}


