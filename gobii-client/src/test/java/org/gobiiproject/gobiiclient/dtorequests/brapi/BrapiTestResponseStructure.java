package org.gobiiproject.gobiiclient.dtorequests.brapi;

import org.gobiiproject.gobiibrapi.core.BrapiResponse;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Phil on 12/16/2016.
 */
public class BrapiTestResponseStructure<T_RESPONSE_OBJ_DATA_LIST> {

    @Test
    public void validatateBrapiResponseStructure(BrapiResponse<T_RESPONSE_OBJ_DATA_LIST> brapiResponse) {

        Assert.assertNotNull(brapiResponse.getBrapiMetaData());
        Assert.assertNotNull(brapiResponse.getData());
        Assert.assertNotNull(brapiResponse.getResultMasterJson());
    }

}
