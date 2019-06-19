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

        String sql = " 	select " +
                    "	j.job_id, " +
                    "	getcvterm(j.type_id) as type, " +
                    "	getcvterm(j.payload_type_id) as payload_type,  " +
                    "	getcvterm(j.status) as status,  " +
                    "	j.message,  " +
                    "	j.submitted_by,  " +
                    "	j.submitted_date,  " +
                    "	j.name,  " +
                    "	array(  " +
                    "		select  " +
                     "			d.dataset_id " +
                    "		from " +
                    "			dataset d " +
                    "		where " +
                    "			d.job_id = j.job_id " +
                    "	) as datasetids " +
                    " from " +
                    "	dataset d " +
                    "	join job j on (d.job_id=j.job_id) " +
                    " where array_length( " +
                    "		array( " +
                    "			select " +
                    "				ds.dataset_id " +
                    "			from " +
                    "				dataset ds " +
                    "			where " +
                    "				ds.job_id = j.job_id " +
                    "		), " +
                    "		1 " +
                    "	) > 0 " +
                    "	and d.dataset_id = ?";

        PreparedStatement returnVal = dbConnection.prepareStatement(sql);
        Integer datasetId = (Integer) jdbcParamVals.get("datasetId");
        returnVal.setInt(1, datasetId);

        return returnVal;
    }
}
