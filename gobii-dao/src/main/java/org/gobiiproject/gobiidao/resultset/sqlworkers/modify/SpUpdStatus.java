package org.gobiiproject.gobiidao.resultset.sqlworkers.modify;

import org.gobiiproject.gobiidao.resultset.core.SpDef;

/**
 * Created by VCalaminos on 9/4/2017.
 */
public class SpUpdStatus extends SpDef {

    public SpUpdStatus() {

        super("{call updatejob(?,?,?,?,?,?)}", false);

        this.addParamDef("jobId", Integer.class).setNullable(false);
        this.addParamDef("typeId", Integer.class).setNullable(false);
        this.addParamDef("payloadTypeId", Integer.class).setNullable(false);
        this.addParamDef("status", Integer.class).setNullable(false);
        this.addParamDef("message", String.class).setNullable(true);
        this.addParamDef("submittedBy", Integer.class).setNullable(false);

    }

}
