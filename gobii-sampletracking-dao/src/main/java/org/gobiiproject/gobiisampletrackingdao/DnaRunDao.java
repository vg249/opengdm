package org.gobiiproject.gobiisampletrackingdao;

import org.gobiiproject.gobiimodel.entity.DnaRun;
import org.gobiiproject.gobiimodel.entity.Marker;

import java.util.List;

public interface DnaRunDao {

    List<DnaRun> getDnaRuns(Integer pageSize, Integer rowOffset,
                            Integer dnaRunId, Integer datasetId);


}
