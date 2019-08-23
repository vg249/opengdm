package org.gobiiproject.gobiisampletrackingdao;


import org.gobiiproject.gobiimodel.dto.entity.auditable.sampletracking.ProjectDTO;

import java.util.List;
import java.util.Map;

public interface ProjectDAO {

    Integer createProject(ProjectDTO newProject);
    ProjectDTO getProjectById(Integer projectId);
    ProjectDTO getProjectByName(String projectName);
    List<ProjectDTO> listProjects(Map<String, String> projectQuery);
    Integer updateProject(ProjectDTO newProject);

}
