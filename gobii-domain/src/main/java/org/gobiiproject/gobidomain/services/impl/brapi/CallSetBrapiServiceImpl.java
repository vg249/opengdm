package org.gobiiproject.gobidomain.services.impl.brapi;

import org.gobiiproject.gobidomain.services.CallSetBrapiService;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.CallSetBrapiDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;
import org.gobiiproject.gobiimodel.entity.DnaRun;
import org.gobiiproject.gobiimodel.modelmapper.ModelMapper;
import org.gobiiproject.gobiisampletrackingdao.DnaRunDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CallSetBrapiServiceImpl implements CallSetBrapiService {

    @Autowired
    private DnaRunDao dnaRunDao;

    public PagedResult<CallSetBrapiDTO> getCallSets(Integer pageSize,
                                                    Integer pageNum,
                                                    Integer variantSetDbId,
                                                    CallSetBrapiDTO callSetsFilter) {

        Objects.requireNonNull(pageSize);
        Objects.requireNonNull(pageNum);

        PagedResult<CallSetBrapiDTO> pagedResult = new PagedResult<>();

        List<CallSetBrapiDTO> callSets = new ArrayList<>();

        Integer rowOffset = pageNum*pageSize;

        List<DnaRun> dnaRuns = dnaRunDao.getDnaRuns(pageSize, rowOffset,
                callSetsFilter.getCallSetDbId(), callSetsFilter.getCallSetName(),
                variantSetDbId, callSetsFilter.getStudyDbId(),
                callSetsFilter.getSampleDbId(), callSetsFilter.getSampleName(),
                callSetsFilter.getGermplasmDbId(), callSetsFilter.getGermplasmName());


        for(DnaRun dnaRun : dnaRuns) {

            CallSetBrapiDTO callSet = new CallSetBrapiDTO();

            ModelMapper.mapEntityToDto(dnaRun, callSet);

            callSets.add(callSet);

        }

        pagedResult.setResult(callSets);
        pagedResult.setCurrentPageNum(pageNum);


        return pagedResult;
    }

    public PagedResult<CallSetBrapiDTO> getCallSets(Integer pageSize,
                                                  String pageToken,
                                                  Integer variantSetDbId,
                                                  CallSetBrapiDTO callSetsFilter) {

        PagedResult<CallSetBrapiDTO> callSets = new PagedResult<>();

        return callSets;
    }


    public CallSetBrapiDTO getCallSetById(Integer callSetDbId) {
        CallSetBrapiDTO callSet = new CallSetBrapiDTO();
        return callSet;
    }



}
