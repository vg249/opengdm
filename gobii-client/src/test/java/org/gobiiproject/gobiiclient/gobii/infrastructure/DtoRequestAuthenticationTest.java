// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.gobii.infrastructure;


import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.gobii.GobiiUriFactory;
import org.gobiiproject.gobiiapimodel.restresources.common.RestUri;
import org.gobiiproject.gobiiapimodel.types.GobiiServiceRequestId;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContext;
import org.gobiiproject.gobiiclient.core.gobii.GobiiAuthenticator;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiiclient.core.gobii.GobiiTestConfiguration;
import org.gobiiproject.gobiiclient.gobii.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.config.GobiiCropConfig;
import org.gobiiproject.gobiimodel.headerlesscontainer.AnalysisDTO;
import org.gobiiproject.gobiiapimodel.types.GobiiControllerType;
import org.gobiiproject.gobiimodel.headerlesscontainer.ContactDTO;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.URL;
import java.util.List;

public class DtoRequestAuthenticationTest {


    @BeforeClass
    public static void setUpClass() throws Exception {
//        Assert.assertTrue(GobiiAuthenticator.authenticate());
    }

    //
    @AfterClass
    public static void tearDownUpClass() throws Exception {
        Assert.assertTrue(GobiiAuthenticator.deAuthenticate());
    }


    @Test
    public void testSucceedWithOneAuthentication() throws Exception {

        Assert.assertTrue(GobiiAuthenticator.authenticate());
    }

    @Test
    public void testFailOnSecondAuthentication() throws Exception {

        Assert.assertTrue(GobiiAuthenticator.authenticate());
        String oldToken = GobiiClientContext.getInstance(null, false).getUserToken();

        Assert.assertTrue(GobiiAuthenticator.authenticate());
        String newToken = GobiiClientContext.getInstance(null, false).getUserToken();

        Assert.assertNotEquals(oldToken, newToken);

        AnalysisDTO analysisDTORequest = new AnalysisDTO();
        analysisDTORequest.setAnalysisId(1);


        String url = GobiiServiceRequestId.URL_ANALYSIS.getRequestUrl(GobiiClientContext.getInstance(null, false).getCurrentCropContextRoot(),
                GobiiControllerType.GOBII.getControllerPath());

//        DtoRequestProcessor<AnalysisDTO> dtoDtoRequestProcessor = new DtoRequestProcessor<>();


    }

    private String makeUrl(GobiiCropConfig gobiiCropConfig) throws Exception {

//        String returnVal = "http://"
//                + gobiiCropConfig.getHost()
//                + ":"
//                + gobiiCropConfig.getPort().toString()
//                + "/"
//

        String returnVal;

        URL url = new URL("http",
                gobiiCropConfig.getHost(),
                gobiiCropConfig.getPort(),
                gobiiCropConfig.getContextPath());

        returnVal = url.toString();
        return returnVal;
    }

    @Test
    public void testSwitchToSecondServer() throws Exception {

        GobiiTestConfiguration gobiiTestConfiguration = new GobiiTestConfiguration();
        List<GobiiCropConfig> activeGobiiCropConfigs = gobiiTestConfiguration.getConfigSettings().getActiveCropConfigs();
        if (activeGobiiCropConfigs.size() > 1) {
            // http://localhost:8282/gobii-dev/


            // ****************** FIRST LOGIN
            GobiiCropConfig gobiiCropConfigOne = activeGobiiCropConfigs.get(0);
            String serviceUrlOne = makeUrl(gobiiCropConfigOne);
            String cropIdOne = GobiiClientContext.getInstance(serviceUrlOne, true).getDefaultCropType();

            GobiiClientContext.getInstance(null, false)
                    .setCurrentClientCrop(cropIdOne);

            Assert.assertNotNull("Could not get testexecconfig", GobiiAuthenticator.getTestExecConfig());

            String testUser = GobiiAuthenticator.getTestExecConfig().getLdapUserForUnitTest();
            String testPassword = GobiiAuthenticator.getTestExecConfig().getLdapPasswordForUnitTest();


            GobiiClientContext.getInstance(null, false).login(testUser, testPassword);

            // ****************** SECOND LOGIN
            GobiiCropConfig gobiiCropConfigTwo = activeGobiiCropConfigs.get(1);
            String serviceUrlTwo = makeUrl(gobiiCropConfigTwo);


            GobiiClientContext.resetConfiguration();
//            String cropIdTwo = GobiiClientContext.getInstance(serviceUrlTwo,true).getDefaultCropType();
            GobiiClientContext.getInstance(serviceUrlTwo, true);
            String cropIdTwo = gobiiCropConfigTwo.getGobiiCropType();

            GobiiClientContext.getInstance(null, false)
                    .setCurrentClientCrop(cropIdTwo);

            GobiiClientContext.getInstance(null, false).login(testUser, testPassword);

            GobiiClientContext.getInstance(null, false).setCurrentClientCrop(cropIdTwo);


            // do contacts request with crop two context root
            String currentCropContextRoot = GobiiClientContext.getInstance(null, false).getCurrentCropContextRoot();
            GobiiUriFactory gobiiUriFactory = new GobiiUriFactory(currentCropContextRoot);
            RestUri restUriContact = gobiiUriFactory
                    .resourceByUriIdParam(GobiiServiceRequestId.URL_CONTACTS);
            restUriContact.setParamValue("id", "6");
            GobiiEnvelopeRestResource<ContactDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(restUriContact);
            PayloadEnvelope<ContactDTO> resultEnvelope = gobiiEnvelopeRestResource
                    .get(ContactDTO.class);
            Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));


