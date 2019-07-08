package org.gobiiproject.gobiidtomapping.entity.noaudit.impl;

import org.gobiiproject.gobiidao.resultset.access.RsDnaRunDao;
import org.gobiiproject.gobiidao.resultset.core.ResultColumnApplicator;
import org.gobiiproject.gobiidao.resultset.core.listquery.DtoListQueryColl;
import org.gobiiproject.gobiidao.resultset.core.listquery.ListSqlId;
import org.gobiiproject.gobiidtomapping.core.GobiiDtoMappingException;
import org.gobiiproject.gobiidtomapping.entity.noaudit.DtoMapDnaRun;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.DnaRunDTO;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Array;
import java.sql.ResultSet;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by VCalaminos on 6/25/2019.
 */
public class DtoMapDnaRunImpl implements DtoMapDnaRun {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapDnaRunImpl.class);

    @Autowired
    private RsDnaRunDao rsDnaRunDao;

    @Autowired
    private DtoListQueryColl dtoListQueryColl;

    @Transactional
    @Override
    public DnaRunDTO get(Integer dnaRunId) throws GobiiDtoMappingException {

        DnaRunDTO returnVal = new DnaRunDTO();

        try {
            ResultSet resultSet = rsDnaRunDao.getDnaRunForDnaRunId(dnaRunId);
            if (resultSet.next()) {

                ResultColumnApplicator.applyColumnValues(resultSet, returnVal);

                if (returnVal.getDatasetDnarunIndex().size() > 0) {

                    for (String dataSetId : returnVal.getDatasetDnarunIndex().keySet()) {

                        if (dataSetId != null) {
                            returnVal.getVariantSetIds().add(Integer.parseInt(dataSetId));
                        }
                    }
                }

                if (resultSet.next()) {
                    throw new GobiiDtoMappingException(GobiiStatusLevel.ERROR,
                            GobiiValidationStatusType.VALIDATION_NOT_UNIQUE,
                            "Multiple resources found. Violation of unique Dnarun ID constraint." +
                                    " Please contact your Data Administrato to resolve this. " +
                                    "Changing underlying database schemas and constraints " +
                                    "without consulting GOBii Team is not recommended.");
                }
            }
            else {
                throw new GobiiDtoMappingException(GobiiStatusLevel.ERROR,
                        GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                        "Dna run not found for given id.");
            }

        }
        catch (GobiiException gE) {
            LOGGER.error(gE.getMessage(), gE);
            throw new GobiiDtoMappingException(
                    gE.getGobiiStatusLevel(),
                    gE.getGobiiValidationStatusType(),
                    gE.getMessage());
        }
        catch (Exception e) {
            LOGGER.error("Gobii Mapping error", e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;
    }

    @Override
    public List<DnaRunDTO> getList(Integer pageToken, Integer pageSize, DnaRunDTO dnaRunDTOFilter) throws GobiiDtoMappingException {

        List<DnaRunDTO> returnVal;
        try {

            Map<String, Object> sqlParams = new HashMap<>();

            if (pageToken != null) {
                sqlParams.put("pageToken", pageToken);
            }

            if (pageSize != null) {
                sqlParams.put("pageSize", pageSize);
            }

            if (dnaRunDTOFilter != null) {

                if (dnaRunDTOFilter.getCallSetName() != null) {
                    sqlParams.put("callSetName", dnaRunDTOFilter.getCallSetName());
                }

                if (dnaRunDTOFilter.getVariantSetIds().size() > 0) {
                    sqlParams.put("variantSetDbId", dnaRunDTOFilter.getVariantSetIds().get(0));
                }

                if (dnaRunDTOFilter.getSampleDbId() != null) {
                    sqlParams.put("sampleDbId", dnaRunDTOFilter.getSampleDbId());
                }

                if (dnaRunDTOFilter.getGermplasmDbId() != null) {
                    sqlParams.put("germplasmDbId", dnaRunDTOFilter.getGermplasmDbId());
                }

                if (dnaRunDTOFilter.getStudyDbId() != null) {
                    sqlParams.put("studyDbId", dnaRunDTOFilter.getStudyDbId());
                }
            }

            returnVal = (List<DnaRunDTO>) dtoListQueryColl.getList(
                    ListSqlId.QUERY_ID_DNARUN_ALL,
                    null,
                    sqlParams
            );

            if (returnVal == null) {
                return new ArrayList<>();
            }

            for(DnaRunDTO currentDnaRunDTO : returnVal) {

                if (currentDnaRunDTO.getDatasetDnarunIndex().size() > 0) {

                    for (String dataSetId : currentDnaRunDTO.getDatasetDnarunIndex().keySet()) {

                        if (dataSetId != null) {
                            currentDnaRunDTO.getVariantSetIds().add(Integer.parseInt(dataSetId));
                        }
                    }
                }
            }

        }
        catch (GobiiException gE) {

            LOGGER.error(gE.getMessage(), gE);

            throw new GobiiDtoMappingException(
                    gE.getGobiiStatusLevel(),
                    gE.getGobiiValidationStatusType(),
                    gE.getMessage());
        }
        catch (Exception e) {
            LOGGER.error("Gobii Mapping Error", e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;
    }
}
