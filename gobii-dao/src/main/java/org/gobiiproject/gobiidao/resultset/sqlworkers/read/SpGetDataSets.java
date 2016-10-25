package org.gobiiproject.gobiidao.resultset.sqlworkers.read;

import org.gobiiproject.gobiidao.resultset.core.ResultColumnApplicator;
import org.gobiiproject.gobiimodel.headerlesscontainer.DataSetDTO;
import org.hibernate.jdbc.Work;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Phil on 4/7/2016.
 */
public class SpGetDataSets implements Work {

    private Map<String, Object> parameters = null;

    public SpGetDataSets() {
    }


    private ResultSet resultSet = null;

    public ResultSet getResultSet() {
        return resultSet;
    }

    @Override
    public void execute(Connection dbConnection) throws SQLException {

        String sql = "select\n" +
                "\tds.dataset_id,\n" +
                "\tds.name,\n" +
                "\tds.experiment_id,\n" +
                "\tds.callinganalysis_id,\n" +
                "\tds.analyses,\n" +
                "\tds.data_table,\n" +
                "\tds.data_file,\n" +
                "\tds.quality_table,\n" +
                "\tds.quality_file,\n" +
//                "\tds.scores,\n" +
                "\tds.created_by,\n" +
                "\tds.created_date,\n" +
                "\tds.modified_by,\n" +
                "\tds.modified_date,\n" +
                "\tds.status, \n" +
                "\tds.type_id \n" +
                "from dataset ds \n" +
                "order by lower(name)";

        PreparedStatement preparedStatement = dbConnection.prepareStatement(sql);

        resultSet = preparedStatement.executeQuery();

        List<DataSetDTO> contacts = new ArrayList<>();
        while (resultSet.next()) {
            try {
                DataSetDTO dataSetDTO = DataSetDTO.class.newInstance();
                ResultColumnApplicator.applyColumnValues(resultSet, dataSetDTO);
                contacts.add(dataSetDTO);
            } catch (IllegalAccessException e) {
                throw new SQLException(e);
            } catch(InstantiationException e) {
                throw new SQLException(e);
            }
        }


        String temp = "foo";

        //DataSetDTO.class.newInstance()  <== here's how you'll instance form type

    } // execute()
}
