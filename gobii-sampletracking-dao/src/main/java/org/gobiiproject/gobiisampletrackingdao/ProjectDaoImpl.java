package org.gobiiproject.gobiisampletrackingdao;

import com.fasterxml.jackson.databind.JsonNode;
import org.gobiiproject.gobiimodel.entity.Project;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.gobiiproject.gobiisampletrackingdao.spworkers.SpDef;
import org.gobiiproject.gobiisampletrackingdao.spworkers.SpWorker;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Data Access Object(DAO) for Project.
 */
public class ProjectDaoImpl implements ProjectDao {

    @PersistenceContext
    protected EntityManager em;

    @Autowired
    protected SpWorker spWorker;

    /**
     * Uses createprojectwithprops stored function in postgres database.
     * Stored procedure arguments by index are, 1-based index
     * 1 - projectname
     * 2 - projectcode
     * 3 - projectdescription
     * 4 - picontact
     * 5 - createdby
     * 6 - createddate
     * 7 - modifiedby
     * 8 - modifieddate
     * 9 - projectstatus
     * 10 - props
     *
     * Stored function returns integer project id.
     *
     * @param newProject - Project entity to be created.
     * @return projectId - Id of the newly created project
     */
    @Override
    @Transactional
    public Integer createProject(Project newProject) {

        Integer returnVal = 0;

        try {

            SpDef spDef = new SpDef("{call createprojectwithprops(?,?,?,?,?,?,?,?,?,?)}")
                    .addParamDef(1, String.class, newProject.projectName)
                    .addParamDef(2, String.class, newProject.projectCode)
                    .addParamDef(3, String.class, newProject.projectDescription)
                    .addParamDef(4, Integer.class, newProject.piContactId)
                    .addParamDef(5, Integer.class, newProject.createdBy)
                    .addParamDef(6, Date.class, newProject.createdDate)
                    .addParamDef(7, Integer.class, newProject.modifiedBy)
                    .addParamDef(8, Date.class, newProject.modifiedDate)
                    .addParamDef(9, Integer.class, newProject.projectStatus)
                    .addParamDef(10, JsonNode.class, newProject.properties);

            spWorker.run(spDef);

            returnVal = spWorker.getResult();

        }
        catch(Exception e) {

        }

        return returnVal;

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
