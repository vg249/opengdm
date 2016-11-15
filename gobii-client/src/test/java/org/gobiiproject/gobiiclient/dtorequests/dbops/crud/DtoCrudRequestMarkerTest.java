// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests.dbops.crud;


import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.RestUri;
import org.gobiiproject.gobiiapimodel.types.ServiceRequestId;
import org.gobiiproject.gobiiclient.core.ClientContext;
import org.gobiiproject.gobiiclient.core.restmethods.RestResource;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.Authenticator;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.GlobalPkColl;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.GlobalPkValues;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestDtoFactory;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.headerlesscontainer.MarkerDTO;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.UUID;

public class DtoCrudRequestMarkerTest implements DtoCrudRequestTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
        Assert.assertTrue(Authenticator.authenticate());
    }

    @AfterClass
    public static void tearDownUpClass() throws Exception {
        Assert.assertTrue(Authenticator.deAuthenticate());
    }


    private MarkerDTO getArbitraryMarkerDTO() throws Exception {

        MarkerDTO returnVal;

        Integer markerId = (new GlobalPkColl<DtoCrudRequestMarkerTest>().getAPkVal(DtoCrudRequestMarkerTest.class,
                GobiiEntityNameType.MARKERS));

        RestUri projectsUri = ClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(ServiceRequestId.URL_MARKERS);
        projectsUri.setParamValue("id", markerId.toString());
        RestResource<MarkerDTO> restResourceForProjects = new RestResource<>(projectsUri);
        PayloadEnvelope<MarkerDTO> resultEnvelope = restResourceForProjects
                .get(MarkerDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        returnVal = resultEnvelope.getPayload().getData().get(0);

        return returnVal;

    }

    @Test
    @Override
    public void get() throws Exception {


        MarkerDTO arbitraryMarkerDTO = this.getArbitraryMarkerDTO();

        Assert.assertNotEquals(null, arbitraryMarkerDTO);
        Assert.assertNotNull(arbitraryMarkerDTO.getMarkerName());
        Assert.assertNotNull(arbitraryMarkerDTO.getPlatformId());
        Assert.assertTrue(arbitraryMarkerDTO.getPlatformId() > 0);
        Assert.assertTrue(arbitraryMarkerDTO.getMarkerId() > 0);

    } //


    @Test
    @Override
    public void create() throws Exception {


        MarkerDTO markerDTORequest = TestDtoFactory
                .makeMarkerDTO("testmarker");

        RestUri markerCollUri = ClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(ServiceRequestId.URL_MARKERS);
        RestResource<MarkerDTO> restResourceForMarkerPost = new RestResource<>(markerCollUri);
        PayloadEnvelope<MarkerDTO> resultEnvelope = restResourceForMarkerPost
                .post(MarkerDTO.class, new PayloadEnvelope<>(markerDTORequest, GobiiProcessType.CREATE));

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        MarkerDTO markerDTOResponse = resultEnvelope.getPayload().getData().get(0);

        Assert.assertNotEquals(null, markerDTOResponse);
        Assert.assertNotNull(markerDTOResponse.getMarkerName());
        Assert.assertNotNull(markerDTOResponse.getPlatformId());
        Assert.assertTrue(markerDTOResponse.getPlatformId() > 0);

        Assert.assertTrue(markerDTOResponse.getMarkerId() > 0);
        GlobalPkValues.getInstance().addPkVal(GobiiEntityNameType.MARKERS,markerDTOResponse.getMarkerId());

    }

    @Test
    public void getMarkerByName() throws Exception {

        MarkerDTO arbitraryMarkerDTO = this.getArbitraryMarkerDTO();
        String arbitaryMarkerName = arbitraryMarkerDTO.getMarkerName();
        RestUri restUriContact = ClientContext.getInstance(null,false)
                .getUriFactory()
                .markerssByQueryParams();
        restUriContact.setParamValue("name", arbitaryMarkerName);

        RestResource<MarkerDTO> restResourceForProjects = new RestResource<>(restUriContact);
        PayloadEnvelope<MarkerDTO> resultEnvelope = restResourceForProjects
                .get(MarkerDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        MarkerDTO markerDTOResponse = resultEnvelope.getPayload().getData().get(0);

        Assert.assertNotEquals(null, arbitraryMarkerDTO);
        Assert.assertNotNull(markerDTOResponse.getMarkerName());
        Assert.assertNotNull(markerDTOResponse.getPlatformId());
        Assert.assertTrue(markerDTOResponse.getPlatformId() > 0);

        Assert.assertTrue(markerDTOResponse.getMarkerId() > 0);


    }

    @Test
    @Override
    public void update() throws Exception {

        // ******** make analyses we'll need for the new data set
//        EntityParamValues entityParamValues = TestDtoFactory.makeArbitraryEntityParams();
//        DtoRequestAnalysis dtoRequestAnalysis = new DtoRequestAnalysis();
//        AnalysisDTO analysisDTORequest = TestDtoFactory
//                .makePopulatedAnalysisDTO(GobiiProcessType.CREATE, 1, entityParamValues);
//
//        AnalysisDTO newCallingAnalysisDTO = dtoRequestAnalysis.process(analysisDTORequest);
//        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(newCallingAnalysisDTO));
//
//        List<AnalysisDTO> analysesToCreate = new ArrayList<>();
//        List<AnalysisDTO> analysesNew = new ArrayList<>();
//        analysesToCreate.add(TestDtoFactory
//                .makePopulatedAnalysisDTO(GobiiProcessType.CREATE,
//                        2,
//                        entityParamValues));
//        analysesToCreate.add(TestDtoFactory
//                .makePopulatedAnalysisDTO(GobiiProcessType.CREATE,
//                        3,
//                        entityParamValues));
//        analysesToCreate.add(TestDtoFactory
//                .makePopulatedAnalysisDTO(GobiiProcessType.CREATE,
//                        4,
//                        entityParamValues));
//
//        List<Integer> analysisIds = new ArrayList<>();
//        for (AnalysisDTO currentAnalysis : analysesToCreate) {
//            AnalysisDTO newAnalysis = dtoRequestAnalysis.process(currentAnalysis);
//            Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(newAnalysis));
//            analysesNew.add(newAnalysis);
//            analysisIds.add(newAnalysis.getAnalysisId());
//        }
//
//
//        // create a new aataSet for our test
//        MarkerDTO newMarkerDto = TestDtoFactory
//                .makeMarkerDTO(1,
//                        newCallingAnalysisDTO.getAnalysisId(),
//                        analysisIds);
//
//        RestUri projectsCollUri = ClientContext.getInstance(null, false)
//                .getUriFactory()
//                .resourceColl(ServiceRequestId.URL_DATASETS);
//        RestResource<MarkerDTO> restResourceForMarkerPost = new RestResource<>(projectsCollUri);
//        PayloadEnvelope<MarkerDTO> resultEnvelope = restResourceForMarkerPost
//                .post(MarkerDTO.class, new PayloadEnvelope<>(newMarkerDto, GobiiProcessType.CREATE));
//
//
//        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
//
//
//        MarkerDTO newMarkerDTOResponse = resultEnvelope.getPayload().getData().get(0);
//
//        RestUri projectsByIdUri = ClientContext.getInstance(null, false)
//                .getUriFactory()
//                .resourceByUriIdParam(ServiceRequestId.URL_DATASETS);
//        RestResource<MarkerDTO> restResourceForMarkerById = new RestResource<>(projectsByIdUri);
//        restResourceForMarkerById.setParamValue("id", newMarkerDTOResponse.getMarkerId().toString());
//        resultEnvelope = restResourceForMarkerById
//                .get(MarkerDTO.class);
//
//        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
//
//        MarkerDTO markerDTOReceived = resultEnvelope.getPayload().getData().get(0);
//
//        // so this would be the typical workflow for the client app
//        String newDataFile = UUID.randomUUID().toString();
//        markerDTOReceived.setDataFile(newDataFile);
//        Integer anlysisIdRemovedFromList = markerDTOReceived.getAnalysesIds().remove(0);
//        Integer newCallingAnalysisId = anlysisIdRemovedFromList;
//        markerDTOReceived.setCallingAnalysisId(newCallingAnalysisId);
//
//
//        resultEnvelope = restResourceForMarkerById
//                .put(MarkerDTO.class, new PayloadEnvelope<>(markerDTOReceived, GobiiProcessType.UPDATE));
//
//        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
//
//        MarkerDTO markerDTOResponse = resultEnvelope.getPayload().getData().get(0);
//
//        restResourceForMarkerById.setParamValue("id", markerDTOResponse.getMarkerId().toString());
//        resultEnvelope = restResourceForMarkerById
//                .get(MarkerDTO.class);
//        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
//        MarkerDTO dtoRequestMarkerReRetrieved = resultEnvelope.getPayload().getData().get(0);
//
//        Assert.assertTrue(dtoRequestMarkerReRetrieved.getMarkerId().equals(markerDTOReceived.getMarkerId()));
//        Assert.assertTrue(dtoRequestMarkerReRetrieved.getDataFile().equals(newDataFile));
//        Assert.assertTrue(dtoRequestMarkerReRetrieved.getCallingAnalysisId().equals(newCallingAnalysisId));
//        Assert.assertTrue(dtoRequestMarkerReRetrieved
//                .getAnalysesIds()
//                .stream()
//                .filter(a -> a.equals(anlysisIdRemovedFromList))
//                .toArray().length == 0);
    }


    @Test
    @Override
    public void getList() throws Exception {

//        RestUri restUriMarker = ClientContext.getInstance(null, false)
//                .getUriFactory().resourceColl(ServiceRequestId.URL_DATASETS);
//        RestResource<MarkerDTO> restResource = new RestResource<>(restUriMarker);
//        PayloadEnvelope<MarkerDTO> resultEnvelope = restResource
//                .get(MarkerDTO.class);
//
//        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
//        List<MarkerDTO> markerDTOList = resultEnvelope.getPayload().getData();
//        Assert.assertNotNull(markerDTOList);
//        Assert.assertTrue(markerDTOList.size() > 0);
//        Assert.assertNotNull(markerDTOList.get(0).getMarkerName());
//
//
//        LinkCollection linkCollection = resultEnvelope.getPayload().getLinkCollection();
//        Assert.assertTrue(linkCollection.getLinksPerDataItem().size() == markerDTOList.size());
//
//        List<Integer> itemsToTest = new ArrayList<>();
//        if (markerDTOList.size() > 50) {
//            itemsToTest = TestUtils.makeListOfIntegersInRange(10, markerDTOList.size());
//
//        } else {
//            for (int idx = 0; idx < markerDTOList.size(); idx++) {
//                itemsToTest.add(idx);
//            }
//        }
//
//        for (Integer currentIdx : itemsToTest) {
//            MarkerDTO currentMarkerDto = markerDTOList.get(currentIdx);
//
//            Link currentLink = linkCollection.getLinksPerDataItem().get(currentIdx);
//
//            RestUri restUriMarkerForGetById = ClientContext.getInstance(null, false)
//                    .getUriFactory()
//                    .RestUriFromUri(currentLink.getHref());
//            RestResource<MarkerDTO> restResourceForGetById = new RestResource<>(restUriMarkerForGetById);
//            PayloadEnvelope<MarkerDTO> resultEnvelopeForGetByID = restResourceForGetById
//                    .get(MarkerDTO.class);
//            Assert.assertNotNull(resultEnvelopeForGetByID);
//            Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForGetByID.getHeader()));
//            MarkerDTO markerDTOFromLink = resultEnvelopeForGetByID.getPayload().getData().get(0);
//            Assert.assertTrue(currentMarkerDto.getMarkerName().equals(markerDTOFromLink.getMarkerName()));
//            Assert.assertTrue(currentMarkerDto.getMarkerId().equals(markerDTOFromLink.getMarkerId()));
//
//            Assert.assertNotNull(markerDTOFromLink.getAnalysesIds());
//            for (Integer currentAnalysisId : markerDTOFromLink.getAnalysesIds()) {
//                DtoRequestAnalysis dtoRequestAnalysis = new DtoRequestAnalysis();
//                AnalysisDTO analysisDTORequest = new AnalysisDTO();
//                analysisDTORequest.setAnalysisId(currentAnalysisId);
//                AnalysisDTO analysisDTOResponse = dtoRequestAnalysis.process(analysisDTORequest);
//
//                Assert.assertNotEquals(null, analysisDTOResponse);
//                Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(analysisDTOResponse));
//                Assert.assertNotEquals(null, analysisDTOResponse.getProgram());
//            }
//
//        }

    }
}
