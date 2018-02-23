package org.gobiiproject.gobiidao.resultset.sqlworkers.read.liststatement.discrete;

import org.gobiiproject.gobiidao.resultset.core.listquery.ListSqlId;
import org.gobiiproject.gobiidao.resultset.core.listquery.ListStatement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

import static org.gobiiproject.gobiidao.resultset.core.listquery.ListSqlId.QUERY_ID_DATASET_ALL;
import static org.gobiiproject.gobiidao.resultset.core.listquery.ListSqlId.QUERY_ID_JOB_BY_DATASET_ID;

/**

 */
public class ListStatementJobByDatasetId implements ListStatement {


    @Override
    public ListSqlId getListSqlId() {
        return QUERY_ID_JOB_BY_DATASET_ID;
    }

    @Override
    public PreparedStatement makePreparedStatement(Connection dbConnection, Map<String, Object> jdbcParamVals, Map<String, Object> sqlParamVals) throws SQLException {

        String sql = "select" +
                " j.job_id, " +
                " type.term as type_id," +
                " payloadtype.term as payload_type_id," +
                " status.term as status," +
                " j.message," +
                " j.submitted_by," +
                " j.submitted_date," +
                " j.name," +
                " ARRAY(select d.dataset_id from dataset d where d.job_id = j.job_id) as datasetids " +
                " from" +
                " job j," +
                " cv type," +
                " cv payloadtype," +
                " cv status" +
                " where" +
                " j.type_id = type.cv_id" +
                " and j.payload_type_id = payloadtype.cv_id" +
                " and j.status = status.cv_id" +
                " and array_length( ARRAY(select d.dataset_id from dataset d where d.job_id = j.job_id), 1) > 0 " +
                " and d.dataset_id = ?";

        PreparedStatement returnVal = dbConnection.prepareStatement(sql);
        Integer datasetId = (Integer) jdbcParamVals.get("datasetId");
        returnVal.setInt(1, datasetId);

        return returnVal;
    }
}
