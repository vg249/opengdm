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
                "	dataset_id, " +
                "	dataset_name as \"datasetname\", " +
                "	experiment_id, " +
                "	experiment_name as \"experimentname\", " +
                "	project_id as \"projectid\", " +
                "	project_name as \"projectname\", " +
                "	protocol_id as \"protocolid\", " +
                "	protocol_name as \"protocolname\", " +
                "	platform_id as \"platformid\", " +
                "	platform_name as \"platformname\", " +
                "	calling_analysis_id as \"callinganalysisid\", " +
                "	calling_analysis_name as \"callinganalysisname\", " +
                "	pi_contact_id as picontactid, " +
                "	pi_email as piemail," +
                "   c.lastname as pilastname," +
                "   c.firstname as pifirstname," +
                "	data_table, " +
                "	data_file, " +
                "	quality_table, " +
                "	quality_file, " +
                "	status, " +
                "	v.created_by, " +
                "	v.created_date, " +
                "	v.modified_by, " +
                "	v.modified_date, " +
                "	analyses, " +
                "	dataset_type_id as \"datatypeid\", " +
                "	dataset_type_name as datatypename, " +
                "	job_id, " +
                "	job_status_id \"jobstatusid\", " +
                "	job_status_name jobstatusname, " +
                "	job_type_id \"jobtypeid\", " +
                "	job_type_name as jobtypename, " +
                "	job_submitted_date as jobsubmitteddate, " +
                "	total_samples as totalsamples, " +
                "	total_markers as totalmarkers " +
                " from v_jobs_summary v" +
                " inner join contact c on c.contact_id = v.pi_contact_id" +
                " where dataset_id=? ";


        PreparedStatement preparedStatement = dbConnection.prepareCall(sql);
        preparedStatement.setInt(1, (Integer) parameters.get("dataSetId"));
        resultSet = preparedStatement.executeQuery();


    } // execute()
}
