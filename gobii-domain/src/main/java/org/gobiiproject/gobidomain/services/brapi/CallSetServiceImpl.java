package org.gobiiproject.gobidomain.services.brapi;

import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.brapi.CallSetDTO;
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

public class CallSetServiceImpl implements CallSetService {

    Logger LOGGER = LoggerFactory.getLogger(CallSetServiceImpl.class);

    @Autowired
    private DnaRunDao dnaRunDao;

    public PagedResult<CallSetDTO> getCallSets(Integer pageSize,
                                               Integer pageNum,
                                               Integer variantSetDbId,
                                               CallSetDTO callSetsFilter) throws GobiiException {

        PagedResult<CallSetDTO> pagedResult = new PagedResult<>();

        List<CallSetDTO> callSets = new ArrayList<>();

        try {

            Objects.requireNonNull(pageSize, "pageSize : Required non null");
            Objects.requireNonNull(pageNum, "pageNum : Required non null");
            Objects.requireNonNull(callSetsFilter, "callSetsFilter : Required non null");


            Integer rowOffset = pageNum * pageSize;

            List<DnaRun> dnaRuns = dnaRunDao.getDnaRuns(pageSize, rowOffset,
                    callSetsFilter.getCallSetDbId(), callSetsFilter.getCallSetName(),
                    variantSetDbId, callSetsFilter.getStudyDbId(),
                    callSetsFilter.getSampleDbId(), callSetsFilter.getSampleName(),
                    callSetsFilter.getGermplasmDbId(), callSetsFilter.getGermplasmName());


            for (DnaRun dnaRun : dnaRuns) {
                CallSetDTO callSet = this.mapDnaRunEntityToCallSetDto(dnaRun);
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

    public PagedResult<CallSetDTO> getCallSets(Integer pageSize,
                                               String pageToken,
                                               Integer variantSetDbId,
                                               CallSetDTO callSetsFilter) {

        PagedResult<CallSetDTO> callSets = new PagedResult<>();

        return callSets;
    }


    public CallSetDTO getCallSetById(Integer callSetDbId) throws GobiiException {

        Objects.requireNonNull(callSetDbId);

        try {

            DnaRun dnaRun = dnaRunDao.getDnaRunById(callSetDbId);

            CallSetDTO callSet = this.mapDnaRunEntityToCallSetDto(dnaRun);

            return callSet;
        }
        catch (Exception e) {

            LOGGER.error(e.getMessage(), e);

            throw new GobiiException(GobiiStatusLevel.ERROR, GobiiValidationStatusType.UNKNOWN, e.getMessage());

        }
    }


    private CallSetDTO mapDnaRunEntityToCallSetDto(DnaRun dnaRun) {

        CallSetDTO callSet = new CallSetDTO();

        ModelMapper.mapEntityToDto(dnaRun, callSet);

        Iterator<String> datasetIdsIter = dnaRun.getDatasetDnaRunIdx().fieldNames();

        while (datasetIdsIter.hasNext()) {
            callSet.getVariantSetIds().add(Integer.parseInt(datasetIdsIter.next()));
        }

        return callSet;
    }

}
