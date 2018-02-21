package org.gobiiproject.gobiidao.resultset.sqlworkers.read.liststatement.paged;

import org.gobiiproject.gobiidao.resultset.core.listquery.ListSqlId;
import org.gobiiproject.gobiidao.resultset.core.listquery.ListStatementPaged;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static org.gobiiproject.gobiidao.resultset.core.listquery.ListSqlId.QUERY_ID_DATASET_ALL;


/**

 */
public class ListStatementDatasetPaged implements ListStatementPaged {


    @Override
    public String getNameColName() {
        return "ds.name";
    }

    @Override
    public String getIdColName() {
        return "ds.dataset_id";
    }

    @Override
    public String getPageNumberColName() {
        return PagerSql.getPageNumberColName();
    }

    private final String fromWhereClause = "from " +
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
            "	) ";

    @Override
    public PreparedStatement makePreparedStatementForPageFrames(Connection dbConnection,Integer pageSize ) throws SQLException {

        String selectColumns = " " + this.getIdColName()+ ", " +
                this.getNameColName();

        Map<String,Object> sqlParamVals = new HashMap<>();
        sqlParamVals.put(PagerSql.PARAM_NAME_SELECT_COLS, selectColumns);
        sqlParamVals.put(PagerSql.PARAM_NAME_NAME_COL, this.getNameColName());
        sqlParamVals.put(PagerSql.PARAM_NAME_ID_COL, this.getIdColName());
        sqlParamVals.put(PagerSql.PARAM_NAME_FROM_WHERE_CLAUSE, fromWhereClause);
        sqlParamVals.put(PagerSql.PARAM_NAME_PAGE_SIZE, pageSize);

        PagerSql pagerSql = new PagerSql(sqlParamVals);
        String pageFrameSql = pagerSql.makePageBoundarySql();
        PreparedStatement returnVal = dbConnection.prepareStatement(pageFrameSql);

        return returnVal;
    }

    @Override
    public PreparedStatement makePreparedStatementForAPage(Connection dbConnection, Integer pageSize, String pgItemNameVal, Integer pgItemIdVal) throws SQLException {


        String selectColumns = " ds.dataset_id, " +
                "	ds.name as \"datasetname\", " +
                "	ds.experiment_id, " +
                "	e.name as \"experimentname\", " +
                "	p.project_id as \"projectid\", " +
                "	p.name as \"projectname\", " +
                "	null as \"protocolid\", " +
                "	null as \"protocolname\", " +
                "	null as \"platformid\", " +
                "	null \"platformname\", " +
                "	ds.callinganalysis_id as \"callinganalysisid\", " +
                "	null as \"callinganalysisname\", " +
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
                "	null as totalmarkers, ";

        Map<String, Object> sqlParamVals = new HashMap<>();
        sqlParamVals.put(PagerSql.PARAM_NAME_SELECT_COLS, selectColumns);
        sqlParamVals.put(PagerSql.PARAM_NAME_NAME_COL, this.getNameColName());
        sqlParamVals.put(PagerSql.PARAM_NAME_ID_COL, this.getIdColName());
        sqlParamVals.put(PagerSql.PARAM_NAME_FROM_WHERE_CLAUSE, fromWhereClause);
        sqlParamVals.put(PagerSql.PARAM_NAME_PAGE_SIZE, pageSize);
        sqlParamVals.put(PagerSql.PARAM_NAME_ID_COL_VAL, pgItemIdVal);
        sqlParamVals.put(PagerSql.PARAM_NAME_NAME_COL_VAL, pgItemNameVal);

        PagerSql pagerSql = new PagerSql(sqlParamVals);
        String pageFrameSql = pagerSql.makePageBoundarySql();
        PreparedStatement returnVal = dbConnection.prepareStatement(pageFrameSql);

        return returnVal;
    }

