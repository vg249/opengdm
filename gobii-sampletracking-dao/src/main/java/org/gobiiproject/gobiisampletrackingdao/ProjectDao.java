/**
 * ProjectDao.java
 * 
 * V3 API Project DAO interface
 */
package org.gobiiproject.gobiisampletrackingdao;

import java.util.List;

import org.gobiiproject.gobiimodel.dto.children.CvPropertyDTO;
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
        List<CvPropertyDTO> properties
    ) throws Exception;
}