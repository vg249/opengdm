package org.gobiiproject.gobiidtomapping.entity.noaudit.impl;

import org.gobiiproject.gobiidao.resultset.core.listquery.DtoListQueryColl;
import org.gobiiproject.gobiidao.resultset.core.listquery.ListSqlId;
import org.gobiiproject.gobiidtomapping.core.GobiiDtoMappingException;
import org.gobiiproject.gobiidtomapping.entity.noaudit.DtoMapGenotypeCalls;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.GenotypeCallsDTO;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DtoMapGenotypeCallsImpl implements DtoMapGenotypeCalls {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapGenotypeCallsImpl.class);

    @Autowired
    private DtoListQueryColl dtoListQueryColl;


    @Override
    public List<GenotypeCallsDTO> getListByDnarunId(Integer dnarunId,
                                                    Integer pageToken,
                                                    Integer pageSize) throws GobiiDtoMappingException {

        List<GenotypeCallsDTO> returnVal;

        try {

            Map<String, Object> sqlParams = new HashMap<>();

            if(dnarunId != null) {
                sqlParams.put("dnarunId", dnarunId);
            }
            else {

                LOGGER.error("dnarunId is null");

                throw new GobiiDtoMappingException(
                        GobiiStatusLevel.ERROR,
                        GobiiValidationStatusType.BAD_REQUEST,
                        "Invalid Dnarun Id");
            }

            if (pageToken != null) {
                sqlParams.put("pageToken", pageToken);
            }

            if (pageSize != null) {
                sqlParams.put("pageSize", pageSize);
            }



            returnVal = (List<GenotypeCallsDTO>) dtoListQueryColl.getList(
                    ListSqlId.QUERY_ID_GENOTYPE_CALLS_BY_DNARUN_ID,
                    null,
                    sqlParams
            );

            if (returnVal == null) {
                return new ArrayList<>();
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
