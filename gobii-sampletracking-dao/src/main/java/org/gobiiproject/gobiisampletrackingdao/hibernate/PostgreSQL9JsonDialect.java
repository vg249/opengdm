package org.gobiiproject.gobiisampletrackingdao.hibernate;

import com.vladmihalcea.hibernate.type.json.JsonNodeBinaryType;
import org.hibernate.dialect.PostgreSQL9Dialect;

import java.sql.Types;

public class PostgreSQL9JsonDialect extends PostgreSQL9Dialect {

    public PostgreSQL9JsonDialect() {
        super();
        this.registerHibernateType(
                Types.OTHER, JsonNodeBinaryType.class.getName()
        );
    }

}
