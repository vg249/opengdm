package org.gobiiproject.gobiiclient.dtorequests.brapi;

import org.apache.commons.lang.ObjectUtils;
import org.gobiiproject.gobiiapimodel.restresources.RestUri;
import org.gobiiproject.gobiiapimodel.types.ControllerType;
import org.gobiiproject.gobiiapimodel.types.ServiceRequestId;
import org.gobiiproject.gobiibrapi.calls.studies.search.BrapiRequestStudiesSearch;
import org.gobiiproject.gobiibrapi.calls.studies.search.BrapiResponseStudiesSearchItem;
import org.gobiiproject.gobiibrapi.core.derived.BrapiResponseEnvelopeList;
import org.gobiiproject.gobiiclient.core.ClientContext;
import org.gobiiproject.gobiiclient.core.restmethods.BrapiResourceDerived;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.Authenticator;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Created by Phil on 12/15/2016.
 */
public class BrapiTestSearchStudies {


    @BeforeClass
    public static void setUpClass() throws Exception {
        Assert.assertTrue(Authenticator.authenticate());

    }

    @AfterClass
    public static void tearDownUpClass() throws Exception {
        Assert.assertTrue(Authenticator.deAuthenticate());
    }


    @Test
    public void getStudies() throws Exception {


        RestUri restUriStudiesSearch = ClientContext.getInstance(null, false)
                .getUriFactory(ControllerType.BRAPI)
                .resourceColl(ServiceRequestId.URL_STUDIES_SEARCH);

//        BrapiResourceJson<BrapiRequestStudiesSearch, BrapiResponseStudiesSearchItem> brapiResourceJson = new BrapiResourceJson<>(restUriStudiesSearch,
//                BrapiRequestStudiesSearch.class,
//                BrapiResponseStudiesSearchItem.class);

        BrapiRequestStudiesSearch brapiRequestStudiesSearch = new BrapiRequestStudiesSearch();
        brapiRequestStudiesSearch.setStudyType("genotype");
        //BrapiResponseJson<BrapiResponseStudiesSearchItem> brapiResponseJson = brapiResourceJson.post(brapiRequestStudiesSearch);


        BrapiResourceDerived<BrapiRequestStudiesSearch,ObjectUtils.Null,BrapiResponseStudiesSearchItem> brapiResourceDerived =
                new BrapiResourceDerived<>(restUriStudiesSearch,
                        BrapiRequestStudiesSearch.class,
                        ObjectUtils.Null.class,
                        BrapiResponseStudiesSearchItem.class);

        BrapiResponseEnvelopeList<ObjectUtils.Null,BrapiResponseStudiesSearchItem> studiesResult = brapiResourceDerived.postToListResource(brapiRequestStudiesSearch);

        Assert.assertNotNull(studiesResult.getBrapiMetaData());
        Assert.assertNotNull(studiesResult.getBrapiMetaData().getDatafiles());
        Assert.assertNotNull(studiesResult.getBrapiMetaData().getPagination());
        Assert.assertNotNull(studiesResult.getBrapiMetaData().getStatus());

        Assert.assertTrue(studiesResult.getData().getData().size() > 0);

    }
}
