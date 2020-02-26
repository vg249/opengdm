package org.gobiiproject.gobidomain.services.impl.brapi;

import org.gobiiproject.gobidomain.services.CallSetBrapiService;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.noaudit.CallSetBrapiDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;
import org.gobiiproject.gobiimodel.entity.DnaRun;
import org.gobiiproject.gobiimodel.modelmapper.ModelMapper;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.gobiiproject.gobiisampletrackingdao.DnaRunDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class CallSetBrapiServiceImpl implements CallSetBrapiService {

    Logger LOGGER = LoggerFactory.getLogger(CallSetBrapiServiceImpl.class);

    @Autowired
    private DnaRunDao dnaRunDao;

    public PagedResult<CallSetBrapiDTO> getCallSets(Integer pageSize,
                                                    Integer pageNum,
                                                    Integer variantSetDbId,
                                                    CallSetBrapiDTO callSetsFilter) throws GobiiException {

        Objects.requireNonNull(pageSize);
        Objects.requireNonNull(pageNum);

        PagedResult<CallSetBrapiDTO> pagedResult = new PagedResult<>();

        List<CallSetBrapiDTO> callSets = new ArrayList<>();

        try {


            Integer rowOffset = pageNum * pageSize;

            List<DnaRun> dnaRuns = dnaRunDao.getDnaRuns(pageSize, rowOffset,
                    callSetsFilter.getCallSetDbId(), callSetsFilter.getCallSetName(),
                    variantSetDbId, callSetsFilter.getStudyDbId(),
                    callSetsFilter.getSampleDbId(), callSetsFilter.getSampleName(),
                    callSetsFilter.getGermplasmDbId(), callSetsFilter.getGermplasmName());


            for (DnaRun dnaRun : dnaRuns) {
                CallSetBrapiDTO callSet = this.mapDnaRunEntityToCallSetDto(dnaRun);
                callSets.add(callSet);
            }

            pagedResult.setResult(callSets);
            pagedResult.setCurrentPageSize(callSets.size());
            pagedResult.setCurrentPageNum(pageNum);


            return pagedResult;
        }
        catch (Exception e) {

            LOGGER.error(e.getMessage(), e);

            throw new GobiiException(GobiiStatusLevel.ERROR, GobiiValidationStatusType.UNKNOWN, e.getMessage());
        }
    }

    public PagedResult<CallSetBrapiDTO> getCallSets(Integer pageSize,
                                                  String pageToken,
                                                  Integer variantSetDbId,
                                                  CallSetBrapiDTO callSetsFilter) {

        PagedResult<CallSetBrapiDTO> callSets = new PagedResult<>();

        return callSets;
    }


    public CallSetBrapiDTO getCallSetById(Integer callSetDbId) throws GobiiException {

        Objects.requireNonNull(callSetDbId);

        try {

            DnaRun dnaRun = dnaRunDao.getDnaRunById(callSetDbId);

            CallSetBrapiDTO callSet = this.mapDnaRunEntityToCallSetDto(dnaRun);

            return callSet;
        }
        catch (Exception e) {

            LOGGER.error(e.getMessage(), e);

            throw new GobiiException(GobiiStatusLevel.ERROR, GobiiValidationStatusType.UNKNOWN, e.getMessage());

        }
    }


    private CallSetBrapiDTO mapDnaRunEntityToCallSetDto(DnaRun dnaRun) {

        CallSetBrapiDTO callSet = new CallSetBrapiDTO();

        ModelMapper.mapEntityToDto(dnaRun, callSet);

        Iterator<String> datasetIdsIter = dnaRun.getDatasetDnaRunIdx().fieldNames();

        while (datasetIdsIter.hasNext()) {
            callSet.getVariantSetIds().add(Integer.parseInt(datasetIdsIter.next()));
        }

        return callSet;
    }

}
