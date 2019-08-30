package org.gobiiproject.gobiisampletrackingdao;

import org.gobiiproject.gobiimodel.dto.entity.auditable.sampletracking.ProjectDTO;
import org.gobiiproject.gobiimodel.entity.Project;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ProjectDaoImpl implements ProjectDAO {

    @PersistenceContext
    protected EntityManager em;

    @Override
    public Integer createProject(ProjectDTO newProject) {
        return 0;
    }

    @Override
    public ProjectDTO getProjectById(Integer projectId) {

        ProjectDTO projectDTO = new ProjectDTO();

        List<Project> listProject = em
                .createNativeQuery("SELECT project.name FROM project WHERE project_id = ?")
                .setParameter(1, projectId)
                .getResultList();


        return projectDTO;
    }

    @Override
    public ProjectDTO getProjectByName(String projectName) {
        ProjectDTO projectDTO = new ProjectDTO();
        return projectDTO;
    }

    @Override
    public List<ProjectDTO> listProjects(Map<String, String> projectQuery) {
        List<ProjectDTO> projectList = new LinkedList<>();
        return projectList;
    }

    @Override
    public Integer updateProject(ProjectDTO newProject) {
        return 0;
    }


}
