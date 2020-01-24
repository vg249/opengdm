package org.gobiiproject.gobiidtomapping.entity.noaudit.impl;

import org.gobiiproject.gobiidao.resultset.access.RsDnaRunDao;
//import org.gobiiproject.gobiidao.resultset.access.RsSearchQueryDao;
import org.gobiiproject.gobiidao.resultset.core.ResultColumnApplicator;
import org.gobiiproject.gobiidao.resultset.core.listquery.DtoListQueryColl;
import org.gobiiproject.gobiidao.resultset.core.listquery.ListSqlId;
import org.gobiiproject.gobiidtomapping.core.GobiiDtoMappingException;
import org.gobiiproject.gobiidtomapping.entity.noaudit.DtoMapCv;
import org.gobiiproject.gobiidtomapping.entity.noaudit.DtoMapDnaRun;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.CvDTO;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.CallSetBrapiDTO;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.util.*;
import java.util.stream.Collectors;

import static org.gobiiproject.gobiimodel.cvnames.CvGroup.CVGROUP_DNASAMPLE_PROP;
import static org.gobiiproject.gobiimodel.cvnames.CvGroup.CVGROUP_GERMPLASM_PROP;

/**
 * Created by VCalaminos on 6/25/2019.
 */
public class DtoMapDnaRunImpl implements DtoMapDnaRun {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapDnaRunImpl.class);

    @Autowired
    private RsDnaRunDao rsDnaRunDao;

//    @Autowired
//    private RsSearchQueryDao rsSearchQueryDao;

    @Autowired
    private DtoListQueryColl dtoListQueryColl;

    @Autowired
    private DtoMapCv dtoMapCv;

    @Transactional
    @Override
    public CallSetBrapiDTO get(Integer dnaRunId) throws GobiiDtoMappingException {

        CallSetBrapiDTO returnVal = new CallSetBrapiDTO();

        //try {
        //    ResultSet resultSet = rsDnaRunDao.getDnaRunForDnaRunId(dnaRunId);
        //    if (resultSet.next()) {

        //        ResultColumnApplicator.applyColumnValues(resultSet, returnVal);

        //        if (returnVal.getDatasetDnarunIndex().size() > 0) {

        //            for (String dataSetId : returnVal.getDatasetDnarunIndex().keySet()) {

        //                if (dataSetId != null) {
        //                    returnVal.getVariantSetIds().add(Integer.parseInt(dataSetId));
        //                }
        //            }
        //        }

        //        if (resultSet.next()) {
        //            throw new GobiiDtoMappingException(GobiiStatusLevel.ERROR,
        //                    GobiiValidationStatusType.VALIDATION_NOT_UNIQUE,
        //                    "Multiple resources found. Violation of unique Dnarun ID constraint." +
        //                            " Please contact your Data Administrato to resolve this. " +
        //                            "Changing underlying database schemas and constraints " +
        //                            "without consulting GOBii Team is not recommended.");
        //        }
        //    }
        //    else {
        //        throw new GobiiDtoMappingException(GobiiStatusLevel.ERROR,
        //                GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
        //                "Dna run not found for given id.");
        //    }

        //}
        //catch (GobiiException gE) {
        //    LOGGER.error(gE.getMessage(), gE);
        //    throw new GobiiDtoMappingException(
        //            gE.getGobiiStatusLevel(),
        //            gE.getGobiiValidationStatusType(),
        //            gE.getMessage());
        //}
        //catch (Exception e) {
        //    LOGGER.error("Gobii Mapping error", e);
        //    throw new GobiiDtoMappingException(e);
        //}

        return returnVal;
    }

    @Override
    public List<CallSetBrapiDTO> getList(Integer pageToken, Integer pageSize,
                                         CallSetBrapiDTO callSetBrapiDTOFilter) throws GobiiDtoMappingException {

        List<CallSetBrapiDTO> returnVal = new ArrayList<>();
        //try {

        //    Map<String, Object> sqlParams = new HashMap<>();

        //    if (pageToken != null) {
        //        sqlParams.put("pageToken", pageToken);
        //    }

        //    if (pageSize != null) {
        //        sqlParams.put("pageSize", pageSize);
        //    }

        //    if (callSetBrapiDTOFilter != null) {

        //        if (callSetBrapiDTOFilter.getCallSetDbId() != null && callSetBrapiDTOFilter.getCallSetDbId() != 0) {
        //            sqlParams.put("callSetDbId", callSetBrapiDTOFilter.getCallSetDbId());
        //        }

        //        if (callSetBrapiDTOFilter.getCallSetName() != null) {
        //            sqlParams.put("callSetName", callSetBrapiDTOFilter.getCallSetName());
        //        }

        //        if (callSetBrapiDTOFilter.getVariantSetIds().size() > 0) {
        //            sqlParams.put("variantSetDbId", callSetBrapiDTOFilter.getVariantSetIds().get(0));
        //        }

        //        if (callSetBrapiDTOFilter.getSampleDbId() != null) {
        //            sqlParams.put("sampleDbId", callSetBrapiDTOFilter.getSampleDbId());
        //        }

        //        if (callSetBrapiDTOFilter.getGermplasmDbId() != null) {
        //            sqlParams.put("germplasmDbId", callSetBrapiDTOFilter.getGermplasmDbId());
        //        }

        //        if (callSetBrapiDTOFilter.getStudyDbId() != null) {
        //            sqlParams.put("studyDbId", callSetBrapiDTOFilter.getStudyDbId());
        //        }

        //        if (callSetBrapiDTOFilter.getSampleName() != null) {
        //            sqlParams.put("sampleName", callSetBrapiDTOFilter.getSampleName());
        //        }
        //    }

        //    returnVal = (List<CallSetBrapiDTO>) dtoListQueryColl.getList(
        //            ListSqlId.QUERY_ID_DNARUN_ALL,
        //            null,
        //            sqlParams
        //    );

        //    if (returnVal == null) {
        //        return new ArrayList<>();
        //    }

        //    List<CvDTO> germplasmPropsCv = dtoMapCv.getCvsByGroupName(CVGROUP_GERMPLASM_PROP.getCvGroupName());
        //    List<CvDTO> samplePropsCv = dtoMapCv.getCvsByGroupName(CVGROUP_DNASAMPLE_PROP.getCvGroupName());

        //    for(CallSetBrapiDTO currentCallSetBrapiDTO : returnVal) {

        //        if (currentCallSetBrapiDTO.getDatasetDnarunIndex().size() > 0) {

        //            for (String dataSetId : currentCallSetBrapiDTO.getDatasetDnarunIndex().keySet()) {

        //                if (dataSetId != null) {
        //                    currentCallSetBrapiDTO.getVariantSetIds().add(Integer.parseInt(dataSetId));
        //                }
        //            }
        //        }

        //        if (currentCallSetBrapiDTO.getGermplasmProps().size() > 0) {

        //            for (String cvId : currentCallSetBrapiDTO.getGermplasmProps().keySet()) {

        //                String propValue = currentCallSetBrapiDTO.getGermplasmProps().get(cvId).toString();

        //                List<CvDTO> result = germplasmPropsCv.stream()
        //                        .filter(a -> Objects.equals(a.getCvId().toString(), cvId))
        //                        .collect(Collectors.toList());

        //                if (result.size() > 0 && propValue != null) {
        //                    currentCallSetBrapiDTO.getAdditionalInfo().put(result.get(0).getTerm(), propValue);
        //                }
        //            }
        //        }

        //        if (currentCallSetBrapiDTO.getSampleProps().size() > 0) {

        //            for (String cvId : currentCallSetBrapiDTO.getSampleProps().keySet()) {

        //                String propValue = currentCallSetBrapiDTO.getSampleProps().get(cvId).toString();

        //                List<CvDTO> result = samplePropsCv.stream()
        //                        .filter(a -> Objects.equals(a.getCvId().toString(), cvId))
        //                        .collect(Collectors.toList());

        //                if (result.size() > 0 && propValue != null) {
        //                    currentCallSetBrapiDTO.getAdditionalInfo().put(result.get(0).getTerm(), propValue);
        //                }
        //            }

        //        }
        //    }
        //}
        //catch (GobiiException gE) {

        //    LOGGER.error(gE.getMessage(), gE);

        //    throw new GobiiDtoMappingException(
        //            gE.getGobiiStatusLevel(),
        //            gE.getGobiiValidationStatusType(),
        //            gE.getMessage());
        //}
        //catch (Exception e) {
        //    LOGGER.error("Gobii Mapping Error", e);
        //    throw new GobiiDtoMappingException(e);
        //}

        return returnVal;
    }

