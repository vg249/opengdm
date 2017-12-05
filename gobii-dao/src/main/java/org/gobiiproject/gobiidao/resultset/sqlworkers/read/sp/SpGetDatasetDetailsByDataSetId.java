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
                "	ds.dataset_id, " +
                "	ds.name as \"datasetname\", " +
                "	e.experiment_id, " +
                "	e.name as \"experimentname\", " +
                "	p.project_id as \"projectid\", " +
                "	p.name as \"projectname\", " +
                "	pr.protocol_id as \"protocolid\", " +
                "	pr.name as \"protocolname\", " +
                "	pl.platform_id as \"platformid\", " +
                "	pl.name as \"platformname\", " +
                "	ds.callinganalysis_id as \"callinganalysisid\", " +
                "	a.name as \"callinganalysisname\", " +
                "	c.contact_id as picontactid, " +
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
                "	( " +
                "		select " +
                "			cv.term " +
                "		from " +
                "			cv " +
                "		where " +
                "			cv.cvgroup_id =( " +
                "				select " +
                "					cvgroup_id " +
                "				from " +
                "					cvgroup " +
                "				where " +
                "					name = 'dataset_type' " +
                "					and cvgroup.type = 1 " +
                "			) " +
                "			and cv.cv_id = ds.type_id " +
                "	) as datatypename, " +
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
                "	( " +
                "		select " +
                "			count( * ) " +
                "		from " +
                "			dataset ds join dnarun d on " +
                "			( " +
                "				ds.experiment_id = d.experiment_id " +
                "			) join dnasample s on " +
                "			( " +
                "				d.dnasample_id = s.dnasample_id " +
                "			) " +
                "		where " +
                "			ds.dataset_id = ? " +
                "	) as totalsamples, " +
                "	( " +
                "		select " +
                "			count( * ) " +
                "		from " +
                "			dataset ds join experiment e on " +
                "			( " +
                "				ds.experiment_id = e.experiment_id " +
                "			) join vendor_protocol vp on " +
                "			( " +
                "				e.vendor_protocol_id = vp.vendor_protocol_id " +
                "			) join protocol p on " +
                "			( " +
                "				vp.protocol_id = p.protocol_id " +
                "			) join platform pf on " +
                "			( " +
                "				p.platform_id = pf.platform_id " +
                "			) join marker m on " +
                "			( " +
                "				m.platform_id = pf.platform_id " +
                "			) " +
                "		where " +
                "			ds.dataset_id = ? " +
                "	) as totalmarkers " +
                "from " +
                "	dataset ds join experiment e on " +
                "	( " +
                "		ds.experiment_id = e.experiment_id " +
                "	) join project p on " +
                "	( " +
                "		e.project_id = p.project_id " +
                "	) join contact c on " +
                "	( " +
                "		p.pi_contact = c.contact_id " +
                "	) left outer join job j on " +
                "	( " +
                "		ds.job_id = j.job_id " +
                "	) left outer join vendor_protocol vp on " +
                "	( " +
                "		e.vendor_protocol_id = vp.vendor_protocol_id " +
                "	) left outer join protocol pr on " +
                "	( " +
                "		vp.protocol_id = pr.protocol_id " +
                "	) left outer join platform pl on " +
                "	( " +
                "		pr.platform_id = pl.platform_id " +
                "	) join analysis a on " +
                "	( " +
                "		ds.callinganalysis_id = a.analysis_id " +
                "	) " +
                "where ds.dataset_id=? " +
                "order by " +
                "	j.submitted_date desc, " +
                "	lower( ds.name ) asc";




        PreparedStatement preparedStatement = dbConnection.prepareCall(sql);
        preparedStatement.setInt(1, (Integer) parameters.get("dataSetId"));
        preparedStatement.setInt(2, (Integer) parameters.get("dataSetId"));
        preparedStatement.setInt(3, (Integer) parameters.get("dataSetId"));
        resultSet = preparedStatement.executeQuery();


    } // execute()
}
