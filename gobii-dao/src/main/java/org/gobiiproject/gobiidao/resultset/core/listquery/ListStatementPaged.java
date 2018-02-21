package org.gobiiproject.gobiidao.resultset.core.listquery;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

public interface ListStatementPaged {

    ListSqlId getListSqlId();
    PreparedStatement makePreparedStatementForPageFrames(Connection dbConnection, Map<String, Object> sqlParamVals) throws SQLException;
    PreparedStatement makePreparedStatementForAPage(Connection dbConnection, Integer pageSize, String pgItemNameVal, String pgItemIdVal) throws SQLException;
}
