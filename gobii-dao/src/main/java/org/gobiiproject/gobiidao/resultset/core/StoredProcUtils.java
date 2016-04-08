package org.gobiiproject.gobiidao.resultset.core;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Phil on 4/8/2016.
 */
public class StoredProcUtils {

    public static List<Map<String,Object>> convertResultSetToList(ResultSet rs) throws SQLException {

        List<Map<String,Object>> listReturnVal = new ArrayList<>();

        ResultSetMetaData metaData = rs.getMetaData();
        int columns = metaData.getColumnCount();

        while (rs.next()) {

            HashMap<String,Object> currentRowMap = new HashMap<>(columns);
            for(int currentColumnIdx=1; currentColumnIdx<=columns; ++currentColumnIdx) {

                currentRowMap.put(metaData.getColumnName(currentColumnIdx),rs.getObject(currentColumnIdx));

            }

            listReturnVal.add(currentRowMap);
        }

        return listReturnVal;

    } // convertResultSetToList()

} // StoredProcUtils
