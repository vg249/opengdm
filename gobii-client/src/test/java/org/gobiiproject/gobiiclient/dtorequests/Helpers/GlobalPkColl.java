package org.gobiiproject.gobiiclient.dtorequests.Helpers;

import org.gobiiproject.gobiiclient.dtorequests.dtorequest.DtoRequestTest;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Phil on 11/11/2016.
 */
public class GlobalPkColl<T extends DtoRequestTest> {


    // this only works if all create() methods put their PK value into
    public Integer getAPkVal(Class<T> dtoType, GobiiEntityNameType gobiiEntityNameType) throws Exception {

        Integer returnVal = GlobalPkValues.getInstance().getAPkVal(gobiiEntityNameType);
        if (returnVal == null) {
            T testClass = dtoType.newInstance();
            testClass.create();
            returnVal = GlobalPkValues.getInstance().getAPkVal(gobiiEntityNameType);
        }
        return returnVal;

    }

}
