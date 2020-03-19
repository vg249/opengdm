package org.gobiiproject.gobiisampletrackingdao;

import org.gobiiproject.gobiimodel.entity.DnaRun;

import java.util.List;

public interface DnaRunDao {

    List<DnaRun> getDnaRuns(Integer pageSize, Integer rowOffset,
                            Integer dnaRunId, String dnaRunName,
                            Integer datasetId, Integer experimentId,
                            Integer dnaSampleId, String dnaSampleName,
                            Integer germplasmId, String germplasmName,
                            boolean fetchAssociations);

    List<DnaRun> getDnaRuns(Integer pageSize, Integer rowOffset,
                            Integer dnaRunId, String dnaRunName,
                            Integer datasetId, Integer experimentId,
                            Integer dnaSampleId, String dnaSampleName,
                            Integer germplasmId, String germplasmName);

    DnaRun getDnaRunById(Integer dnaRunId);

    DnaRun getDnaRunById(Integer dnaRunId, boolean fetchAssociations);

    List<DnaRun> getDnaRunsByDnaRunIdCursor(Integer pageSize,
                                            Integer dnaRunIdCursor,
                                            Integer datasetId);

    List<DnaRun> getDnaRunsByDatasetId(Integer datasetId,
                                       Integer pageSize,
                                       Integer rowOffset);

    List<DnaRun> getDnaRunsByDatasetId(Integer datasetId,
                                       Integer pageSize,
                                       Integer rowOffset,
                                       boolean fetchAssociations);

    List<DnaRun> getDnaRunsByDanRunIds(List<Integer> dnaRunIds);

    List<DnaRun> getDnaRunsByDanRunNames(List<String> dnaRunNames);

}

