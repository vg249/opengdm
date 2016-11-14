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
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestDtoFactory;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.headerlesscontainer.MarkerDTO;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class DtoCrudRequestMarkerTest implements DtoCrudRequestTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
        Assert.assertTrue(Authenticator.authenticate());
    }

    @AfterClass
    public static void tearDownUpClass() throws Exception {
        Assert.assertTrue(Authenticator.deAuthenticate());
    }


    @Test
    @Override
    public void get() throws Exception {


//        Integer markerid = (new GlobalPkColl<DtoCrudRequestMarkerTest>().getAPkVal(DtoCrudRequestMarkerTest.class, GobiiEntityNameType.DATASETS));
//
//        RestUri projectsUri = ClientContext.getInstance(null, false)
//                .getUriFactory()
//                .resourceByUriIdParam(ServiceRequestId.URL_DATASETS);
//        projectsUri.setParamValue("id", markerid.toString());
//        RestResource<MarkerDTO> restResourceForProjects = new RestResource<>(projectsUri);
//        PayloadEnvelope<MarkerDTO> resultEnvelope = restResourceForProjects
//                .get(MarkerDTO.class);
//
//        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
//        MarkerDTO markerDTOResponse = resultEnvelope.getPayload().getData().get(0);
//
//        Assert.assertNotEquals(null, markerDTOResponse);
//        Assert.assertNotEquals(null, markerDTOResponse.getDataFile());
//        Assert.assertNotNull(markerDTOResponse.getCallingAnalysisId());
//        Assert.assertTrue(markerDTOResponse.getCallingAnalysisId() > 0);
//        Assert.assertTrue(markerDTOResponse
//                .getAnalysesIds()
//                .stream()
//                .filter(a -> a.equals(null))
//                .toArray().length == 0);


    } //


    @Test
    @Override
    public void create() throws Exception {


        MarkerDTO markerDTORequest = TestDtoFactory
                .makeMarkerDTO("marker 1");

        RestUri markerCollUri = ClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(ServiceRequestId.URL_MARKERS);
        RestResource<MarkerDTO> restResourceForMarkerPost = new RestResource<>(markerCollUri);
        PayloadEnvelope<MarkerDTO> resultEnvelope = restResourceForMarkerPost
                .post(MarkerDTO.class, new PayloadEnvelope<>(markerDTORequest, GobiiProcessType.CREATE));

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
//        MarkerDTO markerDTOResponse = resultEnvelope.getPayload().getData().get(0);
//
//        Assert.assertNotEquals(null, markerDTOResponse);
//        Assert.assertTrue(markerDTOResponse.getMarkerId() > 0);
//        Assert.assertTrue(markerDTOResponse.getCallingAnalysisId() > 0);
//        Assert.assertNotNull(markerDTOResponse.getAnalysesIds());
//        Assert.assertTrue(markerDTOResponse.getAnalysesIds().size() > 0);
//        Assert.assertTrue(markerDTOResponse.getTypeId() > 0);
//
//        GlobalPkValues.getInstance().addPkVal(GobiiEntityNameType.DATASETS,markerDTOResponse.getMarkerId());
//
//
//        RestUri projectsByIdUri = ClientContext.getInstance(null, false)
//                .getUriFactory()
//                .resourceByUriIdParam(ServiceRequestId.URL_DATASETS);
//        RestResource<MarkerDTO> restResourceForMarkerGet = new RestResource<>(projectsByIdUri);
//        restResourceForMarkerGet.setParamValue("id", markerDTOResponse.getMarkerId().toString());
//        resultEnvelope = restResourceForMarkerGet
//                .get(MarkerDTO.class);
//
//        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
//
//        MarkerDTO markerDTOReResponse = resultEnvelope.getPayload().getData().get(0);
//
//        Assert.assertNotEquals(null, markerDTOReResponse);
//        Assert.assertTrue(markerDTOReResponse.getMarkerId() > 0);
//        Assert.assertTrue(markerDTOReResponse.getCallingAnalysisId() > 0);
//        Assert.assertNotNull(markerDTOReResponse.getAnalysesIds());
//        Assert.assertTrue(markerDTOReResponse.getAnalysesIds().size() > 0);
//        Assert.assertTrue(0 == markerDTOReResponse
//                .getAnalysesIds()
//                .stream()
//                .filter(a -> a.equals(null))
//                .count());

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
//        Assert.assertNotNull(markerDTOList.get(0).getName());
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
//            Assert.assertTrue(currentMarkerDto.getName().equals(markerDTOFromLink.getName()));
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
