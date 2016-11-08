// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests.dtorequest;


import org.gobiiproject.gobiiclient.core.ClientContext;
import org.gobiiproject.gobiiapimodel.restresources.ResourceBuilder;
import org.gobiiproject.gobiiclient.core.restmethods.dtopost.DtoRequestProcessor;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.Authenticator;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestConfiguration;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.config.CropConfig;
import org.gobiiproject.gobiimodel.dto.container.AnalysisDTO;
import org.gobiiproject.gobiiapimodel.types.ControllerType;
import org.gobiiproject.gobiiapimodel.types.ServiceRequestId;
import org.gobiiproject.gobiimodel.types.SystemUserDetail;
import org.gobiiproject.gobiimodel.types.SystemUserNames;
import org.gobiiproject.gobiimodel.types.SystemUsers;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.URL;
import java.util.List;

public class DtoRequestAuthenticationTest {

    //    @BeforeClass
//    public static void setUpClass() throws Exception {
//        Assert.assertTrue(Authenticator.authenticate());
//    }
//
    @AfterClass
    public static void tearDownUpClass() throws Exception {
        Assert.assertTrue(Authenticator.deAuthenticate());
    }


    @Test
    public void testSucceedWithOneAuthentication() throws Exception {

        Assert.assertTrue(Authenticator.authenticate());

        AnalysisDTO analysisDTORequest = new AnalysisDTO();
        analysisDTORequest.setAnalysisId(1);

        AnalysisDTO analysisDTOResponse = new DtoRequestProcessor<AnalysisDTO>().process(analysisDTORequest,
                AnalysisDTO.class,
                ControllerType.LOADER,
                ServiceRequestId.URL_ANALYSIS);

        Assert.assertNotEquals(null, analysisDTOResponse);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(analysisDTOResponse));
        Assert.assertNotEquals(null, analysisDTOResponse.getProgram());

    }

    @Test
    public void testFailOnSecondAuthentication() throws Exception {

        Assert.assertTrue(Authenticator.authenticate());
        String oldToken = ClientContext.getInstance(null, false).getUserToken();

        Assert.assertTrue(Authenticator.authenticate());
        String newToken = ClientContext.getInstance(null, false).getUserToken();

        Assert.assertNotEquals(oldToken, newToken);

        AnalysisDTO analysisDTORequest = new AnalysisDTO();
        analysisDTORequest.setAnalysisId(1);


        String url = ResourceBuilder.getRequestUrl(ControllerType.LOADER,
                ClientContext.getInstance(null, false).getCurrentCropContextRoot(),
                ServiceRequestId.URL_ANALYSIS);

        DtoRequestProcessor<AnalysisDTO> dtoDtoRequestProcessor = new DtoRequestProcessor<>();

        AnalysisDTO analysisDTOResponse = dtoDtoRequestProcessor.getTypedHtppResponseForDto(
                ClientContext.getInstance(null, false).getCurrentCropDomain(),
                ClientContext.getInstance(null, false).getCurrentCropPort(),
                ClientContext.getInstance(null, false).getCurrentCropContextRoot(),
                url,
                AnalysisDTO.class,
                analysisDTORequest,
                oldToken);


        Assert.assertNotEquals(null, analysisDTOResponse);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(analysisDTOResponse));
        Assert.assertNotEquals(null, analysisDTOResponse.getProgram());

    }

    private String makeUrl(CropConfig cropConfig) throws Exception {

//        String returnVal = "http://"
//                + cropConfig.getServiceDomain()
//                + ":"
//                + cropConfig.getServicePort().toString()
//                + "/"
//

        String returnVal;

        URL url = new URL("http",
                cropConfig.getServiceDomain(),
                cropConfig.getServicePort(),
                cropConfig.getServiceAppRoot());

        returnVal = url.toString();
        return returnVal;
    }

    @Test
    public void testSwitchToSecondServer() throws Exception {

        TestConfiguration testConfiguration = new TestConfiguration();
        List<CropConfig> activeCropConfigs = testConfiguration.getConfigSettings().getActiveCropConfigs();
        if (activeCropConfigs.size() > 1) {
            // http://localhost:8282/gobii-dev/


            // ****************** FIRST LOGIN
            CropConfig cropConfigOne = activeCropConfigs.get(0);
            String serviceUrlOne = makeUrl(cropConfigOne);
            String defaultCropOne = ClientContext.getInstance(serviceUrlOne,true).getDefaultCropType();

            ClientContext.getInstance(null, false)
                    .setCurrentClientCrop(defaultCropOne);

            SystemUsers systemUsers = new SystemUsers();
            SystemUserDetail userDetail = systemUsers.getDetail(SystemUserNames.USER_READER.toString());
            ClientContext.getInstance(null, false).login(userDetail.getUserName(), userDetail.getPassword());

            // ****************** SECOND LOGIN
            CropConfig cropConfigTwo = activeCropConfigs.get(1);
            String serviceUrlTwo = makeUrl(cropConfigTwo);


            ClientContext.resetConfiguration();
//            String defaultCropTwo = ClientContext.getInstance(serviceUrlTwo,true).getDefaultCropType();
            ClientContext.getInstance(serviceUrlTwo,true);
            String defaultCropTwo = cropConfigTwo.getGobiiCropType();

            ClientContext.getInstance(null, false)
                    .setCurrentClientCrop(defaultCropTwo);

            ClientContext.getInstance(null, false).login(userDetail.getUserName(), userDetail.getPassword());



        }

    }


}
