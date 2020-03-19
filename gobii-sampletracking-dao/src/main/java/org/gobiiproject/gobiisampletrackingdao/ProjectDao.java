/**
 * ProjectDao.java
 * 
 * V3 API Project DAO interface
 * @author Rodolfo N. Duldulao, Jr.
 */
package org.gobiiproject.gobiisampletrackingdao;

import java.util.List;
import java.util.Map;

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

    Project patchProject(
        Integer projectId,
        Map<String, String> attributes,
        List<CvPropertyDTO> propertiesList,
        String updatedBy
    ) throws Exception;
    
    List<Cv> getProjectProperties(
        Integer pageNum,
        Integer pageSize  
    ); 
}