package org.gobiiproject.gobiiclient.dtorequests.brapi;

import org.gobiiproject.gobiiapimodel.restresources.RestUri;
import org.gobiiproject.gobiiapimodel.types.ControllerType;
import org.gobiiproject.gobiiapimodel.types.ServiceRequestId;
import org.gobiiproject.gobiibrapi.calls.studies.search.BrapiRequestStudiesSearch;
import org.gobiiproject.gobiibrapi.calls.studies.search.BrapiResponseStudiesSearchItem;
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

        BrapiResource<BrapiRequestStudiesSearch, BrapiResponseStudiesSearchItem> brapiResource = new BrapiResource<>(restUriStudiesSearch,
                BrapiRequestStudiesSearch.class,
                BrapiResponseStudiesSearchItem.class);

        BrapiRequestStudiesSearch brapiRequestStudiesSearch = new BrapiRequestStudiesSearch();
        brapiRequestStudiesSearch.setStudyType("genotype");
        BrapiResponseJson<BrapiResponseStudiesSearchItem> brapiResponseJson = brapiResource.post(brapiRequestStudiesSearch);

        (new BrapiTestResponseStructure<BrapiResponseStudiesSearchItem>()).validatateBrapiResponseStructure(brapiResponseJson);
    }
}
