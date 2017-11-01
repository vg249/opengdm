package org.gobiiproject.gobiidtomapping.entity.noaudit.impl;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.cache.EntityCountState;
import org.gobiiproject.gobiidao.cache.ParentChildCountState;
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
    public EntityStatsDTO getEntityLastModified(GobiiEntityNameType gobiiEntityNameType) throws GobiiDtoMappingException {

        EntityStatsDTO returnVal = new EntityStatsDTO();


        try {


            //When the data type of the date_modified column is changed to timestamp, we can go
            //to getting the value from the table. For now we will use the cache, which is
            //adequate only for the purpose of the client knowing when to refresh the values for
            //a particular entity.
//            String tableName = gobiiEntityNameType.toString().toUpperCase();
//            ResultSet resultSet = dtoListQueryColl.getResultSet(ListSqlId.QUERY_ID_LAST_MODIFIED,
//                    null,
//                    new HashMap<String, Object>() {{
//                        put("tableName", tableName);
//                    }});
//
//            if (resultSet.next()) {
//                LocalDate localDate = resultSet.getObject("lastmodified", LocalDate.class);
//                Date date = java.sql.Date.valueOf(localDate);
//                returnVal.setLastModified(date);
//            }
//

            Date lastModified = this.tableTrackingCache.getLastModified(gobiiEntityNameType);
            returnVal.setLastModified(lastModified);

        } catch (Exception e) {
            LOGGER.error("Gobii Mapping error", e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;
    }

    @Override
    public EntityStatsDTO getEntityCount(GobiiEntityNameType gobiiEntityNameType) throws GobiiDtoMappingException {

        EntityStatsDTO returnVal = new EntityStatsDTO();

        try {

            Integer count;
            EntityCountState entityCountState = this.tableTrackingCache.getCount(gobiiEntityNameType);
            if( null == entityCountState ) {
                count = this.getCountFromDb(gobiiEntityNameType);
                Date lastModified = this.tableTrackingCache.getLastModified(gobiiEntityNameType);
                EntityCountState entityCountState1New = new EntityCountState(gobiiEntityNameType,lastModified,count);
                this.tableTrackingCache.setCount(gobiiEntityNameType,entityCountState1New);
            } else {
                Date lastModified = this.tableTrackingCache.getLastModified(gobiiEntityNameType);
                if( lastModified.equals(entityCountState.getLastModified())) {
                    count = entityCountState.getCount();
                } else {
                    count = this.getCountFromDb(gobiiEntityNameType);
                    entityCountState.setLastModified(lastModified);
                    entityCountState.setCount(count);
                }

            }

            returnVal.setCount(count);

        } catch (Exception e) {
            LOGGER.error("Gobii Mapping error", e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;
    }

    @Override
    public EntityStatsDTO getEntityCountOfChildren(GobiiEntityNameType gobiiEntityNameTypeParent,
                                            Integer parentId,
                                            GobiiEntityNameType gobiiEntityNameTypeChild ) throws GobiiDtoMappingException {

        EntityStatsDTO returnVal = new EntityStatsDTO();

        try {

            Integer count;

            ParentChildCountState parentChildCountState = this.tableTrackingCache.getParentChildState(gobiiEntityNameTypeParent, parentId, gobiiEntityNameTypeChild);

            //if either eintity has been modified, we reset; there will be false positives, but we're still benefiting from caching
            if (!this.tableTrackingCache.getLastModified(gobiiEntityNameTypeParent).equals(parentChildCountState.getParentLastModified())
                    || !this.tableTrackingCache.getLastModified(gobiiEntityNameTypeChild).equals(parentChildCountState.getChildLastModified())) {

                count = this.getCountOfChildFromDb(gobiiEntityNameTypeParent, parentId, gobiiEntityNameTypeChild);
                parentChildCountState.setCount(count);
                parentChildCountState.setParentLastModified(this.tableTrackingCache.getLastModified(parentChildCountState.getGobiiEntityNameTypeParent()));
                parentChildCountState.setChildLastModified(this.tableTrackingCache.getLastModified(parentChildCountState.getGobiiEntityNameTypeChild()));

            } else {
                count = parentChildCountState.getCount();
            }


            returnVal.setCount(count);

        } catch (Exception e) {
            LOGGER.error("Gobii Mapping error", e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;
    }

    private Integer getCountOfChildFromDb(GobiiEntityNameType gobiiEntityNameTypeParent,
                                          Integer parentId,
                                          GobiiEntityNameType gobiiEntityNameTypeChild) {

        Integer returnVal = 0;

        try {

            String parentTable = gobiiEntityNameTypeParent.toString().toUpperCase();
            String childTable = gobiiEntityNameTypeChild.toString().toUpperCase();
            ResultSet resultSet = dtoListQueryColl.getResultSet(ListSqlId.QUERY_ID_COUNT_OF_CHILDREN,
                    new HashMap<String, Object>() {{
                        put("parentId", parentId);
                    }}, new HashMap<String, Object>() {{
                        put("tableNameParent", parentTable);
                        put("tableNameChild", childTable);
                    }});

            if (resultSet.next()) {
                returnVal = resultSet.getInt("count");
            }
        } catch (Exception e) {
            throw new GobiiDaoException(e);
        }

        return returnVal;
    }


    private Integer getCountFromDb(GobiiEntityNameType gobiiEntityNameType) {

        Integer returnVal = 0;

        try {

            String tableName = gobiiEntityNameType.toString().toUpperCase();
            ResultSet resultSet = dtoListQueryColl.getResultSet(ListSqlId.QUERY_ID_COUNT,
                    null,
                    new HashMap<String, Object>() {{
                        put("tableName", tableName);
                    }});

            if (resultSet.next()) {
                returnVal = resultSet.getInt("count");
            }
        } catch (Exception e) {
            throw new GobiiDaoException(e);
        }

        return returnVal;
    }
} // DtoMapNameIdListImpl

