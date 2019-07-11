package org.gobiiproject.gobiidtomapping.entity.auditable.impl;

import org.gobiiproject.gobiidao.resultset.core.listquery.DtoListQueryColl;
import org.gobiiproject.gobiidao.resultset.core.listquery.ListSqlId;
import org.gobiiproject.gobiidtomapping.core.GobiiDtoMappingException;
import org.gobiiproject.gobiidtomapping.entity.auditable.DtoMapDatasetBrapi;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.DataSetBrapiDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by VCalaminos on 7/10/2019.
 */
public class DtoMapDatasetBrapiImpl implements DtoMapDatasetBrapi {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapDatasetBrapiImpl.class);

    @Autowired
    private DtoListQueryColl dtoListQueryColl;

    @Override
    public List<DataSetBrapiDTO> getList(Integer pageToken, Integer pageSize, DataSetBrapiDTO dataSetBrapiDTO) throws GobiiDtoMappingException {

        List<DataSetBrapiDTO> returnVal;

        try {

            Map<String, Object> sqlParams = new HashMap<>();

            if (pageToken != null) {
                sqlParams.put("pageToken", pageToken);
            }

            if (pageSize != null) {
                sqlParams.put("pageSize", pageSize);
            }

            returnVal = (List<DataSetBrapiDTO>) dtoListQueryColl.getList(
                    ListSqlId.QUERY_ID_DATASET_ALL_BRAPI,
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
                    gE.getMessage()
            );
        }
        catch (Exception e) {
            LOGGER.error("Gobii Mapping Error", e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;
    }
}
