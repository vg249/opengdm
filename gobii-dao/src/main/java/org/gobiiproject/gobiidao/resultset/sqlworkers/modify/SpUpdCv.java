package org.gobiiproject.gobiidao.resultset.sqlworkers.modify;

import org.gobiiproject.gobiidao.resultset.core.SpDef;

/**
 * Created by Angel on 5/5/2016.
 */
public class SpUpdCv extends SpDef {

    public SpUpdCv() {

        super("{call updatecv(?,?,?,?,?)}",false);

        this.addParamDef("referenceId",Integer.class).setNullable(false);
        this.addParamDef("name", String.class).setNullable(false);
        this.addParamDef("version", String.class).setNullable(false);
        this.addParamDef("link", String.class).setNullable(false);
        this.addParamDef("filePath", Integer.class).setNullable(false);
    } // ctor
}
