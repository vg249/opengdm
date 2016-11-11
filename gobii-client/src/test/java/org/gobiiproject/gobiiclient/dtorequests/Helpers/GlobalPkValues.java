package org.gobiiproject.gobiiclient.dtorequests.Helpers;

import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Phil on 11/11/2016.
 */
public class GlobalPkValues {

    private static GlobalPkValues instance = null;

    protected GlobalPkValues() {
        // Exists only to defeat instantiation.
    }

    public static GlobalPkValues getInstance() {
        if (instance == null) {
            instance = new GlobalPkValues();
        }
        return instance;
    }

    Map<GobiiEntityNameType, List<Integer>> pkMap = new EnumMap<>(GobiiEntityNameType.class);

    public Integer getPkValCount(GobiiEntityNameType gobiiEntityNameType) {

        Integer returnVal = 0;

        if( pkMap.containsKey(gobiiEntityNameType)) {
            returnVal = pkMap.get(gobiiEntityNameType).size();
        }

        return returnVal;
    }

    public Integer getAPkVal(GobiiEntityNameType gobiiEntityNameType) {

        Integer returnVal = null;

        if( pkMap.containsKey(gobiiEntityNameType)
                && pkMap.get(gobiiEntityNameType).size() > 0 ) {
            returnVal = pkMap.get(gobiiEntityNameType).get(0); // get arbitrary value for now
        }

        return returnVal;
    }



    public Integer addPkVal(GobiiEntityNameType gobiiEntityNameType, Integer pkVal ) {

        if( !pkMap.containsKey(gobiiEntityNameType) ) {
            pkMap.put(gobiiEntityNameType,new ArrayList<>());
        }

        pkMap.get(gobiiEntityNameType).add(pkVal);

        return pkMap.size();
    }
}
