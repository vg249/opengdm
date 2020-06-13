package org.gobiiproject.gobidomain.services.brapi;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.cvnames.CvGroupTerm;
import org.gobiiproject.gobiimodel.dto.brapi.CallSetDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;
import org.gobiiproject.gobiimodel.entity.Cv;
import org.gobiiproject.gobiimodel.entity.DnaRun;
import org.gobiiproject.gobiimodel.modelmapper.CvMapper;
import org.gobiiproject.gobiimodel.modelmapper.ModelMapper;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.gobiiproject.gobiimodel.utils.JsonNodeUtils;
import org.gobiiproject.gobiisampletrackingdao.CvDao;
import org.gobiiproject.gobiisampletrackingdao.DnaRunDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;

@Transactional
public class CallSetServiceImpl implements CallSetService {

    Logger LOGGER = LoggerFactory.getLogger(CallSetServiceImpl.class);

    @Autowired
    private DnaRunDao dnaRunDao;

    @Autowired
    private CvDao cvDao;

    public PagedResult<CallSetDTO> getCallSets(Integer pageSize, Integer pageNum,
                                               Integer variantSetDbId, CallSetDTO callSetsFilter)
        throws GobiiException {

        PagedResult<CallSetDTO> pagedResult = new PagedResult<>();

        List<CallSetDTO> callSets = new ArrayList<>();

        try {

            Objects.requireNonNull(pageSize, "pageSize : Required non null");
            Objects.requireNonNull(pageNum, "pageNum : Required non null");
            Objects.requireNonNull(callSetsFilter, "callSetsFilter : Required non null");

            Integer rowOffset = pageNum * pageSize;

            DnaRun dnaRunFilter = new DnaRun();
            ModelMapper.mapDtoToEntity(callSetsFilter, dnaRunFilter);

            List<DnaRun> dnaRuns = dnaRunDao.getDnaRuns(
                pageSize, rowOffset,
                callSetsFilter.getCallSetDbId(), callSetsFilter.getCallSetName(),
                variantSetDbId, callSetsFilter.getStudyDbId(),
                callSetsFilter.getSampleDbId(), callSetsFilter.getSampleName(),
                callSetsFilter.getGermplasmDbId(), callSetsFilter.getGermplasmName());

            List<Cv> dnaSampleGroupCvs = cvDao.getCvListByCvGroup(
                CvGroupTerm.CVGROUP_DNASAMPLE_PROP.getCvGroupName(), null);

            List<Cv> germplasmGroupCvs = cvDao.getCvListByCvGroup(
                CvGroupTerm.CVGROUP_GERMPLASM_PROP.getCvGroupName(), null);

            for (DnaRun dnaRun : dnaRuns) {
                CallSetDTO callSet =
                    this.mapDnaRunEntityToCallSetDto(dnaRun, dnaSampleGroupCvs,
                        germplasmGroupCvs);
                callSets.add(callSet);
            }

            pagedResult.setResult(callSets);
            pagedResult.setCurrentPageSize(callSets.size());
            pagedResult.setCurrentPageNum(pageNum);

            return pagedResult;
        }
        catch (Exception e) {

            LOGGER.error(e.getMessage(), e);
            throw new GobiiException(
                GobiiStatusLevel.ERROR, GobiiValidationStatusType.UNKNOWN, e.getMessage());
        }
    }


    public CallSetDTO getCallSetById(Integer callSetDbId)
        throws GobiiException {

        Objects.requireNonNull(callSetDbId);

        try {

            DnaRun dnaRun = dnaRunDao.getDnaRunById(callSetDbId);

            List<Cv> dnaSampleGroupCvs =
                cvDao.getCvListByCvGroup(CvGroupTerm.CVGROUP_DNASAMPLE_PROP.getCvGroupName(), null);

            List<Cv> germplasmGroupCvs =
                cvDao.getCvListByCvGroup(CvGroupTerm.CVGROUP_DNASAMPLE_PROP.getCvGroupName(), null);

            CallSetDTO callSet =
                this.mapDnaRunEntityToCallSetDto(dnaRun, dnaSampleGroupCvs, germplasmGroupCvs);

            return callSet;
        }
        catch (Exception e) {

            LOGGER.error(e.getMessage(), e);
            throw new GobiiException(
                GobiiStatusLevel.ERROR, GobiiValidationStatusType.UNKNOWN, e.getMessage());

        }
    }


    private CallSetDTO mapDnaRunEntityToCallSetDto(DnaRun dnaRun, List<Cv> dnaSampleGroupCvs,
                                                   List<Cv> germplasmGroupCvs) {

        CallSetDTO callSet = new CallSetDTO();

        ModelMapper.mapEntityToDto(dnaRun, callSet);

        Iterator<String> datasetIdsIter = dnaRun.getDatasetDnaRunIdx().fieldNames();

        while (datasetIdsIter.hasNext()) {
            callSet
                .getVariantSetIds()
                .add(Integer.parseInt(datasetIdsIter.next()));
        }

        if(!JsonNodeUtils.isEmpty(dnaRun.getDnaSample().getProperties())) {

            Map<String, String> additionalInfo = CvMapper.mapCvIdToCvTerms(
                dnaSampleGroupCvs, dnaRun.getDnaSample().getProperties());

            if(!JsonNodeUtils.isEmpty(dnaRun.getDnaSample().getGermplasm().getProperties())) {
                additionalInfo = CvMapper.mapCvIdToCvTerms(
                        germplasmGroupCvs, dnaRun.getDnaSample().getGermplasm().getProperties(),
                        additionalInfo);
            }

            callSet.setAdditionalInfo(additionalInfo);

        }
        return callSet;
    }


}
