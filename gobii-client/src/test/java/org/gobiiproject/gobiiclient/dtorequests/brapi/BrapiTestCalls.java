package org.gobiiproject.gobiiclient.dtorequests.brapi;

import org.apache.commons.lang.ObjectUtils;
import org.gobiiproject.gobiiapimodel.restresources.RestUri;
import org.gobiiproject.gobiiapimodel.types.ControllerType;
import org.gobiiproject.gobiiapimodel.types.ServiceRequestId;
import org.gobiiproject.gobiibrapi.calls.calls.BrapiResponseCallsItem;
import org.gobiiproject.gobiibrapi.core.json.BrapiResponseJson;
import org.gobiiproject.gobiiclient.core.ClientContext;
import org.gobiiproject.gobiiclient.core.restmethods.BrapiResource;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.Authenticator;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Created by Phil on 12/15/2016.
 */
public class BrapiTestCalls {


    @BeforeClass
    public static void setUpClass() throws Exception {
        Assert.assertTrue(Authenticator.authenticate());

    }

    @AfterClass
    public static void tearDownUpClass() throws Exception {
        Assert.assertTrue(Authenticator.deAuthenticate());
    }


    @Test
    public void getCalls() throws Exception {


        RestUri restUriCalls = ClientContext.getInstance(null, false)
                .getUriFactory(ControllerType.BRAPI)
                .resourceColl(ServiceRequestId.URL_CALLS);

        BrapiResource<ObjectUtils.Null, BrapiResponseCallsItem> brapiResource =
                new BrapiResource<>(restUriCalls, ObjectUtils.Null.class, BrapiResponseCallsItem.class);

        BrapiResponseJson<BrapiResponseCallsItem> brapiResponseJsonCalls = brapiResource.get();

        (new BrapiTestResponseStructure<BrapiResponseCallsItem>()).validatateBrapiResponseStructure(brapiResponseJsonCalls);

        Assert.assertTrue(brapiResponseJsonCalls.getData().size() > 0 );

    }
}
