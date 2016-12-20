package org.gobiiproject.gobiiclient.dtorequests.brapi;

import org.apache.commons.lang.ObjectUtils;
import org.gobiiproject.gobiiapimodel.restresources.RestUri;
import org.gobiiproject.gobiiapimodel.types.ControllerType;
import org.gobiiproject.gobiiapimodel.types.ServiceRequestId;
import org.gobiiproject.gobiibrapi.calls.calls.BrapiResponseCallsItem;
import org.gobiiproject.gobiibrapi.calls.studies.search.BrapiRequestStudiesSearch;
import org.gobiiproject.gobiibrapi.calls.studies.search.BrapiResponseStudiesSearchItem;
import org.gobiiproject.gobiibrapi.core.derived.BrapiResponseEnvelopeList;
import org.gobiiproject.gobiibrapi.core.json.BrapiResponseJson;
import org.gobiiproject.gobiiclient.core.ClientContext;
import org.gobiiproject.gobiiclient.core.restmethods.BrapiResourceDerived;
import org.gobiiproject.gobiiclient.core.restmethods.BrapiResourceJson;
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

        BrapiResourceDerived<ObjectUtils.Null,ObjectUtils.Null,BrapiResponseCallsItem> brapiResourceDerived =
                new BrapiResourceDerived<>(restUriCalls,
                        ObjectUtils.Null.class,
                        ObjectUtils.Null.class,
                        BrapiResponseCallsItem.class);

        BrapiResponseEnvelopeList<ObjectUtils.Null,BrapiResponseCallsItem> callsResult = brapiResourceDerived.getFromListResource();

        BrapiTestResponseStructure.validatateBrapiResponseStructure(callsResult.getBrapiMetaData());

        Assert.assertTrue(callsResult.getData().getData().size() > 0 );

    }
}
