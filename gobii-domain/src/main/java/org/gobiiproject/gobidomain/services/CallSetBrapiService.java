package org.gobiiproject.gobidomain.services;

import org.gobiiproject.gobiimodel.dto.entity.noaudit.CallSetBrapiDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;

public interface CallSetBrapiService {

    PagedResult<CallSetBrapiDTO> getCallSets(Integer pageSize,
                                             Integer pageNum,
                                             CallSetBrapiDTO callSetsFilter);

    PagedResult<CallSetBrapiDTO> getCallSets(Integer pageSize,
                                           String pageToken,
                                           CallSetBrapiDTO callSetsFilter);

    CallSetBrapiDTO getCallSetById(Integer callSetDbId);

}
