package org.gobiiproject.gobiidao.resultset.core.listquery;

import org.gobiiproject.gobiidao.GobiiDaoException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Phil on 10/25/2016.
 */
public class DtoListQueryColl {

    public void setListQueriesBySqlId(Map<ListSqlId, DtoListQuery> listQueriesBySqlId) {
        this.listQueriesBySqlId = listQueriesBySqlId;
    }

    public Map<ListSqlId, DtoListQuery> getListQueriesBySqlId() {
        return listQueriesBySqlId;
    }

    private Map<ListSqlId, DtoListQuery> listQueriesBySqlId = new HashMap<>();

    public List getList(ListSqlId listSqlId, Map<String, Object> parameters) throws GobiiDaoException {

        List returnVal;

        DtoListQuery dtoListQuery = listQueriesBySqlId.get(listSqlId);

        if (null != dtoListQuery ) {
            returnVal = dtoListQuery.getDtoList(parameters);
        } else {
            throw new GobiiDaoException("Unknown query id " + listSqlId.toString());
        }

        return returnVal;


    }
}
