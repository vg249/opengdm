package org.gobiiproject.gobiidao.resultset.sqlworkers.read;

import org.hibernate.jdbc.Work;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by VCalaminos on 6/14/2017.
 */
public class SpGetSampleDetailsByExternalCode implements Work {

    private Map<String, Object> parameters = null;

    public SpGetSampleDetailsByExternalCode(Map<String,Object> parameters) { this.parameters = parameters; }

    private ResultSet resultSet = null;

    public ResultSet getResultSet() { return resultSet; }


    @Override
    public void execute(Connection dbConnection) throws SQLException {

        String sql = "select\n" +
                "\tdnr.dnarun_id,\n" +
                "\tdns.dnasample_id,\n" +
                "\tg.external_code,\n" +
                "\tg.germplasm_id,\n" +
                "\tg.name as germplasmName,\n" +
                "\t(\n" +
                "\t\tselect\n" +
                "\t\t\tcv.term\n" +
                "\t\tfrom\n" +
                "\t\t\tcv\n" +
                "\t\twhere\n" +
                "\t\t\tcvgroup_id =(\n" +
                "\t\t\t\tselect\n" +
                "\t\t\t\t\tcvgroup_id\n" +
                "\t\t\t\tfrom\n" +
                "\t\t\t\t\tcvgroup\n" +
                "\t\t\t\twhere\n" +
                "\t\t\t\t\tname = 'analysis_type'\n" +
                "\t\t\t\t\tand type = 1\n" +
                "\t\t\t)\n" +
                "\t\t\tand cv.cv_id = d.callinganalysis_id   -- the join from dnarun to dataset doesn't work; need to use dnarun.dataset_dnarun_idx instead\n" +
                "\t) as analysisMethodName,\n" +
                "\t-- we don't have such a thing\n" +
                " null as markerCount -- number of markers for this sample -- will require jsonb fu\n" +
                "from\n" +
                "\tdnasample dns left outer join germplasm g on\n" +
                "\t(\n" +
                "\t\tdns.germplasm_id = g.germplasm_id\n" +
                "\t) left outer join dnarun dnr on\n" +
                "\t(\n" +
                "\t\tdns.dnasample_id = dnr.dnasample_id\n" +
                "\t) join experiment e on\n" +
                "\t(\n" +
                "\t\tdnr.experiment_id = e.experiment_id\n" +
                "\t) join dataset d on\n" +
                "\t(\n" +
                "\t\te.experiment_id = d.dataset_id\n" +
                "\t)\n" +
                "where g.external_code=?";

        PreparedStatement preparedStatement = dbConnection.prepareStatement(sql);
        preparedStatement.setString(1, parameters.get("externalCode").toString());
        resultSet = preparedStatement.executeQuery();

    }

}
