package org.gobiiproject.gobidomain.services;

import org.gobiiproject.gobiimodel.dto.entity.noaudit.CallSetBrapiDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedList;

public interface CallSetBrapiService {

    PagedList<CallSetBrapiDTO> getCallSets(Integer pageSize,
                                           Integer pageNum,
                                           CallSetBrapiDTO callSetsFilter);

    PagedList<CallSetBrapiDTO> getCallSets(Integer pageSize,
                                           String pageToken,
                                           CallSetBrapiDTO callSetsFilter);

    CallSetBrapiDTO getCallSetById(Integer callSetDbId);

}
