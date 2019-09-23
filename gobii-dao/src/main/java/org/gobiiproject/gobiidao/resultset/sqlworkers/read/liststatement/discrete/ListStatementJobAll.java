package org.gobiiproject.gobiidao.resultset.sqlworkers.read.liststatement.discrete;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import org.gobiiproject.gobiidao.resultset.core.listquery.ListSqlId;
import org.gobiiproject.gobiidao.resultset.core.listquery.ListStatement;
import static org.gobiiproject.gobiidao.resultset.core.listquery.ListSqlId.QUERY_ID_DATASET_ALL;

/**

 */
public class ListStatementJobAll implements ListStatement {


    @Override
    public ListSqlId getListSqlId() {
        return QUERY_ID_DATASET_ALL;
    }

    @Override
    public PreparedStatement makePreparedStatement(Connection dbConnection, Map<String, Object> jdbcParamVals, Map<String, Object> sqlParamVals) throws SQLException {

        String sql = "select \n" +
                "j.job_id,\n" +
                "type.term as type,\n" +
                "payloadtype.term as payload_type,\n" +
                "status.term as status,\n" +
                "j.message,\n" +
                "j.submitted_by,\n" +
                "j.submitted_date,\n" +
                "j.name,\n" +
                "	ARRAY(select d.dataset_id \n" +
                "   from dataset d " +
                "    where d.job_id = j.job_id) as datasetids " +
                "from job j\n" +
                "left JOIN\n" +
                "dataset d\n" +
                "on d.job_id = j.job_id,\n" +
                "cv type,\n" +
                "cv payloadtype,\n" +
                "cv status\n" +
                "where j.type_id = type.cv_id\n" +
                "and j.payload_type_id = payloadtype.cv_id\n" +
                "and j.status = status.cv_id\n" +
                "order by j.name";

        PreparedStatement returnVal = dbConnection.prepareStatement(sql);

        return returnVal;
    }
}
