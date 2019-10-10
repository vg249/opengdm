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
public class ListStatementMarkerAll implements ListStatement {


    @Override
    public ListSqlId getListSqlId() {
        return QUERY_ID_DATASET_ALL;
    }

    @Override
    public PreparedStatement makePreparedStatement(Connection dbConnection, Map<String, Object> jdbcParamVals, Map<String, Object> sqlParamVals) throws SQLException {

        String sql = "select m.marker_id,\n" +
                "p.platform_id,\n" +
                "m.variant_id, \n" +
                "m.name \"marker_name\", \n" +
                "m.code, \n" +
                "m.ref, \n" +
                "m.alts, \n" +
                "m.sequence, \n" +
                "m.reference_id, \n" +
                "m.strand_id, \n" +
                "m.status, \n" +
                "p.name \"platform_name\"\n" +
                "from marker m\n" +
                "join platform p on (m.platform_id=p.platform_id)\n" +
                "order by lower(m.name)";

        PreparedStatement returnVal = dbConnection.prepareStatement(sql);

        return returnVal;
    }
}
