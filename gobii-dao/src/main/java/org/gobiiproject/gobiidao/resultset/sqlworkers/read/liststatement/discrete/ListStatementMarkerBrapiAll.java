package org.gobiiproject.gobiidao.resultset.sqlworkers.read.liststatement.discrete;

import org.gobiiproject.gobiidao.resultset.core.listquery.ListSqlId;
import org.gobiiproject.gobiidao.resultset.core.listquery.ListStatement;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by VCalaminos on 7/3/2019.
 */
public class ListStatementMarkerBrapiAll implements ListStatement {

    @Override
    public ListSqlId getListSqlId() { return ListSqlId.QUERY_ID_MARKER_ALL_BRAPI; }

    @Override
    public PreparedStatement makePreparedStatement(Connection dbConnection,
                                                   Map<String, Object> jdbcParamVals,
                                                   Map<String, Object> sqlParamVals)
        throws SQLException, GobiiException {

        String pageCondition = "";
        String pageSizeCondition = "";
        String filterCondition = "";
        Integer pageSize = 0;
        HashMap<String, Integer> filterConditionIndexArr = new HashMap<>();
        Integer parameterIndex = 1;

        if (sqlParamVals != null) {
            if (sqlParamVals.containsKey("pageSize")
                    && sqlParamVals.get("pageSize") instanceof Integer) {
                pageSize = (Integer) sqlParamVals.getOrDefault("pageSize", 0);
                if (pageSize > 0) {
                    pageSizeCondition = "LIMIT ?";
                }
                else {
                    throw new GobiiException(
                            GobiiStatusLevel.ERROR,
                            GobiiValidationStatusType.BAD_REQUEST,
                            "Invalid Page Size"
                    );
                }
            } else if (sqlParamVals.containsKey("pageSize")) {
                throw new GobiiException(
                        GobiiStatusLevel.ERROR,
                        GobiiValidationStatusType.BAD_REQUEST,
                        "Invalid Page Size"
                );
            }

            if (sqlParamVals.containsKey("pageToken")
                    && sqlParamVals.get("pageToken") instanceof Integer) {
                if ((Integer) sqlParamVals.getOrDefault("pageToken", 0) > 0) {
                    pageCondition = "WHERE mr.marker_id > ?\n";
                } else {
                    pageCondition = "";
                }
            } else if (sqlParamVals.containsKey("pageToken")) {
                throw new GobiiException(GobiiStatusLevel.ERROR, GobiiValidationStatusType.BAD_REQUEST,
                        "Invalid Page Token");
            }

            if (!pageCondition.isEmpty()) {
                parameterIndex = 2;
            }

            if (sqlParamVals.containsKey("variantDbId")) {

                if (pageCondition.isEmpty()) {
                    filterCondition += "WHERE \n";
                } else {
                    filterCondition += "AND ";
                }

                filterCondition += " mr.marker_id = ?\n";
                filterConditionIndexArr.put("variantDbId", parameterIndex);
                parameterIndex++;
            }

            if (sqlParamVals.containsKey("variantSetDbId")) {

                if (pageCondition.isEmpty() && filterCondition.isEmpty()) {
                    filterCondition += "WHERE \n";
                } else {
                    filterCondition += "AND ";
                }

                filterCondition += " jsonb_exists(mr.dataset_marker_idx,?::text)\n";
                filterConditionIndexArr.put("variantSetDbId", parameterIndex);
                parameterIndex++;
            }
        }

        String sql = "SELECT  \n" +
                    "mr.marker_id, \n" +
                    "mr.platform_id, \n" +
                    "mr.variant_id, \n" +
                    "mr.name, \n" +
                    "mr.code, \n" +
                    "mr.reference_id, \n" +
                    "mr.dataset_marker_idx  ,\n" +
                    "r.name as reference_name \n" +
                "FROM  \n" +
                    "marker mr\n" +
                "LEFT OUTER JOIN reference r \n" +
                "USING(reference_id)\n" +
                pageCondition +
                filterCondition +
                "order by mr.marker_id\n" +
                pageSizeCondition;

        PreparedStatement returnVal = dbConnection.prepareStatement(sql);

        if (!pageCondition.isEmpty()) {
            returnVal.setInt(1, (Integer) sqlParamVals.get("pageToken"));
        }

        for (Map.Entry<String, Integer> filter: filterConditionIndexArr.entrySet()) {
            returnVal.setInt(filter.getValue(), (Integer) sqlParamVals.get(filter.getKey()));
        }

        if (!pageSizeCondition.isEmpty()) {
            returnVal.setInt(parameterIndex, pageSize);
        }

        return returnVal;
    }

}
