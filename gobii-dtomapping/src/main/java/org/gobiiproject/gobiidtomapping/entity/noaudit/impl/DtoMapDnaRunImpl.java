package org.gobiiproject.gobiidtomapping.entity.noaudit.impl;

import org.gobiiproject.gobiidao.resultset.access.impl.RsDnaRunDaoImpl;
import org.gobiiproject.gobiidao.resultset.core.ResultColumnApplicator;
import org.gobiiproject.gobiidtomapping.core.GobiiDtoMappingException;
import org.gobiiproject.gobiidtomapping.entity.noaudit.DtoMapDnaRun;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.DnaRunDTO;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Array;
import java.sql.ResultSet;
import java.util.Arrays;

/**
 * Created by VCalaminos on 6/25/2019.
 */
public class DtoMapDnaRunImpl implements DtoMapDnaRun {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapDnaRunImpl.class);

    @Autowired
    private RsDnaRunDaoImpl rsDnaRunDao;

    public DnaRunDTO get(Integer dnaRunId) throws GobiiDtoMappingException {

        DnaRunDTO returnVal = new DnaRunDTO();

        try {
            ResultSet resultSet = rsDnaRunDao.getDnaRunForDnaRunId(dnaRunId);
            if (resultSet.next()) {

                returnVal.setCallSetDbId(resultSet.getInt("dnarun_id"));
                returnVal.setCallSetName(resultSet.getString("name"));
                returnVal.setSampleDbId(resultSet.getInt("dnasample_id"));
                returnVal.setDnaRunCode(resultSet.getString("code"));

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
}
