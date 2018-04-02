package org.gobiiproject.gobiidao.resultset.sqlworkers.read.sp;

import org.hibernate.jdbc.Work;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by Angel on 4/19/2016.
 */
public class SpGetDatasetDetailsByDataSetId implements Work {

    private Map<String, Object> parameters = null;

    public SpGetDatasetDetailsByDataSetId(Map<String, Object> parameters) {
        this.parameters = parameters;
    }


    private ResultSet resultSet = null;

    public ResultSet getResultSet() {
        return resultSet;
    }

    @Override
    public void execute(Connection dbConnection) throws SQLException {

        // BuildMyString.com generated code. Please enjoy your string responsibly.
        String sql = "select " +
                "	js.dataset_id, " +
                "	js.dataset_name as \"datasetname\", " +
                "	js.experiment_id, " +
                "	js.experiment_name as \"experimentname\", " +
                "	js.project_id as \"projectid\", " +
                "	js.project_name as \"projectname\", " +
                "	js.protocol_id as \"protocolid\", " +
                "	js.protocol_name as \"protocolname\", " +
                "	js.platform_id as \"platformid\", " +
                "	js.platform_name as \"platformname\", " +
                "	js.calling_analysis_id as \"callinganalysisid\", " +
                "	js.calling_analysis_name as \"callinganalysisname\", " +
                "	js.pi_contact_id as picontactid, " +
                "	js.pi_email as piemail, " +
                "	js.data_table, " +
                "	js.data_file, " +
                "	js.quality_table, " +
                "	js.quality_file, " +
                "	js.status, " +
                "	js.created_by, " +
                "	js.created_date, " +
                "	js.modified_by, " +
                "	js.modified_date, " +
                "	js.analyses, " +
                "	js.dataset_type_id as \"datatypeid\", " +
                "	js.dataset_type_name as datatypename, " +
                "	js.job_id, " +
                "	js.job_status_id \"jobstatusid\", " +
                "	js.job_status_name jobstatusname, " +
                "	js.job_type_id \"jobtypeid\", " +
                "	js.job_type_name as jobtypename, " +
                "	js.job_submitted_date as jobsubmitteddate, " +
                "	js.total_samples as totalsamples, " +
                "	js.total_markers as totalmarkers, " +
                "   c.lastname as loader_last_name, " +
                "   c.firstname as loader_first_name " +
                " from v_jobs_summary js " +
                " left outer join contact c on (js.modified_by=c.contact_id) " +
                " where dataset_id=? ";

        PreparedStatement preparedStatement = dbConnection.prepareCall(sql);
        preparedStatement.setInt(1, (Integer) parameters.get("dataSetId"));
        resultSet = preparedStatement.executeQuery();


    } // execute()
}
