package org.gobiiproject.gobiimodel.entity.pgsql;

import java.sql.Types;

public class GobiiPostgresSQLDialect extends org.hibernate.dialect.PostgreSQL9Dialect {
    public GobiiPostgresSQLDialect() {
        this.registerColumnType(Types.JAVA_OBJECT, "jsonb");
    }
}