package org.gobiiproject.gobiidao.resultset.sqlworkers.read.liststatement.discrete;

import org.gobiiproject.gobiidao.resultset.core.listquery.ListSqlId;
import org.gobiiproject.gobiidao.resultset.core.listquery.ListStatement;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

import static org.gobiiproject.gobiidao.resultset.core.listquery.ListSqlId.QUERY_ID_DNARUN_ALL;
import static org.gobiiproject.gobiidao.resultset.core.listquery.ListSqlId.QUERY_ID_GENOTYPE_CALLS_BY_DNARUN_ID;

public class ListStatementGenotypeCallsByDnarunId implements ListStatement {

    @Override
    public ListSqlId getListSqlId() {
        return QUERY_ID_GENOTYPE_CALLS_BY_DNARUN_ID;
    }

    @Override
    public PreparedStatement makePreparedStatement(Connection dbConnection,
                                                   Map<String, Object> jdbcParamVals,
                                                   Map<String, Object> sqlParamVals)
            throws SQLException, GobiiException {

        String pageCondition = "";
        String pageSizeCondition = "";

        Integer pageSize = 0;
        Integer dnarunId;

        if(sqlParamVals != null) {

            if((!sqlParamVals.containsKey("dnarunId")) || sqlParamVals.get("dnarunId") == null) {
                throw new GobiiException(
                        GobiiStatusLevel.ERROR,
                        GobiiValidationStatusType.BAD_REQUEST,
                        "Required query parameter Dnarun Id missing.");
            }

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
                            "Invalid Page Size");
                }
            } else if(sqlParamVals.containsKey("pageSize")) {
                throw new GobiiException(
                        GobiiStatusLevel.ERROR,
                        GobiiValidationStatusType.BAD_REQUEST,
                        "Invalid Page Size");
            }

            if (sqlParamVals.containsKey("pageToken")
                    && sqlParamVals.get("pageToken") instanceof Integer) {
                if ((Integer) sqlParamVals.getOrDefault("pageToken", 0) > 0) {
                    pageCondition = " WHERE marker_id > ?";
                } else {
                    pageCondition = "";
                }
            } else if(sqlParamVals.containsKey("pageToken")) {
                throw new GobiiException(GobiiStatusLevel.ERROR, GobiiValidationStatusType.BAD_REQUEST,
                        "Invalid Page Token");
            }
        }

        String sql = "SELECT dnarun_dset.dset_id AS dataset_id, marker.marker_id, " +
                "dnarun_dset.dnarun_id, marker.name AS marker_name, " +
                "dnarun_dset.name AS dnarun_name, " +
                "marker.dataset_marker_idx->>dnarun_dset.dset_id::text as hdf5_marker_idx, " +
                "dnarun_dset.hdf5_dnarun_idx FROM (" +
                "SELECT dnarun.dnarun_id, dnarun.name, dnarun_dset_id.dset_id dset_id, " +
                "dnarun.dataset_dnarun_idx->>dset_id::text AS hdf5_dnarun_idx " +
                "FROM dnarun INNER JOIN (" +
                "SELECT dnarun_id, jsonb_object_keys(dataset_dnarun_idx)::int as dset_id " +
                "FROM dnarun WHERE dnarun_id = ?) dnarun_dset_id USING(dnarun_id)) dnarun_dset " +
                "INNER JOIN marker ON(marker.dataset_marker_idx ?? dnarun_dset.dset_id::text) " +
                pageCondition + " ORDER BY marker.marker_id "+pageSizeCondition;

        PreparedStatement returnVal = dbConnection.prepareStatement(sql);

        returnVal.setInt(1, (Integer) sqlParamVals.get("dnarunId"));

        if(!pageCondition.isEmpty() && !pageSizeCondition.isEmpty()) {
            returnVal.setInt(2, (Integer) sqlParamVals.get("pageToken"));
            returnVal.setInt(3, pageSize);
        }
        else if(!pageCondition.isEmpty()) {
            returnVal.setInt(2, (Integer) sqlParamVals.get("pageToken"));
        }
        else if(!pageSizeCondition.isEmpty()) {
            returnVal.setInt(2, pageSize);
        }

        return returnVal;

    }

}
