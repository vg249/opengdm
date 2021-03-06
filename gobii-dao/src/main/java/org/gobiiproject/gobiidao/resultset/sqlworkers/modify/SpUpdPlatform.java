package org.gobiiproject.gobiidao.resultset.sqlworkers.modify;

import org.gobiiproject.gobiidao.resultset.core.SpDef;

import java.util.Date;

/**
 * Created by Phil on 4/7/2016.
 */
public class SpUpdPlatform extends SpDef {

    public SpUpdPlatform() {

        super("{call updateplatform(?,?,?,?,?,?,?,?,?,?)}",false);

        this.addParamDef("platformId",Integer.class).setNullable(false);
        this.addParamDef("platformName", String.class).setNullable(false);
        this.addParamDef("platformCode", String.class).setNullable(false);
        this.addParamDef("platformDescription", String.class).setNullable(true);
        this.addParamDef("createdBy", Integer.class).setNullable(false);
        this.addParamDef("createdDate", java.util.Date.class).setNullable(false);
        this.addParamDef("modifiedBy", Integer.class).setNullable(true);
        this.addParamDef("modifiedDate", java.util.Date.class).setNullable(true);
        this.addParamDef("status", Integer.class).setNullable(false);
        this.addParamDef("typeId", Integer.class).setNullable(false);

    } // ctor
}
