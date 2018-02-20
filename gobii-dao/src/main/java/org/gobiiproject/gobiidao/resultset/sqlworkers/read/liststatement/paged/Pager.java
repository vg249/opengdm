package org.gobiiproject.gobiidao.resultset.sqlworkers.read.liststatement.paged;

import org.gobiiproject.gobiidao.resultset.core.listquery.ParameterizedSql;

import java.util.HashMap;
import java.util.Map;

public class Pager {

    Map<String, Object> sqlParamVals;
    public Pager(Map<String, Object> sqlParamVals) {
        this.sqlParamVals = sqlParamVals;
    } //ctor

    public static final String PARAM_NAME_NAME_COL = "nameCol";
    public static final String PARAM_NAME_ID_COL = "idCol";
    public static final String PARAM_NAME_SELECT_COLS = "selectClause";
    public static final String PARAM_NAME_FROM_WHERE_CLAUSE = "fromWhereClause";
    public static final String PARAM_NAME_PAGE_SIZE = "pageSize";


    // the SELECT_CLAUSE parameter value must include the columns identified as the NAME_COL and ID_COL
    private final String PARAM_NAME_NAME_COL_DELIMITED = ParameterizedSql.makeDelimitedParamName(PARAM_NAME_NAME_COL);
    private final String PARAM_NAME_ID_COL_DELIMITED = ParameterizedSql.makeDelimitedParamName(PARAM_NAME_ID_COL);
    private final String PARAM_NAME_SELECT_COLS_DELIMITED = ParameterizedSql.makeDelimitedParamName(PARAM_NAME_SELECT_COLS);
    private final String PARAM_NAME_FROM_WHERE_CLAUSE_DELIMITED = ParameterizedSql.makeDelimitedParamName(PARAM_NAME_FROM_WHERE_CLAUSE);
    private final String PARAM_NAME_PAGE_SIZE_DELIMITED = ParameterizedSql.makeDelimitedParamName(PARAM_NAME_PAGE_SIZE);


    public String makePageBoundarySql() {

        String intialSqlTemplate = "select " +
                                    " x.pg_item_name, " +
                                    " x.pg_item_id, " +
                                    " row_number() over( " +
                                    " order by " +
                                        " x.pg_item_name, " +
                                        " x.pg_item_id " +
                                    " ) + 1 page_number " +
                                " from " +
                                    " ( " +
                                        " select " +
                                            PARAM_NAME_NAME_COL_DELIMITED + " as pg_item_name, " +
                                            PARAM_NAME_ID_COL_DELIMITED + " as pg_item_id, " +
                                            " case " +
                                                " row_number() over( " +
                                                " order by " +
                                                    " 1, -- can't refer to column alias in order by " +
                                                    " 2 " +
                                                " ) % " + PARAM_NAME_PAGE_SIZE_DELIMITED +
                                                " when 0 then 1 " +
                                                " else 0 " +
                                            " end page_boundary " +
                                            PARAM_NAME_FROM_WHERE_CLAUSE_DELIMITED +
                                        " order by " +
                                            " 1, " +
                                            " 2 " +
                                    ") x " +
                                " where " +
                                    " x.page_boundary = 1";

        ParameterizedSql parameterizedSql =
                new ParameterizedSql(intialSqlTemplate,
                        new HashMap<String, String>() {
                            {
                                put(PARAM_NAME_NAME_COL_DELIMITED, null);
                                put(PARAM_NAME_ID_COL_DELIMITED, null);
                                put(PARAM_NAME_SELECT_COLS_DELIMITED, null);
                                put(PARAM_NAME_PAGE_SIZE_DELIMITED, null);
                                put(PARAM_NAME_FROM_WHERE_CLAUSE_DELIMITED, null);

                            }
                        });

        String sql = parameterizedSql
                .setParamValue(PARAM_NAME_NAME_COL_DELIMITED, this.sqlParamVals.get(PARAM_NAME_NAME_COL).toString())
                .setParamValue(PARAM_NAME_ID_COL_DELIMITED, this.sqlParamVals.get(PARAM_NAME_NAME_COL).toString())
                .setParamValue(PARAM_NAME_SELECT_COLS_DELIMITED, this.sqlParamVals.get(PARAM_NAME_SELECT_COLS).toString())
                .setParamValue(PARAM_NAME_PAGE_SIZE_DELIMITED, this.sqlParamVals.get(PARAM_NAME_FROM_WHERE_CLAUSE).toString())
                .setParamValue(PARAM_NAME_FROM_WHERE_CLAUSE_DELIMITED, this.sqlParamVals.get(PARAM_NAME_PAGE_SIZE).toString())
                .getSql();

        return sql;
    }

    public String makeSinglePageSql(Integer pageNameVal, String pageIdVal ) {


        String pageSqltemplate = "select " +
                    " name, " +
                    " dataset_id " +
                " from " +
                    " dataset " +
                " where " +
                    " ( " +
                        " name, " +
                        " dataset_id " +
                    " ) >( " +
                        " 'DebTestDS4-full-ssrAllele', " +
                        " 62 " +
                    " ) " +
                " order by " +
                    " name, " +
                    " dataset_id limit 4 ";


        String sql = null;

        return sql;
    }


} // class: Pager
