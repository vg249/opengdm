package org.gobiiproject.gobidomain.services.impl.brapi;

import org.gobiiproject.gobidomain.services.CallSetBrapiService;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.CallSetBrapiDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedList;

public class CallSetBrapiServiceImpl implements CallSetBrapiService {

    public PagedList<CallSetBrapiDTO> getCallSets(Integer pageSize,
                                                  Integer pageNum,
                                                  CallSetBrapiDTO callSetsFilter) {

        PagedList<CallSetBrapiDTO> callSets = new PagedList<>();

        return callSets;
    }

    public PagedList<CallSetBrapiDTO> getCallSets(Integer pageSize,
                                                  String pageToken,
                                                  CallSetBrapiDTO callSetsFilter) {

        PagedList<CallSetBrapiDTO> callSets = new PagedList<>();

        return callSets;
    }


    public CallSetBrapiDTO getCallSetById(Integer callSetDbId) {
        CallSetBrapiDTO callSet = new CallSetBrapiDTO();
        return callSet;
    }



}
