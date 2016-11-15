package org.gobiiproject.gobiiclient.dtorequests.Helpers;

import org.gobiiproject.gobiiclient.dtorequests.dbops.crud.DtoCrudRequestTest;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;

import java.util.ArrayList;
import java.util.List;

/**
 * This class serves as a gate keeper for retrieving the PK values that are
 * stored in GlobalPkValues. The classes in the dbops.crud namespace all derive
 * from DtoCrudRequestTest, which requires implementation of a create() method.
 * Thus, by type-parameterizing this class for the particular DtoCrudRequest*Test
 * class, we are able to create the requested entity if it has not yet been created.
 * Because JUnit @Test methods must be parameterless and void, the only way we can
 * get the PK value out of the test class is to require that all the create() methods
 * add the PK values for the entities they create to GlobalPkValues. The awkwardness of
 * this idiom is compensated for by the error message which, in the case that the create()
 * method is not adding the PK values to GlobalPkValues, gives a precise indication of
 * where and why the thing went awry
 */
public class GlobalPkColl<T extends DtoCrudRequestTest> {


    // this only works if all create() methods put their PK value into
    public Integer getAPkVal(Class<T> dtoRequestTestType, GobiiEntityNameType gobiiEntityNameType) throws Exception {

        Integer returnVal = GlobalPkValues.getInstance().getAPkVal(gobiiEntityNameType);
        if (returnVal == null) {
            T testClass = dtoRequestTestType.newInstance();
            testClass.create();
            returnVal = GlobalPkValues.getInstance().getAPkVal(gobiiEntityNameType);

            if (returnVal == null) {
                throw new Exception("Error retrieving test pk for entity "
                        + gobiiEntityNameType.toString()
                        + ":  "
                        + dtoRequestTestType.toString()
                        + ".create()"
                        + " may not be adding its PK value to the to GlobalPkValues");
            }
        }

        return returnVal;

    } //

    public List<Integer> getPkVals(Class<T> dtoRequestTestType,
                                   GobiiEntityNameType gobiiEntityNameType,
                                   Integer totalPks) throws Exception {

        List<Integer> returnVal = new ArrayList<>();

        for( int idx=0; idx< totalPks; idx++) {
            returnVal.add(this.getAPkVal(dtoRequestTestType,gobiiEntityNameType));
        }

        return  returnVal;

    } //

}