//    @Transactional
//    @Override
//    public List<DnaRunDTO> getListFromSearch(String searchResultsDbId) throws GobiiDtoMappingException {
//
//        List<DnaRunDTO> returnVal = new ArrayList<>();
//
//        try {
//
//            ResultSet resultSet = rsSearchQueryDao.getSearchQuery(searchResultsDbId);
//
//            if (resultSet.next()) {
//
//                JsonParser parser = new JsonParser();
//
//                JsonObject searchQueryJson = (JsonObject) parser.parse(resultSet.getString("query"));
//
//                Map<String, Object> sqlParams = new HashMap<>();
//
//                sqlParams.put("searchQuery", searchQueryJson);
//
//
//                returnVal = (List<DnaRunDTO>) dtoListQueryColl.getList(
//                        ListSqlId.QUERY_ID_DNARUN_SEARCH,
//                        null,
//                        sqlParams
//                );
//
//                System.out.print(searchQueryJson);
//
//
//            } else {
//                throw new GobiiDtoMappingException(GobiiStatusLevel.ERROR,
//                        GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
//                        "Search query does not exist for give");
//            }
//        }
//        catch (GobiiException gE) {
//
//            LOGGER.error(gE.getMessage(), gE);
//
//            throw new GobiiDtoMappingException(
//                    gE.getGobiiStatusLevel(),
//                    gE.getGobiiValidationStatusType(),
//                    gE.getMessage());
//        }
//        catch (Exception e) {
//            LOGGER.error("Gobii Mapping Error", e);
//            throw new GobiiDtoMappingException(e);
//        }
//
//        return returnVal;
//    }
}
