package org.gobiiproject.gobiidao.resultset.sqlworkers.read.liststatement;

import org.gobiiproject.gobiidao.resultset.core.listquery.ListSqlId;
import org.gobiiproject.gobiidao.resultset.core.listquery.ListStatement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

import static org.gobiiproject.gobiidao.resultset.core.listquery.ListSqlId.QUERY_ID_DATASET_ALL;

/**

 */
public class ListStatementDatasetAll implements ListStatement {

    @Override
    public ListSqlId getListSqlId() {
        return QUERY_ID_DATASET_ALL;
    }

    @Override
    public PreparedStatement makePreparedStatement(Connection dbConnection, Map<String, Object> jdbcParamVals, Map<String, Object> sqlParamVals) throws SQLException {

        String sql = "select ds.*, " +
                "j.status \"jobstatusid\"," +
                "case " +
                "when j.status is not null " +
                "then (select cv.term from cv where cvgroup_id = (select cvgroup_id from cvgroup where name='job_status' and type=1) and cv.cv_id=j.status) " +
                "else 'Unsubmitted' " +
                "end " +
                "as jobstatusname, " +
                "j.type_id \"jobtypeid\"," +
                "case " +
                "when j.type_id is not null " +
                "then (select cv.term from cv where cvgroup_id = (select cvgroup_id from cvgroup where name='job_type' and type=1) and cv.cv_id=j.type_id) " +
                "else 'n/a' " +
                "end " +
                "as jobtypename , " +
                "j.submitted_date as jobsubmitteddate, " +
                "null as totalsamples, " +
                "null as totalmarkers " +
                "from dataset ds " +
                "left outer join job j on (ds.job_id=j.job_id) " +
                "order by lower(ds.name)";

        PreparedStatement returnVal = dbConnection.prepareStatement(sql);

        return returnVal;
    }
}
