package org.gobiiproject.gobiidao.resultset.core;


import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Phil on 4/18/2016.
 */
public class SpRunnerCallable implements Work {

    public SpRunnerCallable(SpDef spDef, Map<String, Object> paramVals) {

        this.spDef = spDef;
        this.paramVals = paramVals;

    }  // ctor

    @PersistenceContext
    protected EntityManager em;

    private SpDef spDef = null;
    private Map<String, Object> paramVals = null;

    List<String> errors = new ArrayList<>();
    Integer result = null;

    public List<String> getErrors() {
        return errors;
    }

    public String getErrorString() {
        String returnVal = null;

        for(String currentMessage : errors ) {
            returnVal += currentMessage + "; ";
        }

        return returnVal;
    }

    public Integer getResult() {
        return result;
    }

    public boolean run() {

        boolean returnVal = true;

        // first do validation checking
        List<SpParamDef> paramDefs = spDef.getSpParamDefs();
        for (int idx = 0; idx < paramDefs.size() && returnVal; idx++) {

            SpParamDef currentParamDef = paramDefs.get(idx);
            String currentParamName = currentParamDef.getParamName();

            if (paramVals.containsKey(currentParamName)) {

                Object currentParamVal = paramVals.get(currentParamName);
                if (null == currentParamVal && !currentParamDef.isNullable()) {
                    errors.add("Parameter is not allowed to be null " + currentParamName);
                } else if (!currentParamVal.getClass().equals(currentParamDef.getParamType())) {
                    errors.add("Parameter " + currentParamName + " should be of type " + currentParamDef.getParamType());
                } else {
                    if (null != currentParamVal) {
                        currentParamDef.setCurrentValue(currentParamVal);
                    } else {
                        currentParamDef.setCurrentValue(currentParamDef.getDefaultValue());
                    }
                }

            } else {
                returnVal = false;
                errors.add("There is no value param entry for parameter " + currentParamName);
            }


        } // iterate param defs


        returnVal = errors.size() == 0;

        if (returnVal) {
            try {
                Session session = (Session) em.getDelegate();
                session.doWork(this);

            } catch (Exception e) {
                returnVal = false;
                errors.add("Exception executing stored proc " + spDef.getCallString() + ": " + e.getMessage());

            }
        }

        return returnVal;

    } // run()

    @Override
    public void execute(Connection connection) throws SQLException {

        CallableStatement callableStatement = connection.prepareCall(spDef.getCallString());

        List<SpParamDef> paramDefs = spDef.getSpParamDefs();

        for (SpParamDef currentParamDef : paramDefs) {

            Integer currentParamIndex = currentParamDef.getOrderIdx();
            String nameCurrentParamname = currentParamDef.getParamName();
            Type currentParamType = currentParamDef.getParamType();
            Object currentParamValue = currentParamDef.getCurrentValue();

            if (currentParamType.equals(String.class)) {
                callableStatement.setString(currentParamIndex, (String) currentParamValue);
            } else if (currentParamType.equals(Integer.class)) {
                callableStatement.setInt(currentParamIndex, (Integer) currentParamValue);
            } else if (currentParamType.equals(Date.class)) {
                callableStatement.setDate(currentParamIndex, (Date) currentParamValue);
            } else {
                throw new SQLException("Unsupported param type: " + Type.class.toString());
            }
        }

        callableStatement.registerOutParameter(paramDefs.size() + 1, Types.INTEGER);

        result = callableStatement.executeUpdate();

    } // execute
}
