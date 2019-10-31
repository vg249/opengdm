package org.gobiiproject.gobiisampletrackingdao;

import org.gobiiproject.gobiimodel.entity.DnaSample;

import java.util.List;

public interface DnaSampleDao {

    List<DnaSample> getDnaSamples(Integer pageNum, Integer pageSize,
                                  Integer projectId, Integer dnaSampleId,
                                  Integer germplasmId, String germplasmExternamCode);

    List<DnaSample> getDnaSamples(Integer pageNum, Integer pageSize);

    List<DnaSample> getDnaSamplesByProjectId(Integer pageNum, Integer pageSize, Integer projectId);

    List<DnaSample> getDnaSamplesByDnaSampleId(Integer pageNum, Integer pageSize, Integer dnaSampleId);

    List<DnaSample> getDnaSamplesByGermplasmId(Integer pageNum, Integer pageSize, Integer germplasmId);

    List<DnaSample> getDnaSamplesByGermplasmExternalCode(Integer pageNum, Integer pageSize,
                                                         String germplasmExternalCode);

}
