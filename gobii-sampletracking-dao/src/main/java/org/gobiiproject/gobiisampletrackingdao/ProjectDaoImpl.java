package org.gobiiproject.gobiisampletrackingdao;

import org.gobiiproject.gobiimodel.entity.Project;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Data Access Object(DAO) for Project.
 */
public class ProjectDaoImpl implements ProjectDao {

    @PersistenceContext
    protected EntityManager em;

    @Override
    @Transactional
    public Integer createProject(Project newProject) {

        em.persist(newProject);

        em.getTransaction().commit();

        return 0;

    }

    @Override
    public Project getProjectById(Integer projectId) {

        List<Project> projectList = em
                .createNativeQuery(
                        "SELECT * FROM project WHERE project_id = ?", Project.class)
                .setParameter(1, projectId)
                .getResultList();

        if(projectList.size() == 0) {
           return null;
        }
        else if(projectList.size() > 1) {
            throw new GobiiDaoException(GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.VALIDATION_NOT_UNIQUE,
                    "Multiple resources found. Violation of Unique Project Id constraint." +
                            " Please contact your Data Administrator to resolve this. " +
                            "Changing underlying database schemas and constraints " +
                            "without consulting GOBii Team is not recommended.");

        }

        return projectList.get(0);

    }

    @Override
    public Project getProjectByName(String projectName) {
        Project project = new Project();
        return project;
    }

    @Override
    public List<Project> listProjects(Map<String, String> projectQuery) {
        List<Project> projectList = new ArrayList<>();
        return projectList;
    }

    @Override
    public Integer updateProject(Project newProject) {
        return 0;
    }


}
