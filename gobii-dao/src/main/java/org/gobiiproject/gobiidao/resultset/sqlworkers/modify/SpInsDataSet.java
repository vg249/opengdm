package org.gobiiproject.gobiidao.resultset.sqlworkers.modify;

import org.gobiiproject.gobiidao.resultset.core.SpDef;

import java.sql.Date;
import java.util.List;

/**
 * Created by Phil on 4/7/2016.
 */
public class SpInsDataSet extends SpDef {

    public SpInsDataSet() {

        super("{call createdataset(?,?,?,?,?,?,?,?,?,?,?,?,?)}");

        this.addParamDef("experimentid", Integer.class).setNullable(false);
        this.addParamDef("callinganalysisid", Integer.class).setNullable(false);
        this.addParamDef("datasetanalyses", List.class).setNullable(true);
        this.addParamDef("datatable", String.class).setNullable(false);
        this.addParamDef("datafile", String.class).setNullable(false);
        this.addParamDef("qualitytable", String.class).setNullable(true);
        this.addParamDef("qualityfile", String.class).setNullable(true);
        this.addParamDef("datasetscores", String.class).setNullable(true); //jsonb!!!
        this.addParamDef("createdby", Integer.class).setNullable(true);
        this.addParamDef("createdDate.class", Date.class).setNullable(true);
        this.addParamDef("modifiedby", Integer.class).setNullable(true);
        this.addParamDef("modifiedDate.class", Date.class).setNullable(true);
        this.addParamDef("datasetstatus", Integer.class).setNullable(false);


    } // ctor

//    public Integer projectId = null;
//    public Integer getEntityIdId() {
//        return projectId;
//    }


//    public void execute(Connection dbConnection) throws SQLException {
//
//        CallableStatement callableStatement = dbConnection.prepareCall("{call createproject(?,?,?,?,?,?,?,?,?)}");
//
//        callableStatement.setString(1,(String) parameters.get("projectName"));
//        callableStatement.setString(2,(String) parameters.get("projectCode"));
//        callableStatement.setString(3,(String) parameters.get("projectDescription"));
//        callableStatement.setInt(4,(Integer) parameters.get("piContact"));
//        callableStatement.setInt(5,(Integer) parameters.get("createdBy"));
//        callableStatement.setDate(6,(Date) parameters.get("createdDate"));
//        callableStatement.setInt(7,(Integer) parameters.get("modifiedby"));
//        callableStatement.setDate(8,(Date) parameters.get("modifiedDate"));
//        callableStatement.setInt(9,(Integer) parameters.get("projectStatus"));
//
//        callableStatement.registerOutParameter(10, Types.INTEGER);
//
//        projectId = callableStatement.executeUpdate();

//    } // execute()
}
