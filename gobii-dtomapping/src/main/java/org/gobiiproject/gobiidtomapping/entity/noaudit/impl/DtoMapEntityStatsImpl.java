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

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
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


    // These are the ones that are of type DTOBaseAuditable
    // there are others in GobiiEntityNameType that only derive from DTOBase
    // and because they lack modified/created date columns we cannot
    // gather statistics on them
    List<GobiiEntityNameType> trackableEntityNames = Arrays.asList(
            GobiiEntityNameType.ANALYSIS,
            GobiiEntityNameType.CONTACT,
            GobiiEntityNameType.DATASET,
            GobiiEntityNameType.DISPLAY,
            GobiiEntityNameType.EXPERIMENT,
            GobiiEntityNameType.MANIFEST,
            GobiiEntityNameType.MAPSET,
            GobiiEntityNameType.MARKER_GROUP,
            GobiiEntityNameType.ORGANIZATION,
            GobiiEntityNameType.PLATFORM,
            GobiiEntityNameType.PROJECT,
            GobiiEntityNameType.PROTOCOL,
            GobiiEntityNameType.REFERENCE,
            GobiiEntityNameType.CV // CV does not have the columns but it doesn't matter for now because are using the cache instead
    );

    /***
     * Teh distinction between DTOs that derive from DTOBaseAuditable verses those that do not
     * was baseed on the assumption that the modified by and user columns in the tables is
     * meaingful with respect to tracking when something was last updated. However, since those
     * columns are of a type that doesn't track the actual time of the update, making them useless.
     * Thus, the aspect that is tracking these entities should be targeted against all crate() and
     * replace() methods so that it doesn't matter what anything is derived from. This approach would eliminate
     * the need for the trackableEntityNames list, which is really just an awkward way of breaking inheritance.
     * 
     * @return
     * @throws GobiiDtoMappingException
     */
    @Override
    public List<EntityStatsDTO> getAll() throws GobiiDtoMappingException {

        List<EntityStatsDTO> returnVal = new ArrayList<>();

        for (GobiiEntityNameType currentTypeName : trackableEntityNames) {

            if (currentTypeName != GobiiEntityNameType.UNKNOWN) {

                EntityStatsDTO currentEntityStatsDTO = this.getEntityCount(currentTypeName);
                returnVal.add(currentEntityStatsDTO);
                currentEntityStatsDTO.setEntityNameType(currentTypeName);
                currentEntityStatsDTO.setLastModified(this.tableTrackingCache.getLastModified(currentTypeName));
            }
        }


        return returnVal;
    }


    @Override
    public EntityStatsDTO getEntityLastModified(GobiiEntityNameType gobiiEntityNameType) throws GobiiDtoMappingException {

        EntityStatsDTO returnVal = new EntityStatsDTO();


        if(!trackableEntityNames.contains(gobiiEntityNameType)) {
            throw new GobiiDtoMappingException("The specified entity cannot be tracked because the entity lacks the required tracking columns: " + gobiiEntityNameType.toString());
        }

        try {


            //When the data type of the date_modified column is changed to timestamp, we can go
            //to getting the value from the table. For now we will use the cache, which is
            //adequate only for the purpose of the client knowing when to refresh the values for
            //a particular entity.
            // When we do retrive the value from the db, the jdbc idiom for retrieving a Date is
            // LocalDate localDate = resultSet.getObject("lastmodified", LocalDate.class);
            returnVal.setEntityNameType(gobiiEntityNameType);
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

        if(!trackableEntityNames.contains(gobiiEntityNameType)) {
            throw new GobiiDtoMappingException("The specified entity cannot be tracked because the entity lacks the required tracking columns: " + gobiiEntityNameType.toString());
        }

        try {

            Integer count;
            EntityCountState entityCountState = this.tableTrackingCache.getCount(gobiiEntityNameType);
            if (null == entityCountState) {
                count = this.getCountFromDb(gobiiEntityNameType);
                Date lastModified = this.tableTrackingCache.getLastModified(gobiiEntityNameType);
                EntityCountState entityCountState1New = new EntityCountState(gobiiEntityNameType, lastModified, count);
                this.tableTrackingCache.setCount(gobiiEntityNameType, entityCountState1New);
            } else {
                Date lastModified = this.tableTrackingCache.getLastModified(gobiiEntityNameType);
                if (lastModified.equals(entityCountState.getLastModified())) {
                    count = entityCountState.getCount();
                } else {
                    count = this.getCountFromDb(gobiiEntityNameType);
                    entityCountState.setLastModified(lastModified);
                    entityCountState.setCount(count);
                }

            }

            returnVal.setEntityNameType(gobiiEntityNameType);
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
                                                   GobiiEntityNameType gobiiEntityNameTypeChild) throws GobiiDtoMappingException {

        EntityStatsDTO returnVal = new EntityStatsDTO();

        // we should also be checking to see if the child really is a child of the parent. But the sql error
        // from the query that will be dynamically assembled will tell us that
        if(!trackableEntityNames.contains(gobiiEntityNameTypeParent)) {
            throw new GobiiDtoMappingException("The specified entity cannot be tracked because the entity lacks the required tracking columns: " + gobiiEntityNameTypeParent.toString());
        }

        if(!trackableEntityNames.contains(gobiiEntityNameTypeChild)) {
            throw new GobiiDtoMappingException("The specified entity cannot be tracked because the entity lacks the required tracking columns: " + gobiiEntityNameTypeChild.toString());
        }

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

            returnVal.setEntityNameType(gobiiEntityNameTypeChild);
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

