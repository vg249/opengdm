package org.gobiiproject.gobiidao.cache;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.resultset.core.listquery.DtoListQueryColl;
import org.gobiiproject.gobiidao.resultset.core.listquery.ListSqlId;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import java.sql.ResultSet;
import java.util.Date;
import java.util.HashMap;

/***
 * The idiom here is not obvious. There is a lot of magic going on in the background via the annotations and the
 * cachemanager configuration in the application-config.xml file, which is where we set up the "tableTracker" cache.
 * The cache is essentially a kev-value pair storage mechanism. Because we specify the gobiiEntityNameType parameter as
 * the key, the values are keyed by that value. If getLasetModified() is called first, the returned
 * value is cached indefinitely. Subsequent calls to getLastModified() will return the value from the cache
 * for that gobiiEntityNameType. This behavior is determined by the @Cacheable annotation. The @CachePut annotation on
 * setLastModified() replaces the existing value for the specified gobiiEntityNameType. If @CachePut is called first,
 * getLastModified() does not replace that value but rather gets it from the store.
 *
 * Two separate caches are needed per the datatype of the item stored. If you have only one cache, both the date and count
 * methods are storing the data in the same cache and the data don't cast properly upon retrieval.
 */
public class TableTrackingCache {

    @Autowired
    private DtoListQueryColl dtoListQueryColl;

    @Cacheable(cacheNames = "tableLastUpdateCache", key = "#gobiiEntityNameType")
    public Date getLastModified(GobiiEntityNameType gobiiEntityNameType) {
        return new Date();
    }

    @CachePut(cacheNames = "tableLastUpdateCache", key = "#gobiiEntityNameType")
    public Date setLastModified(GobiiEntityNameType gobiiEntityNameType, Date date) {
        return date;
    }

    @Cacheable(cacheNames = "tableRecordCountCache", key = "#gobiiEntityNameType")
    public Integer getCount(GobiiEntityNameType gobiiEntityNameType) {
        return this.getCountFromDb(gobiiEntityNameType);
    }

    @CachePut(cacheNames = "tableRecordCountCache", key = "#gobiiEntityNameType")
    public Integer setCount(GobiiEntityNameType gobiiEntityNameType) throws GobiiDaoException {
        return this.getCountFromDb(gobiiEntityNameType);
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
}
