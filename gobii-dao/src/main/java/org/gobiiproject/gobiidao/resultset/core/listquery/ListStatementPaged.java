package org.gobiiproject.gobiidao.resultset.core.listquery;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

/***
 * Classes that implement this interface will provide sql for the page frame and single page queries.
 */
public interface ListStatementPaged {

    ListSqlId getListSqlId();
    String getNameColName();
    String getIdColName();
    String getPageNumberColName();
    PreparedStatement makePreparedStatementForPageFrames(Connection dbConnection, Integer pageSize ) throws SQLException;
    PreparedStatement makePreparedStatementForAPage(Connection dbConnection, Integer pageSize, String pgItemNameVal, Integer pgItemIdVal) throws SQLException;
}
