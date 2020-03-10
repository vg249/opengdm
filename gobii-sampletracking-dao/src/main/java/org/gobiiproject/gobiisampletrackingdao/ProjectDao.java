/**
 * V3ProjectDao.java
 * 
 * V3 API Project DAO interface
 */
package org.gobiiproject.gobiisampletrackingdao;

import java.util.List;

import org.gobiiproject.gobiimodel.entity.v3.GobiiProject;

public interface ProjectDao {

    List<GobiiProject> getProjects(
        Integer pageNum,
        Integer pageSize  
    );
}