package org.gobiiproject.gobiidao.resultset.sqlworkers.modify;

import org.gobiiproject.gobiidao.resultset.core.SpDef;

/**
 * Created by Angel on 5/5/2016.
 */
public class SpUpdCv extends SpDef {


    public SpUpdCv() {

        super("{call updatecv(?,?,?,?,?,?,?,?)}",false);

        this.addParamDef("cvId",Integer.class).setNullable(false);
        this.addParamDef("groupId", Integer.class).setNullable(false);
        this.addParamDef("term", String.class).setNullable(false);
        this.addParamDef("definition", String.class).setNullable(false);
        this.addParamDef("rank", Integer.class).setNullable(false);
        this.addParamDef("abbreviation", String.class).setNullable(true);
        this.addParamDef("xrefId", Integer.class).setNullable(true);
        this.addParamDef("status", Integer.class).setNullable(false);
    } // ctor
}
