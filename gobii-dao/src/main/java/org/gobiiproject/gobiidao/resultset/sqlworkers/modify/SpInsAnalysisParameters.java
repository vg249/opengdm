package org.gobiiproject.gobiidao.resultset.sqlworkers.modify;

import org.gobiiproject.gobiidao.resultset.core.SpDef;

/**
 * Created by Phil on 4/7/2016.
 */
public class SpInsAnalysisParameters extends SpDef {

    public SpInsAnalysisParameters() {

        super("{call upsertanalysisparameter(?,?,?)}");

        this.addParamDef("analysisId", Integer.class)
                .setNullable(false);

        this.addParamDef("parameterName",String.class)
                .setNullable(false);

        this.addParamDef("parameterValue",String.class)
                .setNullable(true);

    } // ctor
}
