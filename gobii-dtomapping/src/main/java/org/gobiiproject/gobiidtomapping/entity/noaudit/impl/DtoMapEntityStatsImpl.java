package org.gobiiproject.gobiidtomapping.entity.noaudit.impl;

import org.gobiiproject.gobiidao.cache.TableTrackingCache;
import org.gobiiproject.gobiidao.resultset.access.RsCvDao;
import org.gobiiproject.gobiidao.resultset.core.ParamExtractor;
import org.gobiiproject.gobiidao.resultset.core.ResultColumnApplicator;
import org.gobiiproject.gobiidao.resultset.core.listquery.DtoListQueryColl;
import org.gobiiproject.gobiidao.resultset.core.listquery.ListSqlId;
import org.gobiiproject.gobiidtomapping.core.GobiiDtoMappingException;
import org.gobiiproject.gobiidtomapping.entity.noaudit.DtoMapCv;
import org.gobiiproject.gobiidtomapping.entity.noaudit.DtoMapCvGroup;
import org.gobiiproject.gobiidtomapping.entity.noaudit.DtoMapEntityStats;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.CvDTO;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.CvGroupDTO;
import org.gobiiproject.gobiimodel.dto.system.EntityStatsDTO;
import org.gobiiproject.gobiimodel.types.GobiiCvGroupType;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Phil on 4/6/2016.
 */
public class DtoMapEntityStatsImpl implements DtoMapEntityStats {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapEntityStatsImpl.class);


    @Autowired
    private DtoListQueryColl dtoListQueryColl;

    @Autowired
    private TableTrackingCache tableTrackingCache;


    @Override
    public EntityStatsDTO getEntityCount(GobiiEntityNameType gobiiEntityNameType) throws GobiiDtoMappingException {

        EntityStatsDTO returnVal = new EntityStatsDTO();


        try {
            /*
            String tableName = gobiiEntityNameType.toString().toUpperCase();
            ResultSet resultSet = dtoListQueryColl.getResultSet(ListSqlId.QUERY_ID_LAST_MODIFIED,
                    null,
                    new HashMap<String, Object>() {{
                        put("tableName", tableName);
                    }});

            if (resultSet.next()) {
                LocalDate localDate = resultSet.getObject("lastmodified", LocalDate.class);
                Date date = java.sql.Date.valueOf(localDate);
                returnVal.setLastModified(date);
            } */

            Date lastModified = this.tableTrackingCache.getLastModified(gobiiEntityNameType);
            returnVal.setLastModified(lastModified);

        } catch (Exception e) {
            LOGGER.error("Gobii Mapping error", e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;
    }

} // DtoMapNameIdListImpl
