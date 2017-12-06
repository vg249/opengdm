package org.gobiiproject.gobiidao.resultset.sqlworkers.read.liststatement;

import org.gobiiproject.gobiidao.resultset.core.listquery.ListSqlId;
import org.gobiiproject.gobiidao.resultset.core.listquery.ListStatement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

import static org.gobiiproject.gobiidao.resultset.core.listquery.ListSqlId.QUERY_ID_DATASET_ALL;

/**

 */
public class ListStatementDatasetAll implements ListStatement {

    @Override
    public ListSqlId getListSqlId() {
        return QUERY_ID_DATASET_ALL;
    }

    @Override
    public PreparedStatement makePreparedStatement(Connection dbConnection, Map<String, Object> jdbcParamVals, Map<String, Object> sqlParamVals) throws SQLException {

        /**********************************************************************
        * This is the query you use for populating a list -- it needs the column names to
        * satisfy populating the DTOs, but does not actually use the extensive joins that the
        * byId version uses */
        String sql = "select " +
                "	ds.dataset_id, " +
                "	ds.name as \"datasetname\", " +
                "	ds.experiment_id, " +
                "	e.name as \"experimentname\", " +
                "	null as \"projectid\", " +
                "	null as \"projectname\", " +
                "	null as \"protocolid\", " +
                "	null as \"protocolname\", " +
                "	null as \"platformid\", " +
                "	null \"platformname\", " +
                "	ds.callinganalysis_id as \"callinganalysisid\", " +
                "	null as \"callinganalysisname\", " +
                "	null as picontactid, " +
                "	c.email as piemail, " +
                "	ds.data_table, " +
                "	ds.data_file, " +
                "	ds.quality_table, " +
                "	ds.quality_file, " +
                "	ds.status, " +
                "	ds.created_by, " +
                "	ds.created_date, " +
                "	ds.modified_by, " +
                "	ds.modified_date, " +
                "	ds.analyses, " +
                "	ds.type_id as \"datatypeid\", " +
                "	null as datatypename, " +
                "	ds.job_id, " +
                "	j.status \"jobstatusid\", " +
                "	case " +
                "		when j.status is not null then( " +
                "			select " +
                "				cv.term " +
                "			from " +
                "				cv " +
                "			where " +
                "				cvgroup_id =( " +
                "					select " +
                "						cvgroup_id " +
                "					from " +
                "						cvgroup " +
                "					where " +
                "						name = 'job_status' " +
                "						and type = 1 " +
                "				) " +
                "				and cv.cv_id = j.status " +
                "		) " +
                "		else 'Unsubmitted' " +
                "	end as jobstatusname, " +
                "	j.type_id \"jobtypeid\", " +
                "	case " +
                "		when j.type_id is not null then( " +
                "			select " +
                "				cv.term " +
                "			from " +
                "				cv " +
                "			where " +
                "				cvgroup_id =( " +
                "					select " +
                "						cvgroup_id " +
                "					from " +
                "						cvgroup " +
                "					where " +
                "						name = 'job_type' " +
                "						and type = 1 " +
                "				) " +
                "				and cv.cv_id = j.type_id " +
                "		) " +
                "		else 'n/a' " +
                "	end as jobtypename, " +
                "	j.submitted_date as jobsubmitteddate, " +
                "	null as totalsamples, " +
                "	null as totalmarkers " +
                "from " +
                "	dataset ds left outer join job j on " +
                "	( " +
                "		ds.job_id = j.job_id " +
                "	)  join experiment e on " +
                "	( " +
                "		ds.experiment_id = e.experiment_id " +
                "	) join project p on " +
                "	( " +
                "		e.project_id = p.project_id " +
                "	) join contact c on " +
                "	( " +
                "		p.pi_contact = c.contact_id " +
                "	) " +
                "order by " +
                "	j.submitted_date desc, " +
                "	lower( ds.name ) asc	";

        PreparedStatement returnVal = dbConnection.prepareStatement(sql);

        return returnVal;
    }
}
