package org.gobiiproject.gobiiclient.dtorequests.Helpers;

import org.gobiiproject.gobiiclient.dtorequests.dbops.crud.DtoCrudRequestTest;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;

/**
 * Created by Phil on 11/11/2016.
 */
public class GlobalPkColl<T extends DtoCrudRequestTest> {


    // this only works if all create() methods put their PK value into
    public Integer getAPkVal(Class<T> dtoRequestTestType, GobiiEntityNameType gobiiEntityNameType) throws Exception {

        Integer returnVal = GlobalPkValues.getInstance().getAPkVal(gobiiEntityNameType);
        if (returnVal == null) {
            T testClass = dtoRequestTestType.newInstance();
            testClass.create();
            returnVal = GlobalPkValues.getInstance().getAPkVal(gobiiEntityNameType);

            if( returnVal == null ) {
                throw new Exception("Error retrieving test pk for entity "
                        + gobiiEntityNameType.toString()
                        + ":  "
                        + dtoRequestTestType.toString()
                        + ".create()"
                        + " may not be adding its PK value to the to GlobalPkValues");
            }
        }

        return returnVal;

    }

}
