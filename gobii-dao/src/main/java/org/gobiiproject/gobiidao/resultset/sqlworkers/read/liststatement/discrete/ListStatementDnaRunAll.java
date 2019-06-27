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

/**
 * Created by VCalaminos on 6/26/2019.
 */
public class ListStatementDnaRunAll implements ListStatement {

    @Override
    public ListSqlId getListSqlId() {
        return QUERY_ID_DNARUN_ALL;
    }

    @Override
    public PreparedStatement makePreparedStatement(Connection dbConnection,
                                                   Map<String, Object> jdbcParamVals,
                                                   Map<String, Object> sqlParamVals)
            throws SQLException, GobiiException {

        String pageCondition = "";
        String pageSizeCondition = "";
        Integer pageSize = 0;

        if(sqlParamVals != null) {
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
                    pageCondition = " WHERE dnarun_id > ?";
                } else {
                    pageCondition = "";
                }
            } else if(sqlParamVals.containsKey("pageToken")) {
                throw new GobiiException(GobiiStatusLevel.ERROR, GobiiValidationStatusType.BAD_REQUEST,
                        "Invalid Page Token");
            }
        }

        String sql = "with dnarun as (\n" +
                "SELECT\n" +
                "dr.dnarun_id,\n" +
                "dr.experiment_id,\n" +
                "dr.dnasample_id,\n" +
                "array_agg(datasetids) as dataset_ids,\n" +
                "dr.name,\n" +
                "dr.code\n" +
                "FROM\n" +
                "(\n" +
                "   SELECT\n" +
                        "dr.dnarun_id,\n" +
                        "dr.experiment_id,\n" +
                        "dr.dnasample_id,\n" +
                        "dr.name,\n" +
                        "dr.code,\n" +
                        "jsonb_object_keys(dr.dataset_dnarun_idx)::integer as datasetids\n" +
                    "FROM\n" +
                        "dnarun dr\n" +
                    pageCondition +
                ") as dr\n" +
                "GROUP BY dr.dnarun_id, dr.experiment_id, dr.dnasample_id, dr.name, dr.code)\n" +
                "SELECT\n" +
                "   dnarun.*,\n" +
                "   s.name as sample_name,\n" +
                "   g.germplasm_id,\n" +
                "   g.name as germplasm_name, \n" +
                "   g.external_code as germplasm_external_code\n" +
                "FROM\n" +
                "   dnarun, dnasample s, germplasm g\n" +
                "WHERE\n" +
                "   dnarun.dnasample_id = s.dnasample_id\n" +
                "   and g.germplasm_id = s.germplasm_id\n" +
                pageSizeCondition;

        PreparedStatement returnVal = dbConnection.prepareStatement(sql);

        if(!pageCondition.isEmpty() && !pageSizeCondition.isEmpty()) {
            returnVal.setInt(1, (Integer) sqlParamVals.get("pageToken"));
            returnVal.setInt(2, pageSize);
        }
        else if(!pageCondition.isEmpty()) {
            returnVal.setInt(1, (Integer) sqlParamVals.get("pageToken"));
        }
        else if(!pageSizeCondition.isEmpty()) {
            returnVal.setInt(1, pageSize);
        }

        return returnVal;

    }

}
