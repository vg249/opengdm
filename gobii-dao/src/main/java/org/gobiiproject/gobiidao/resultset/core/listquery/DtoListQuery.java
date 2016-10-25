package org.gobiiproject.gobiidao.resultset.core.listquery;

import org.gobiiproject.gobiidao.resultset.core.StoredProcExec;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by Phil on 10/25/2016.
 */
public class DtoListQuery<T> {


    private ListSqlId listSqlId;
    private Class<T> dtoType;
    private StoredProcExec storedProcExec;

    public DtoListQuery(StoredProcExec storedProcExec,
                        Class<T> dtoType,
                        ListSqlId listSqlId) {

        this.storedProcExec = storedProcExec;
        this.dtoType = dtoType;
        this.listSqlId = listSqlId;

    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<T> getDtoList(Map<String, Object> parameters) throws GobiiException {

        String sql = listSqlId.getSql();

        DtoListFromSql<T> dtoListFromSql = new DtoListFromSql<>(dtoType, sql, parameters);

        this.storedProcExec.doWithConnection(dtoListFromSql);

        List<T> returnVal = dtoListFromSql.getDtoList();


        return returnVal;

    } // getDtoList()
}
