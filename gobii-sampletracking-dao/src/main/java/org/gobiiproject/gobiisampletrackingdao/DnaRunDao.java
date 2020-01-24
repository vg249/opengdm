package org.gobiiproject.gobiisampletrackingdao;

import org.gobiiproject.gobiimodel.entity.DnaRun;

import java.util.List;

public interface DnaRunDao {

    List<DnaRun> getDnaRuns(Integer pageSize, Integer rowOffset,
                            Integer dnaRunId, String dnaRunName,
                            Integer datasetId, Integer experimentId,
                            Integer dnaSampleId, String dnaSampleName,
                            Integer germplasmId, String germplasmName);


    List<DnaRun> getDnaRunsByDnaRunIdCursor(Integer dnaRunId,
                                            Integer datasetId,
                                            Integer pageSize);

    DnaRun getDnaRunById(Integer dnaRunId);

    List<DnaRun> getDnaRunsByDatasetId(Integer datasetId,
                                       Integer pageSize,
                                       Integer rowOffset);


    List<DnaRun> getDnaRunsByDanRunIds(List<Integer> dnaRunIds);

    List<DnaRun> getDnaRunsByDanRunNames(List<String> dnaRunNames);

}

