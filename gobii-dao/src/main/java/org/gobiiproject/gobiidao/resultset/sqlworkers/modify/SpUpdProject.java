package org.gobiiproject.gobiidao.resultset.sqlworkers.modify;

import java.util.Date;
import org.gobiiproject.gobiidao.resultset.core.SpDef;


/**
 * Created by Phil on 4/7/2016.
 */
public class SpUpdProject extends SpDef {

    public SpUpdProject() {

        super("{call updateProject(?,?,?,?,?,?,?,?,?,?)}", false);

        this.addParamDef("projectId", Integer.class)
                .setNullable(false);

        this.addParamDef("projectName", String.class)
                .setNullable(false);

        this.addParamDef("projectCode", String.class)
                .setNullable(false);

        this.addParamDef("projectDescription", String.class)
                .setNullable(true);

        this.addParamDef("piContact", Integer.class)
                .setNullable(false);

        this.addParamDef("createdBy", Integer.class)
                .setNullable(true);

        this.addParamDef("createdDate", Date.class)
                .setNullable(true);

        this.addParamDef("modifiedBy", Integer.class)
                .setNullable(true);

        this.addParamDef("modifiedDate", Date.class)
                .setNullable(true);

        this.addParamDef("projectStatus", Integer.class)
                .setNullable(false);


    } // ctor

}
