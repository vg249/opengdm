package org.gobiiproject.gobiidao.resultset.sqlworkers.read.liststatement.discrete.sampletracking;

import org.gobiiproject.gobiidao.resultset.core.listquery.ListSqlId;
import org.gobiiproject.gobiidao.resultset.core.listquery.ListStatement;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

import static org.gobiiproject.gobiidao.resultset.core.listquery.ListSqlId.QUERY_ID_PROJECT_ALL;

public class ListStatementProjectAll implements ListStatement {

    @Override
    public ListSqlId getListSqlId() {
        return QUERY_ID_PROJECT_ALL;
    }

    @Override
    public PreparedStatement makePreparedStatement(Connection dbConnection, Map<String, Object> jdbcParamVals,
                                                   Map<String, Object> sqlParamVals)
            throws SQLException {

        String pageCondition = "";

        if(sqlParamVals.getOrDefault("pageToken", 0) instanceof Integer) {
            if ((Integer) sqlParamVals.getOrDefault("pageToken", 0) > 0) {
                pageCondition = " WHERE project_id > ?";
            }
        }
        else {
            throw new GobiiException(GobiiStatusLevel.ERROR, GobiiValidationStatusType.BAD_REQUEST,
                    "Invalid Page Token");
        }

        if(sqlParamVals.getOrDefault("pageToken", 0) instanceof Integer) {
            if ((Integer) sqlParamVals.getOrDefault("pageToken", 0) > 0) {
                pageCondition = " WHERE project_id > ?";
            }
        }
        else {
            throw new GobiiException(GobiiStatusLevel.ERROR, GobiiValidationStatusType.BAD_REQUEST,
                    "Invalid Page Size");
        }


        String sql = "SELECT project.*, project_prop.properties AS system_properties " +
                "FROM project LEFT OUTER JOIN (" +
                "SELECT p.project_id AS project_id, " +
                "json_object(array_agg(cv.term::text), array_agg(p.props->>p.props_keys::text)) AS properties " +
                "FROM(SELECT project.project_id, project.props, jsonb_object_keys(props)::int as props_keys " +
                "FROM project " + pageCondition +
                "ORDER BY project_id LIMIT ?) AS p " +
                "INNER JOIN cv AS cv ON (cv.cv_id = p.props_keys) GROUP BY 1) AS project_prop " +
                "USING(project_id)" + pageCondition + "ORDER BY project.project_id LIMIT ?";


        PreparedStatement returnVal = dbConnection.prepareStatement(sql);

        returnVal.setInt(1, (Integer) sqlParamVals.get("pageToken"));

        return returnVal;
    }

}
