package org.gobiiproject.gobidomain.services.brapi;

import org.gobiiproject.gobiimodel.dto.brapi.CallSetBrapiDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;

public interface CallSetBrapiService {

    PagedResult<CallSetBrapiDTO> getCallSets(Integer pageSize,
                                             Integer pageNum,
                                             Integer variantSetDbId,
                                             CallSetBrapiDTO callSetsFilter);

    PagedResult<CallSetBrapiDTO> getCallSets(Integer pageSize,
                                           String pageToken,
                                           Integer variantSetDbId,
                                           CallSetBrapiDTO callSetsFilter);

    CallSetBrapiDTO getCallSetById(Integer callSetDbId);

}
