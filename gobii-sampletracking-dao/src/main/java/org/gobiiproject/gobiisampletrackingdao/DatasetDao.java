package org.gobiiproject.gobiisampletrackingdao;


import org.gobiiproject.gobiimodel.entity.Project;

import java.util.List;
import java.util.Map;

public interface DatasetDao {

    Project getDatasetById(String projectName);
    List<Project> listDatasetsByPageNum(Integer pageNum, Integer pageSize, Map<String, String> projectQuery);

}
