package org.gobiiproject.gobiidao.resultset.sqlworkers.modify;

import java.util.Date;
import org.gobiiproject.gobiidao.resultset.core.SpDef;

/**
 * Created by Angel on 5/4/2016.
 */
public class SpInsDisplay extends SpDef {

    public SpInsDisplay() {

        super("{call createdisplay(?,?,?,?,?,?,?,?)}");

        this.addParamDef("tableName", String.class).setNullable(false);
        this.addParamDef("columnName", String.class).setNullable(true);
        this.addParamDef("displayName", String.class).setNullable(true);
        this.addParamDef("createdBy", Integer.class).setNullable(true);
        this.addParamDef("createdDate", Date.class).setNullable(true);
        this.addParamDef("modifiedBy", Integer.class).setNullable(true);
        this.addParamDef("modifiedDate", Date.class).setNullable(true);
        this.addParamDef("displayRank", Integer.class).setNullable(false);


    } // ctor

}
