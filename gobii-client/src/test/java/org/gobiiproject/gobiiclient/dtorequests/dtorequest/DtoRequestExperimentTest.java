// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests.dtorequest;

import org.gobiiproject.gobiiapimodel.hateos.Link;
import org.gobiiproject.gobiiapimodel.hateos.LinkCollection;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.RestUri;
import org.gobiiproject.gobiiapimodel.restresources.UriFactory;
import org.gobiiproject.gobiiapimodel.types.ServiceRequestId;
import org.gobiiproject.gobiiclient.core.ClientContext;
import org.gobiiproject.gobiiclient.core.restmethods.RestResource;
import org.gobiiproject.gobiiclient.dtorequests.DtoRequestManifest;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.Authenticator;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.GlobalPkValues;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.headerlesscontainer.ExperimentDTO;
import org.gobiiproject.gobiimodel.tobemovedtoapimodel.HeaderStatusMessage;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class DtoRequestExperimentTest {

    private static UriFactory uriFactory;

    @BeforeClass
    public static void setUpClass() throws Exception {
        Assert.assertTrue(Authenticator.authenticate());
        String currentCropContextRoot = ClientContext.getInstance(null, false).getCurrentCropContextRoot();
        uriFactory = new UriFactory(currentCropContextRoot);
    }

    @AfterClass
    public static void tearDownUpClass() throws Exception {
        Assert.assertTrue(Authenticator.deAuthenticate());
    }


    @Test
    public void testGetExperimentDetailsByExperimentId() throws Exception {
        
        //DtoRequestExperiment dtoRequestExperiment = new DtoRequestExperiment();

//        ExperimentDTO experimentDTO = new ExperimentDTO();
//        experimentDTO.setExperimentId(2);
//        dtoRequestExperiment.process(experimentDTO);

        RestUri experimentsUri = uriFactory
                .resourceByUriIdParam(ServiceRequestId.URL_EXPERIMENTS);
        experimentsUri.setParamValue("id", "2");
        RestResource<ExperimentDTO> restResourceForExperiments = new RestResource<>(experimentsUri);
        PayloadEnvelope<ExperimentDTO> resultEnvelope = restResourceForExperiments
                .get(ExperimentDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        ExperimentDTO experimentDTOResponse = resultEnvelope.getPayload().getData().get(0);

//        ExperimentDTO experimentDTOResponse = dtoRequestExperiment.process(experimentDTO);
//
//        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(experimentDTOResponse));
        Assert.assertNotEquals(experimentDTOResponse, null);
        Assert.assertTrue(experimentDTOResponse.getExperimentId() > 0);
        Assert.assertNotNull(experimentDTOResponse.getPlatformName());

    } // testGetMarkers()

    @Test
    public void testCreateExperiment() throws Exception {

        //DtoRequestExperiment dtoRequestExperiment = new DtoRequestExperiment();

        Integer projectId = GlobalPkValues.getInstance().getAPkVal(GobiiEntityNameType.PROJECTS);
        if(projectId == null ) {
            new DtoRequestProjectTest().testCreateProject();
            projectId = GlobalPkValues.getInstance().getAPkVal(GobiiEntityNameType.PROJECTS);
        }

        Integer platformId = GlobalPkValues
                .getInstance()
                .getAPkVal(GobiiEntityNameType.PLATFORMS);
        if( platformId == null ) {
            new DtoRequestPlatformTest().testCreatePlatformWithHttpPost();
            platformId = GlobalPkValues
                    .getInstance()
                    .getAPkVal(GobiiEntityNameType.PLATFORMS);
        }

        Integer manifestId = GlobalPkValues.getInstance().getAPkVal(GobiiEntityNameType.MANIFESTS);
        if(manifestId == null ) {
            new DtoRequestManifesTest().testCreateManifest();
             manifestId = GlobalPkValues.getInstance().getAPkVal(GobiiEntityNameType.MANIFESTS);
        }

        ExperimentDTO experimentDTORequest = new ExperimentDTO();
        // experimentDTORequest.setExperimentId(1);
        experimentDTORequest.setManifestId(manifestId);
        experimentDTORequest.setPlatformId(platformId);
        experimentDTORequest.setProjectId(projectId);
        experimentDTORequest.setCreatedBy(1);
        experimentDTORequest.setModifiedBy(1);
        experimentDTORequest.setExperimentCode("foocode");
        experimentDTORequest.setExperimentDataFile("foofile");
        experimentDTORequest.setStatusId(1);
        experimentDTORequest.setExperimentName(UUID.randomUUID().toString());

        //dtoRequestExperiment.process(experimentDTORequest);

        RestUri experimentsUri = uriFactory.resourceColl(ServiceRequestId.URL_EXPERIMENTS);
        RestResource<ExperimentDTO> restResourceForExperiments = new RestResource<>(experimentsUri);
        PayloadEnvelope<ExperimentDTO> payloadEnvelope = new PayloadEnvelope<>(experimentDTORequest, GobiiProcessType.CREATE);
        PayloadEnvelope<ExperimentDTO> resultEnvelope = restResourceForExperiments
                .post(ExperimentDTO.class, payloadEnvelope);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));


        //ExperimentDTO experimentDTOResponse = dtoRequestExperiment.process(experimentDTORequest);
        ExperimentDTO experimentDTOResponse = resultEnvelope.getPayload().getData().get(0);

        Assert.assertNotEquals(experimentDTOResponse, null);
        Assert.assertTrue(experimentDTOResponse.getExperimentId() > 0);

    } // testGetMarkers()

    @Test
    public void testCreateExistingExperiment() throws Exception {

//        DtoRequestExperiment dtoRequestExperiment = new DtoRequestExperiment();
//        ExperimentDTO experimentDTORequest = new ExperimentDTO();
//        experimentDTORequest.setExperimentId(2);
//        ExperimentDTO ExperimentDTOExisting = dtoRequestExperiment.process(experimentDTORequest);

        RestUri experimentsUriById = uriFactory
                .resourceByUriIdParam(ServiceRequestId.URL_EXPERIMENTS);
        experimentsUriById.setParamValue("id", "2");
        RestResource<ExperimentDTO> restResourceForExperiments = new RestResource<>(experimentsUriById);
        PayloadEnvelope<ExperimentDTO> resultEnvelope = restResourceForExperiments
                .get(ExperimentDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        ExperimentDTO experimentDTOExisting = resultEnvelope.getPayload().getData().get(0);


        RestUri experimentCollUri = uriFactory.resourceColl(ServiceRequestId.URL_EXPERIMENTS);
        RestResource<ExperimentDTO> restResourceForProjectPost =
                new RestResource<>(experimentCollUri);
        PayloadEnvelope<ExperimentDTO> payloadEnvelope = new PayloadEnvelope<>(experimentDTOExisting,
                GobiiProcessType.CREATE);
        resultEnvelope = restResourceForProjectPost
                .post(ExperimentDTO.class, payloadEnvelope);

        //ExperimentDTO ExperimentDTOResponse = dtoRequestExperiment.process(ExperimentDTOExisting);
        List<HeaderStatusMessage> headerStatusMessages = resultEnvelope
                .getHeader()
                .getStatus()
                .getStatusMessages()
                .stream()
                .filter(m -> m.getGobiiValidationStatusType().equals(GobiiValidationStatusType.VALIDATION_COMPOUND_UNIQUE))
                .collect(Collectors.toList());


        Assert.assertNotNull(headerStatusMessages);
        Assert.assertTrue(headerStatusMessages.size() > 0);
        HeaderStatusMessage headerStatusMessageValidation = headerStatusMessages.get(0);
        Assert.assertTrue(headerStatusMessageValidation.getMessage().toLowerCase().contains("name"));
        Assert.assertTrue(headerStatusMessageValidation.getMessage().toLowerCase().contains("project"));
        Assert.assertTrue(headerStatusMessageValidation.getMessage().toLowerCase().contains("platform"));


    } // testCreateExperiment()

    @Test
    public void testUpdateExperiment() throws Exception {

//        DtoRequestExperiment dtoRequestExperiment = new DtoRequestExperiment();
//        ExperimentDTO experimentDTORequest = new ExperimentDTO();
//        experimentDTORequest.setExperimentId(2);
//        ExperimentDTO experimentDTOReceived = dtoRequestExperiment.process(experimentDTORequest);

        RestUri experimentsUriById = uriFactory
                .resourceByUriIdParam(ServiceRequestId.URL_EXPERIMENTS);
        experimentsUriById.setParamValue("id", "2");
        RestResource<ExperimentDTO> restResourceForExperimentsById = new RestResource<>(experimentsUriById);
        PayloadEnvelope<ExperimentDTO> resultEnvelope = restResourceForExperimentsById
                .get(ExperimentDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        ExperimentDTO experimentDTOReceived = resultEnvelope.getPayload().getData().get(0);

        String newDataFile = UUID.randomUUID().toString();

        experimentDTOReceived.setExperimentDataFile(newDataFile);


        PayloadEnvelope<ExperimentDTO> postRequestEnvelope = new PayloadEnvelope<>(experimentDTOReceived,GobiiProcessType.UPDATE);
        resultEnvelope = restResourceForExperimentsById
                .put(ExperimentDTO.class,postRequestEnvelope);

        // ExperimentDTO experimentDTOResponse = dtoRequestExperiment.process(experimentDTOReceived);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));



        ExperimentDTO dtoRequestExperimentExperimentReRetrieved = resultEnvelope.getPayload().getData().get(0);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));

        Assert.assertTrue(dtoRequestExperimentExperimentReRetrieved.getExperimentDataFile().equals(newDataFile));

    }

    @Test
    public void getExperiments() throws Exception {

        RestUri restUriExperiment = DtoRequestExperimentTest.uriFactory.resourceColl(ServiceRequestId.URL_EXPERIMENTS);
        RestResource<ExperimentDTO> restResource = new RestResource<>(restUriExperiment);
        PayloadEnvelope<ExperimentDTO> resultEnvelope = restResource
                .get(ExperimentDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        List<ExperimentDTO> experimentDTOList = resultEnvelope.getPayload().getData();
        Assert.assertNotNull(experimentDTOList);
        Assert.assertTrue(experimentDTOList.size() > 0);
        Assert.assertNotNull(experimentDTOList.get(0).getExperimentName());


        LinkCollection linkCollection = resultEnvelope.getPayload().getLinkCollection();
        Assert.assertTrue(linkCollection.getLinksPerDataItem().size() == experimentDTOList.size() );

        List<Integer> itemsToTest = new ArrayList<>();
        if (experimentDTOList.size() > 50) {
            itemsToTest = TestUtils.makeListOfIntegersInRange(10, experimentDTOList.size());

        } else {
            for (int idx = 0; idx < experimentDTOList.size(); idx++) {
                itemsToTest.add(idx);
            }
        }

        for (Integer currentIdx : itemsToTest) {
            ExperimentDTO currentExperimentDto = experimentDTOList.get(currentIdx);

            Link currentLink = linkCollection.getLinksPerDataItem().get(currentIdx);

            RestUri restUriExperimentForGetById = DtoRequestExperimentTest
                    .uriFactory
                    .RestUriFromUri(currentLink.getHref());
            RestResource<ExperimentDTO> restResourceForGetById = new RestResource<>(restUriExperimentForGetById);
            PayloadEnvelope<ExperimentDTO> resultEnvelopeForGetByID = restResourceForGetById
                    .get(ExperimentDTO.class);
            Assert.assertNotNull(resultEnvelopeForGetByID);
            Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForGetByID.getHeader()));
            ExperimentDTO experimentDTOFromLink = resultEnvelopeForGetByID.getPayload().getData().get(0);
            Assert.assertTrue(currentExperimentDto.getExperimentName().equals(experimentDTOFromLink.getExperimentName()));
            Assert.assertTrue(currentExperimentDto.getExperimentId().equals(experimentDTOFromLink.getExperimentId()));
        }

    }

}
