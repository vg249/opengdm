/**
 * ProjectDao.java
 * 
 * V3 API Project DAO interface
 * @author Rodolfo N. Duldulao, Jr.
 */
package org.gobiiproject.gobiisampletrackingdao;

import java.util.List;

import org.gobiiproject.gobiimodel.dto.children.CvPropertyDTO;
import org.gobiiproject.gobiimodel.entity.Cv;
import org.gobiiproject.gobiimodel.entity.Project;

public interface ProjectDao {

    List<Project> getProjects(
        Integer pageNum,
        Integer pageSize  
    );

    Project createProject(
        String contactId,
        String projectName,
        String projectDescrition,
        List<CvPropertyDTO> properties,
        String createByUser
    ) throws Exception;

    List<Cv> getProjectProperties(
        Integer pageNum,
        Integer pageSize  
    ); 
}