package org.gobiiproject.gobidomain.services.brapi;

import org.gobiiproject.gobiimodel.dto.brapi.CallSetDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;
import org.gobiiproject.gobiimodel.entity.DnaRun;

import java.util.List;

public interface CallSetService {

    PagedResult<CallSetDTO> getCallSets(Integer pageSize,
                                        Integer pageNum,
                                        Integer variantSetDbId,
                                        CallSetDTO callSetsFilter);

    CallSetDTO getCallSetById(Integer callSetDbId);

    List<CallSetDTO> mapDnaRunsToCallSetDtos(List<DnaRun> dnaRuns);

}
