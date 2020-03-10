/**
 * V3ProjectDao.java
 * 
 * V3 API Project DAO interface
 */
package org.gobiiproject.gobiisampletrackingdao;

import java.util.List;

import org.gobiiproject.gobiimodel.entity.v3.Project;

public interface V3ProjectDao {

    List<Project> getProjects(
        Integer pageNum,
        Integer pageSize  
    );
}