package org.gobiiproject.gobiiclient.dtorequests.brapi;

import org.gobiiproject.gobiibrapi.core.json.BrapiResponseJson;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Phil on 12/16/2016.
 */
public class BrapiTestResponseStructure<T_RESPONSE_OBJ_DATA_LIST> {

    @Test
    public void validatateBrapiResponseStructure(BrapiResponseJson<T_RESPONSE_OBJ_DATA_LIST> brapiResponseJson) {

        Assert.assertNotNull(brapiResponseJson.getBrapiMetaData());
        Assert.assertNotNull(brapiResponseJson.getData());
        Assert.assertNotNull(brapiResponseJson.getResultMasterJson());
    }

}
