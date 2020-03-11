/**
 * V3ProjectDao.java
 * 
 * V3 API Project DAO interface
 */
package org.gobiiproject.gobiisampletrackingdao;

import java.util.List;

import org.gobiiproject.gobiimodel.entity.Project;

public interface ProjectDao {

    List<Project> getProjects(
        Integer pageNum,
        Integer pageSize  
    );
}