    public ListSqlId getListSqlId() {
        return QUERY_ID_DATASET_ALL;
    }


//    @Override
//    public PreparedStatement makePreparedStatement(Connection dbConnection, Map<String, Object> jdbcParamVals, Map<String, Object> sqlParamVals) throws SQLException {
//
//
//        Integer pageSizeValue = null;
//        if (sqlParamVals.containsKey(PagerSql.PARAM_NAME_PAGE_SIZE) && NumberUtils.isNumber(sqlParamVals.get(PagerSql.PARAM_NAME_PAGE_SIZE).toString())) {
//
//            pageSizeValue = NumberUtils.toInt(sqlParamVals.get(PagerSql.PARAM_NAME_PAGE_SIZE).toString());
//
//        } else {
//            throw new GobiiDaoException("The specified page size value is not an integer: " + pageSizeValue);
//        }
//
//
//        String pageNameValue = sqlParamVals.containsKey(PagerSql.PARAM_NAME_NAME_COL) ? sqlParamVals.get(PagerSql.PARAM_NAME_NAME_COL).toString() : null;
//        String pageIdValueAsString = sqlParamVals.containsKey(PagerSql.PARAM_NAME_ID_COL) ? sqlParamVals.get(PagerSql.PARAM_NAME_ID_COL).toString() : null;
//
//        Integer pageIdValue = null;
//        if (pageIdValueAsString != null && NumberUtils.isNumber(pageIdValueAsString)) {
//
//            pageIdValue = NumberUtils.toInt(pageIdValueAsString);
//
//        } else {
//            throw new GobiiDaoException("The specified pageId value is not an integer: " + pageIdValueAsString);
//        }
//
//
//        String selectColumns = " ds.dataset_id, " +
//                "	ds.name as \"datasetname\", " +
//                "	ds.experiment_id, " +
//                "	e.name as \"experimentname\", " +
//                "	p.project_id as \"projectid\", " +
//                "	p.name as \"projectname\", " +
//                "	null as \"protocolid\", " +
//                "	null as \"protocolname\", " +
//                "	null as \"platformid\", " +
//                "	null \"platformname\", " +
//                "	ds.callinganalysis_id as \"callinganalysisid\", " +
//                "	null as \"callinganalysisname\", " +
//                "	c.contact_id as picontactid, " +
//                "	c.email as piemail, " +
//                "	ds.data_table, " +
//                "	ds.data_file, " +
//                "	ds.quality_table, " +
//                "	ds.quality_file, " +
//                "	ds.status, " +
//                "	ds.created_by, " +
//                "	ds.created_date, " +
//                "	ds.modified_by, " +
//                "	ds.modified_date, " +
//                "	ds.analyses, " +
//                "	ds.type_id as \"datatypeid\", " +
//                "	null as datatypename, " +
//                "	ds.job_id, " +
//                "	j.status \"jobstatusid\", " +
//                "	case " +
//                "		when j.status is not null then( " +
//                "			select " +
//                "				cv.term " +
//                "			from " +
//                "				cv " +
//                "			where " +
//                "				cvgroup_id =( " +
//                "					select " +
//                "						cvgroup_id " +
//                "					from " +
//                "						cvgroup " +
//                "					where " +
//                "						name = 'job_status' " +
//                "						and type = 1 " +
//                "				) " +
//                "				and cv.cv_id = j.status " +
//                "		) " +
//                "		else 'Unsubmitted' " +
//                "	end as jobstatusname, " +
//                "	j.type_id \"jobtypeid\", " +
//                "	case " +
//                "		when j.type_id is not null then( " +
//                "			select " +
//                "				cv.term " +
//                "			from " +
//                "				cv " +
//                "			where " +
//                "				cvgroup_id =( " +
//                "					select " +
//                "						cvgroup_id " +
//                "					from " +
//                "						cvgroup " +
//                "					where " +
//                "						name = 'job_type' " +
//                "						and type = 1 " +
//                "				) " +
//                "				and cv.cv_id = j.type_id " +
//                "		) " +
//                "		else 'n/a' " +
//                "	end as jobtypename, " +
//                "	j.submitted_date as jobsubmitteddate, " +
//                "	null as totalsamples, " +
//                "	null as totalmarkers, ";
//
//
//        sqlParamVals.put(PagerSql.PARAM_NAME_SELECT_COLS, selectColumns);
//        sqlParamVals.put(PagerSql.PARAM_NAME_NAME_COL, "d.name");
//        sqlParamVals.put(PagerSql.PARAM_NAME_ID_COL, "d.dataset_id");
//        sqlParamVals.put(PagerSql.PARAM_NAME_FROM_WHERE_CLAUSE, " from dataset d");
//
//
//        /**********************************************************************
//         * This is the query you use for populating a list -- it needs the column names to
//         * satisfy populating the DTOs, but does not actually use the extensive joins that the
//         * byId version uses */
//        String sql = "select " +
//                "	ds.dataset_id, " +
//                "	ds.name as \"datasetname\", " +
//                "	ds.experiment_id, " +
//                "	e.name as \"experimentname\", " +
//                "	p.project_id as \"projectid\", " +
//                "	p.name as \"projectname\", " +
//                "	null as \"protocolid\", " +
//                "	null as \"protocolname\", " +
//                "	null as \"platformid\", " +
//                "	null \"platformname\", " +
//                "	ds.callinganalysis_id as \"callinganalysisid\", " +
//                "	null as \"callinganalysisname\", " +
//                "	c.contact_id as picontactid, " +
//                "	c.email as piemail, " +
//                "	ds.data_table, " +
//                "	ds.data_file, " +
//                "	ds.quality_table, " +
//                "	ds.quality_file, " +
//                "	ds.status, " +
//                "	ds.created_by, " +
//                "	ds.created_date, " +
//                "	ds.modified_by, " +
//                "	ds.modified_date, " +
//                "	ds.analyses, " +
//                "	ds.type_id as \"datatypeid\", " +
//                "	null as datatypename, " +
//                "	ds.job_id, " +
//                "	j.status \"jobstatusid\", " +
//                "	case " +
//                "		when j.status is not null then( " +
//                "			select " +
//                "				cv.term " +
//                "			from " +
//                "				cv " +
//                "			where " +
//                "				cvgroup_id =( " +
//                "					select " +
//                "						cvgroup_id " +
//                "					from " +
//                "						cvgroup " +
//                "					where " +
//                "						name = 'job_status' " +
//                "						and type = 1 " +
//                "				) " +
//                "				and cv.cv_id = j.status " +
//                "		) " +
//                "		else 'Unsubmitted' " +
//                "	end as jobstatusname, " +
//                "	j.type_id \"jobtypeid\", " +
//                "	case " +
//                "		when j.type_id is not null then( " +
//                "			select " +
//                "				cv.term " +
//                "			from " +
//                "				cv " +
//                "			where " +
//                "				cvgroup_id =( " +
//                "					select " +
//                "						cvgroup_id " +
//                "					from " +
//                "						cvgroup " +
//                "					where " +
//                "						name = 'job_type' " +
//                "						and type = 1 " +
//                "				) " +
//                "				and cv.cv_id = j.type_id " +
//                "		) " +
//                "		else 'n/a' " +
//                "	end as jobtypename, " +
//                "	j.submitted_date as jobsubmitteddate, " +
//                "	null as totalsamples, " +
//                "	null as totalmarkers " +
//                "from " +
//                "	dataset ds left outer join job j on " +
//                "	( " +
//                "		ds.job_id = j.job_id " +
//                "	)  join experiment e on " +
//                "	( " +
//                "		ds.experiment_id = e.experiment_id " +
//                "	) join project p on " +
//                "	( " +
//                "		e.project_id = p.project_id " +
//                "	) join contact c on " +
//                "	( " +
//                "		p.pi_contact = c.contact_id " +
//                "	) " +
//                "order by " +
//                "	j.submitted_date desc, " +
//                "	lower( ds.name ) asc	";
//
//        PreparedStatement returnVal = dbConnection.prepareStatement(sql);
//
//        return returnVal;
//    }
}
