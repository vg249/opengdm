package org.gobiiproject.gobiidao.resultset.core.listquery;

import org.gobiiproject.gobiidao.resultset.core.StoredProcExec;
import org.gobiiproject.gobiimodel.config.GobiiException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Phil on 10/25/2016.
 */
public class DtoListQuery<T> {

    public enum QueryId {
        QUERY_ID_DATASET_ALL,
        QUERY_ID_CONTACT_ALL
    }

     private StoredProcExec storedProcExec = null;

    private Map<QueryId, String> queriesById = new HashMap<>();

    public DtoListQuery(StoredProcExec storedProcExec) {

        this.storedProcExec = storedProcExec;

        this.queriesById.put(QueryId.QUERY_ID_CONTACT_ALL,
                "select * from contact order by lower(lastname),lower(firstname)");
        this.queriesById.put(QueryId.QUERY_ID_DATASET_ALL,
                "select * from dataset order by lower(name)");
    }

    public List<T> getDtoList(QueryId queryId,
                              Class<T> dtoType,
                              Map<String, Object> parameters) throws GobiiException {

        String sql = this.queriesById.get(queryId);

        DtoListFromSql<T> dtoListFromSql = new DtoListFromSql<>(dtoType, sql, parameters);

        this.storedProcExec.doWithConnection(dtoListFromSql);

        List<T> returnVal = dtoListFromSql.getDtoList();


        return returnVal;

    } // getDtoList()
}
