/**
 * V3ProjectDao.java
 * 
 * V3 API Project DAO interface
 */
package org.gobiiproject.gobiisampletrackingdao;

import java.util.List;

import org.gobiiproject.gobiimodel.entity.V3Project;

public interface V3ProjectDao {

    List<V3Project> getProjects(
        Integer pageNum,
        Integer pageSize  
    );
}