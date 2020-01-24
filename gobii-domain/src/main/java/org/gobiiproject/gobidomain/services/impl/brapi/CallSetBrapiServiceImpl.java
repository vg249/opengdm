package org.gobiiproject.gobidomain.services.impl.brapi;

import org.gobiiproject.gobidomain.services.CallSetBrapiService;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.CallSetBrapiDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedList;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;

public class CallSetBrapiServiceImpl implements CallSetBrapiService {

    public PagedResult<CallSetBrapiDTO> getCallSets(Integer pageSize,
                                                    Integer pageNum,
                                                    CallSetBrapiDTO callSetsFilter) {

        PagedResult<CallSetBrapiDTO> callSets = new PagedResult<>();

        return callSets;
    }

    public PagedResult<CallSetBrapiDTO> getCallSets(Integer pageSize,
                                                  String pageToken,
                                                  CallSetBrapiDTO callSetsFilter) {

        PagedResult<CallSetBrapiDTO> callSets = new PagedResult<>();

        return callSets;
    }


    public CallSetBrapiDTO getCallSetById(Integer callSetDbId) {
        CallSetBrapiDTO callSet = new CallSetBrapiDTO();
        return callSet;
    }



}
