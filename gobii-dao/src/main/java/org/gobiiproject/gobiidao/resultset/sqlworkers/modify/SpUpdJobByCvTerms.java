package org.gobiiproject.gobiidao.resultset.sqlworkers.modify;

import org.gobiiproject.gobiidao.resultset.core.SpDef;

/**
 * Created by VCalaminos on 9/4/2017.
 */
public class SpUpdJobByCvTerms extends SpDef {

    public SpUpdJobByCvTerms() {

        super("{call updatejob(?,?,?,?,?,?,?)}", false);

        this.addParamDef("jobId", Integer.class).setNullable(false);
        this.addParamDef("jobName", String.class).setNullable(false);
        this.addParamDef("type", String.class).setNullable(false);
        this.addParamDef("payloadType", String.class).setNullable(false);
        this.addParamDef("status", String.class).setNullable(false);
        this.addParamDef("message", String.class).setNullable(true);
        this.addParamDef("submittedBy", Integer.class).setNullable(false);

    }

}