            // now set current root to crop onej -- this will cause an error because the
            // gobiiUriFactory still has crop two's context root.
            GobiiClientContext.getInstance(null, false).setCurrentClientCrop(cropIdOne);
            restUriContact = gobiiUriFactory
                    .resourceByUriIdParam(GobiiServiceRequestId.URL_CONTACTS);
            restUriContact.setParamValue("id", "6");
            gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(restUriContact);
            resultEnvelope = gobiiEnvelopeRestResource
                    .get(ContactDTO.class);

            Assert.assertTrue("Method with incorrectly configured gobiiUriFactory should have failed with 404",
                    resultEnvelope
                            .getHeader()
                            .getStatus()
                            .getStatusMessages()
                            .stream()
                            .filter(m -> m.getMessage().contains("failed with status code 404"))
                            .count() == 1
            );


            // create a new factory with correct context root and re-do the request
            // this should now work
            GobiiClientContext.getInstance(null, false).login(testUser, testPassword);
            String currentCropContextRootForCropOne = GobiiClientContext.getInstance(null, false).getCropContextRoot(cropIdOne);
            gobiiUriFactory = new GobiiUriFactory(currentCropContextRootForCropOne);
            restUriContact = gobiiUriFactory
                    .resourceByUriIdParam(GobiiServiceRequestId.URL_CONTACTS);
            restUriContact.setParamValue("id", "6");
            gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(restUriContact);
            resultEnvelope = gobiiEnvelopeRestResource
                    .get(ContactDTO.class);


            Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));


        }

    }

    @Test
    public void testSwitchToSecondCrop () throws Exception {


        // these steps require physical access to a config file. Other clients do not have
        // this access. So we need to make sure that, aside from retrieving te config URL nad the
        // username/password, the remainder of the test does not consume the gobiiTestConfiguration.
        GobiiTestConfiguration gobiiTestConfiguration = new GobiiTestConfiguration();
        String initialConfigUrl = gobiiTestConfiguration.getConfigSettings().getTestExecConfig().getInitialConfigUrl();
        String testUser = gobiiTestConfiguration.getConfigSettings().getTestExecConfig().getLdapUserForUnitTest();
        String testPassword = gobiiTestConfiguration.getConfigSettings().getTestExecConfig().getLdapPasswordForUnitTest();

        GobiiClientContext.getInstance(initialConfigUrl, true);
        List<String> activeCrops = GobiiClientContext.getInstance(null, false).getCropTypeTypes();
        if (activeCrops.size() > 1) {

            String cropIdOne = activeCrops.get(0);
            String cropIdTwo = activeCrops.get(1);

            // ****************** FIRST LOGIN
            GobiiClientContext.getInstance(null, false)
                    .setCurrentClientCrop(cropIdOne);
            GobiiClientContext.getInstance(null, false).login(testUser, testPassword);
            Assert.assertNotNull("Authentication with first crop failed: " + cropIdOne,
                    GobiiClientContext.getInstance(null,true).getUserToken());

            String cropOneToken = GobiiClientContext.getInstance(null,true).getUserToken();

            // ****************** SECOND LOGIN

            GobiiClientContext.getInstance(null, false)
                    .setCurrentClientCrop(cropIdTwo);
            GobiiClientContext.getInstance(null, false).login(testUser, testPassword);
            Assert.assertNotNull("Authentication with second crop failed: " + cropIdTwo,
                    GobiiClientContext.getInstance(null,true).getUserToken());

            String cropTwoToken = GobiiClientContext.getInstance(null,true).getUserToken();

            Assert.assertFalse("The tokens for the two authentications should be different: " + cropOneToken + "," + cropTwoToken,
                    cropOneToken.equals(cropTwoToken));

        }
    }

}
