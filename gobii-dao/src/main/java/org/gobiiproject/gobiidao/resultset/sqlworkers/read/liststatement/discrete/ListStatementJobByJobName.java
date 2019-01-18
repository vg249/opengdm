package org.gobiiproject.gobiidao.resultset.sqlworkers.read.liststatement.discrete;

import org.gobiiproject.gobiidao.resultset.core.listquery.ListSqlId;
import org.gobiiproject.gobiidao.resultset.core.listquery.ListStatement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

import static org.gobiiproject.gobiidao.resultset.core.listquery.ListSqlId.QUERY_ID_JOB_BY_JOBNAME;

/**

 */
public class ListStatementJobByJobName implements ListStatement {


    @Override
    public ListSqlId getListSqlId() {
        return QUERY_ID_JOB_BY_JOBNAME;
    }

    @Override
    public PreparedStatement makePreparedStatement(Connection dbConnection, Map<String, Object> jdbcParamVals, Map<String, Object> sqlParamVals) throws SQLException {

        String sql = "select\n" +
                "j.job_id,\n" +
                "type.term as type,\n" +
                "payloadtype.term as payload_type,\n" +
                "status.term as status,\n" +
                "j.message,\n" +
                "j.submitted_by,\n" +
                "j.submitted_date,\n" +
                "j.name,\n" +
                "ARRAY(select d.dataset_id from dataset d where d.job_id = j.job_id) as datasetids \n" +
                "from job j,\n" +
                "cv type,\n" +
                "cv payloadtype,\n" +
                "cv status\n" +
                "where j.type_id = type.cv_id\n" +
                "and j.payload_type_id = payloadtype.cv_id\n" +
                "and j.status = status.cv_id\n" +
                "and j.name = ?";

        PreparedStatement returnVal = dbConnection.prepareStatement(sql);
        String jobName = (String) jdbcParamVals.get("jobName");
        returnVal.setString(1, jobName);

        return returnVal;
    }
}
