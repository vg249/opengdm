package org.gobiiproject.gobidomain.services.brapi;

import org.gobiiproject.gobiimodel.dto.brapi.CallSetDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;

public interface CallSetService {

    PagedResult<CallSetDTO> getCallSets(Integer pageSize,
                                        Integer pageNum,
                                        Integer variantSetDbId,
                                        CallSetDTO callSetsFilter);

    CallSetDTO getCallSetById(Integer callSetDbId);

}
