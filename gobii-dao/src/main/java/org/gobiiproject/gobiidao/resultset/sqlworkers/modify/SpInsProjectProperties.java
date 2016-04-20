package org.gobiiproject.gobiidao.resultset.sqlworkers.modify;

import org.gobiiproject.gobiidao.resultset.core.SpDef;

import java.sql.Date;

/**
 * Created by Phil on 4/7/2016.
 */
public class SpInsProjectProperties extends SpDef {

    public SpInsProjectProperties() {

        super("{call upsertprojectpropertybyname(?,?,?)}");

        this.addParamDef("projectId", Integer.class)
                .setNullable(false);

        this.addParamDef("propertyName",String.class)
                .setNullable(false);

        this.addParamDef("propertyValue",String.class)
                .setNullable(true);

    } // ctor
}
