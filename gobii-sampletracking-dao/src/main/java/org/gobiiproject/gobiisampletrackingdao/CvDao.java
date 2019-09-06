package org.gobiiproject.gobiisampletrackingdao;


import org.gobiiproject.gobiimodel.entity.Cv;
import org.gobiiproject.gobiimodel.entity.Project;

import java.util.List;
import java.util.Map;

public interface CvDao {

    List<Cv> getCvListByCvGroup(String cvGroupName);

}
