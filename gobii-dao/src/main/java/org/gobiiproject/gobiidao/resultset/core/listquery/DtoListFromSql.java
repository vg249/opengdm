package org.gobiiproject.gobiidao.resultset.core.listquery;

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
public class DtoListFromSql<T> implements Work {


    private Class<T> dtoType;
    private String sql;
    private Map<String, Object> parameters = null;
    public DtoListFromSql(Class<T> dtoType,
                          String sql,
                          Map<String, Object> parameters) {
        this.dtoType = dtoType;
        this.sql = sql;
        this.parameters = parameters;
    }


    List<T> dtoList = new ArrayList<>();

    public List<T> getDtoList() {
        return dtoList;
    }

    @Override
    public void execute(Connection dbConnection) throws SQLException {

        PreparedStatement preparedStatement = dbConnection.prepareStatement(sql);

       ResultSet resultSet = preparedStatement.executeQuery();

        List<T> dtoList = new ArrayList<>();
        while (resultSet.next()) {
            try {
                T dto = dtoType.newInstance();
                ResultColumnApplicator.applyColumnValues(resultSet, dto);
                dtoList.add(dto);
            } catch (IllegalAccessException e) {
                throw new SQLException(e);
            } catch(InstantiationException e) {
                throw new SQLException(e);
            }
        }


        //DataSetDTO.class.newInstance()  <== here's how you'll instance form type

    } // execute()
}
