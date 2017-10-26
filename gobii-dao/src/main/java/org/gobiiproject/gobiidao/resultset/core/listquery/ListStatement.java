package org.gobiiproject.gobiidao.resultset.core.listquery;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

public interface ListStatement {

    ListSqlId getListSqlId();
    PreparedStatement makePreparedStatement(Connection dbConnection, Map<String, Object> jdbcParamVals) throws SQLException;
}
