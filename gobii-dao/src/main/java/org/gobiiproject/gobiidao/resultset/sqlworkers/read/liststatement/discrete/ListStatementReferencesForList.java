package org.gobiiproject.gobiidao.resultset.sqlworkers.read.liststatement.discrete;

import org.gobiiproject.gobiidao.resultset.core.listquery.ListSqlId;
import org.gobiiproject.gobiidao.resultset.core.listquery.ListStatement;
import org.gobiiproject.gobiidao.resultset.core.listquery.ParameterizedSql;
import org.gobiiproject.gobiimodel.dto.entity.children.NameIdDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by VCalaminos on 1/10/2018.
 */
public class ListStatementReferencesForList implements ListStatement {

    private final String PARAM_NAME_NAME_LIST = "nameArray";

    @Override
    public ListSqlId getListSqlId() { return ListSqlId.QUERY_ID_REFERENCE_BY_LIST; }

    @Override
    public PreparedStatement makePreparedStatement(Connection dbConnection, Map<String, Object> jdbcParamVals, Map<String, Object> sqlParamVals) throws SQLException {

        String parsedNameList = "";

        List<NameIdDTO> nameArray = (ArrayList) sqlParamVals.get(PARAM_NAME_NAME_LIST);

        // parse array into csv

        for (NameIdDTO nameIdDTO : nameArray) {

            String quotedName = "'" +  nameIdDTO.getName() + "'";

            parsedNameList = (parsedNameList.equals("")) ? quotedName : parsedNameList + ", " + quotedName;

        }

        ParameterizedSql parameterizedSql =
                new ParameterizedSql("select * " +
                        "from reference " +
                        "where name in (" + PARAM_NAME_NAME_LIST + ") ",
                        new HashMap<String, String>(){
                            {
                                put(PARAM_NAME_NAME_LIST, null);
                            }
                        });

        String sql = parameterizedSql
                .setParamValue(PARAM_NAME_NAME_LIST, parsedNameList)
                .getSql();

        PreparedStatement returnVal = dbConnection.prepareStatement(sql);

        return returnVal;

    }


}
