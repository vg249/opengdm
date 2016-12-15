package org.gobiiproject.gobiibrapi.calls.calls;

import org.gobiiproject.gobiibrapi.types.BrapiDataTypes;
import org.gobiiproject.gobiimodel.types.RestMethodTypes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Phil on 12/15/2016.
 */
public class BrapiResponseMapCalls {

    public List<BrapiResponseCallsItem> getBrapiResponseCallsItems() {

        List<BrapiResponseCallsItem> returnVal = new ArrayList<>();

        returnVal.add(new BrapiResponseCallsItem("calls",
                Arrays.asList(RestMethodTypes.GET),
                Arrays.asList(BrapiDataTypes.JSON)));

        return returnVal;
    }
}
