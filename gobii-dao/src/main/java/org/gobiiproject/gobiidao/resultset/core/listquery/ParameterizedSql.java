package org.gobiiproject.gobiidao.resultset.core.listquery;

import java.util.Map;
import java.util.Set;
import org.gobiiproject.gobiidao.GobiiDaoException;

public class ParameterizedSql {


    private static final String PARAM_DELIM_OPEN = "{";
    private static final String PARAM_DELIM_CLOSE = "}";
    private String sqlTemplate;
    public ParameterizedSql(String sqlTemplate, Map<String,String> parameters)throws GobiiDaoException{

        this.sqlTemplate = sqlTemplate;

        Set<String> paramNames = parameters.keySet();
        for( String currentParam : paramNames ) {
            this.validateSqlParam(currentParam);
        }
    }


    public String getSql() {return this.sqlTemplate;}

    private void validateSqlParam(String paramName) throws GobiiDaoException {

        if( !sqlTemplate.contains(paramName)) {
            throw new GobiiDaoException("The specified sql does not contain a required tempalte parameter ("
                    + paramName
                    +"): " +
                    sqlTemplate);
        }
    }


    public static String makeDelimitedParamName(String paramName) {
        return ParameterizedSql.PARAM_DELIM_OPEN + paramName + ParameterizedSql.PARAM_DELIM_CLOSE;
    }

    public ParameterizedSql setParamValue(String paramName, String paramValue ) throws GobiiDaoException {

        this.validateSqlParam(paramName);

        while(this.sqlTemplate.contains(paramName)) {
            this.sqlTemplate = this.sqlTemplate.replace(paramName,paramValue);
        }

        return this;
    }



}
