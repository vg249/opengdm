package org.gobiiproject.gobiidao.resultset.core.listquery;

/**
 * Created by Phil on 10/25/2016.
 */
public enum ListSqlId {
    QUERY_ID_DATASET_ALL("select * from dataset order by lower(name)"),
    QUERY_ID_CONTACT_ALL("select * from contact order by lower(lastname),lower(firstname)"),
    QUERY_ID_ORGANIZATION_ALL("select * from organization order by lower(name)"),
    QUERY_ID_PLATFORM_ALL("select * from platform order by lower(name)"),
    QUERY_ID_PROJECT_ALL("select * from project order by lower(name)"),
    QUERY_ID_EXPERIMENT("select p.name \"platform_name\",e.*\n" +
            "from experiment e\n" +
            "join platform p on (e.platform_id=p.platform_id)\n" +
            "order by lower(e.name)");

    private String sql;
    ListSqlId(String sql) {
        this.sql = sql;
    }

    public String getSql() {
        return sql;
    }
}
