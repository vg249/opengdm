// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests;

import org.gobiiproject.gobiiapimodel.hateos.Link;
import org.gobiiproject.gobiiapimodel.hateos.LinkCollection;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.RestUri;
import org.gobiiproject.gobiiapimodel.restresources.UriFactory;
import org.gobiiproject.gobiiapimodel.types.ServiceRequestId;
import org.gobiiproject.gobiiclient.core.ClientContext;
import org.gobiiproject.gobiiclient.core.restmethods.RestResource;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.Authenticator;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.dto.container.NameIdDTO;
import org.gobiiproject.gobiimodel.dto.container.NameIdListDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.OrganizationDTO;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

public class DtoRequestNameIdListTest {

    private static UriFactory uriFactory;

    @BeforeClass
    public static void setUpClass() throws Exception {
        Assert.assertTrue(Authenticator.authenticate());
        String currentCropContextRoot = ClientContext.getInstance(null, false).getCurrentCropContextRoot();
        DtoRequestNameIdListTest.uriFactory = new UriFactory(currentCropContextRoot);

    }

    @AfterClass
    public static void tearDownUpClass() throws Exception {
        Assert.assertTrue(Authenticator.deAuthenticate());
    }


    @Test
    public void testGetAnalysisNames() throws Exception {

        // Assumes rice data with seed script is loaded
//        NameIdListDTO nameIdListDTORequest = new NameIdListDTO();
//        nameIdListDTORequest.setEntityName("analysis");
//        DtoRequestNameIdList dtoRequestNameIdList = new DtoRequestNameIdList();
//        NameIdListDTO nameIdListDtoResponse = dtoRequestNameIdList.process(nameIdListDTORequest);

        RestUri namesUri = uriFactory.nameIdList();
        RestResource<NameIdDTO> restResource = new RestResource<>(namesUri);
        namesUri.setParamValue("entity", "analysis");
        PayloadEnvelope<NameIdDTO> resultEnvelope = restResource
                .get(NameIdDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        List<NameIdDTO> NameIdDTOList = resultEnvelope.getPayload().getData();
        Assert.assertNotNull(NameIdDTOList);
        Assert.assertTrue(NameIdDTOList.size() > 0);
        Assert.assertNotNull(NameIdDTOList.get(0).getName());


//        LinkCollection linkCollection = resultEnvelope.getPayload().getLinkCollection();
//        for(int currentItemIdx = 0; currentItemIdx < NameIdDTOList.size(); currentItemIdx++  ) {
//            NameIdDTO currentNameIdDTO = NameIdDTOList.get(currentItemIdx);
//
//            Link currentLink = linkCollection.getLinksPerDataItem().get(currentItemIdx);
//
//            RestUri restUriAnalysisForGetById = DtoRequestAnalysisTest
//                    .uriFactory
//                    .RestUriFromUri(currentLink.getHref());
//            RestResource<NameIdDTO> restResourceForGetById = new RestResource<>(restUriAnalysisForGetById);
//            PayloadEnvelope<NameIdDTO> resultEnvelopeForGetByID = restResourceForGetById
//                    .get(NameIdDTO.class);
//            Assert.assertNotNull(resultEnvelopeForGetByID);
//            Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForGetByID.getHeader()));
//            NameIdDTO NameIdDTOFromLink = resultEnvelopeForGetByID.getPayload().getData().get(0);
//            Assert.assertTrue(currentNameIdDTO.getName().equals(NameIdDTOFromLink.getName()));
//            Assert.assertTrue(currentNameIdDTO.getAnalysisId().equals(NameIdDTOFromLink.getAnalysisId()));
//        }


    } // testGetAnalysisNames()

    @Test
    public void testGetAnalysisNamesByTypeId() throws Exception {

        // Assumes rice data with seed script is loaded
        NameIdListDTO nameIdListDTORequest = new NameIdListDTO();
        nameIdListDTORequest.setEntityName("analysisNameByTypeId");
        nameIdListDTORequest.setFilter("33");
        DtoRequestNameIdList dtoRequestNameIdList = new DtoRequestNameIdList();
        NameIdListDTO nameIdListDtoResponse = dtoRequestNameIdList.process(nameIdListDTORequest);


        Assert.assertNotEquals(null, nameIdListDtoResponse);
        Assert.assertEquals(true, nameIdListDtoResponse.getStatus().isSucceeded());
        Assert.assertTrue(nameIdListDtoResponse.getNamesById().size() >= 0);
        Assert.assertEquals(true, TestUtils.isNameIdListSorted(nameIdListDtoResponse.getNamesById()));
    }


    @Test
    public void testGetContactsByIdForContactType() throws Exception {

        // Assumes rice data with seed script is loaded
        DtoRequestNameIdList dtoRequestNameIdList = new DtoRequestNameIdList();
        NameIdListDTO nameIdListDTOReuest = new NameIdListDTO();
        nameIdListDTOReuest.setFilter("Curator");
        nameIdListDTOReuest.setEntityName("contact");

        NameIdListDTO nameIdListDtoResponse = dtoRequestNameIdList.process(nameIdListDTOReuest);


        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(nameIdListDtoResponse));
        Assert.assertNotEquals(null, nameIdListDtoResponse);
        Assert.assertEquals(true, nameIdListDtoResponse.getStatus().isSucceeded());
        Assert.assertTrue(nameIdListDtoResponse.getNamesById().size() >= 0);
        Assert.assertEquals(true, TestUtils.isNameIdListSorted(nameIdListDtoResponse.getNamesById()));

    } // testGetMarkers()

    @Test
    public void testGetContactNames() throws Exception {

        // Assumes rice data with seed script is loaded
        DtoRequestNameIdList dtoRequestNameIdList = new DtoRequestNameIdList();
        NameIdListDTO nameIdListDTORequest = new NameIdListDTO();
        nameIdListDTORequest.setEntityName("allContacts");

        NameIdListDTO nameIdListDtoResponse = dtoRequestNameIdList.process(nameIdListDTORequest);


        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(nameIdListDtoResponse));
        Assert.assertNotEquals(null, nameIdListDtoResponse);
        Assert.assertEquals(true, nameIdListDtoResponse.getStatus().isSucceeded());
        Assert.assertTrue(nameIdListDtoResponse.getNamesById().size() >= 0);
        Assert.assertEquals(true, TestUtils.isNameIdListSorted(nameIdListDtoResponse.getNamesById()));

    } // testGetMarkers()

    @Test
    public void testGetProjectNamesByContactId() throws Exception {


        // Assumes rice data with seed script is loaded
        DtoRequestNameIdList dtoRequestNameIdList = new DtoRequestNameIdList();
        NameIdListDTO nameIdListDTORequest = new NameIdListDTO();
        nameIdListDTORequest.setEntityName("project");
        nameIdListDTORequest.setFilter("2");
        NameIdListDTO nameIdListDtoResponse = dtoRequestNameIdList.process(nameIdListDTORequest);


        Assert.assertNotEquals(null, nameIdListDtoResponse);
        Assert.assertEquals(true, nameIdListDtoResponse.getStatus().isSucceeded());
        Assert.assertTrue(nameIdListDtoResponse.getNamesById().size() >= 0);
        Assert.assertEquals(true, TestUtils.isNameIdListSorted(nameIdListDtoResponse.getNamesById()));

    }

    @Test
    public void testGetExperimentNamesByProjectId() throws Exception {


        // Assumes rice data with seed script is loaded
        DtoRequestNameIdList dtoRequestNameIdList = new DtoRequestNameIdList();
        NameIdListDTO nameIdListDTORequest = new NameIdListDTO();
        nameIdListDTORequest.setEntityName("experiment");
        nameIdListDTORequest.setFilter("1");
        NameIdListDTO nameIdListDtoResponse = dtoRequestNameIdList.process(nameIdListDTORequest);

        Assert.assertNotEquals(null, nameIdListDtoResponse);
        Assert.assertEquals(true, nameIdListDtoResponse.getStatus().isSucceeded());
        Assert.assertTrue(nameIdListDtoResponse.getNamesById().size() >= 0);
        Assert.assertEquals(true, TestUtils.isNameIdListSorted(nameIdListDtoResponse.getNamesById()));
    }

    @Test
    public void testGetExperimentNames() throws Exception {

        // Assumes rice data with seed script is loaded
        DtoRequestNameIdList dtoRequestNameIdList = new DtoRequestNameIdList();
        NameIdListDTO nameIdListDTORequest = new NameIdListDTO();
        nameIdListDTORequest.setEntityName("experimentnames");
        NameIdListDTO nameIdListDtoResponse = dtoRequestNameIdList.process(nameIdListDTORequest);

        Assert.assertNotEquals(null, nameIdListDtoResponse);
        Assert.assertEquals(true, nameIdListDtoResponse.getStatus().isSucceeded());
        Assert.assertTrue(nameIdListDtoResponse.getNamesById().size() >= 0);
        Assert.assertEquals(true, TestUtils.isNameIdListSorted(nameIdListDtoResponse.getNamesById()));
    }

    @Test
    public void testGetCvTermsByGroup() throws Exception {

        // Assumes rice data with seed script is loaded
        DtoRequestNameIdList dtoRequestNameIdList = new DtoRequestNameIdList();
        NameIdListDTO nameIdListDTORequest = new NameIdListDTO();
        nameIdListDTORequest.setEntityName("cvgroupterms");
        nameIdListDTORequest.setFilter("map_type");
        NameIdListDTO nameIdListDtoResponse = dtoRequestNameIdList.process(nameIdListDTORequest);

        Assert.assertNotEquals(null, nameIdListDtoResponse);
        Assert.assertEquals(true, nameIdListDtoResponse.getStatus().isSucceeded());
        Assert.assertTrue(nameIdListDtoResponse.getNamesById().size() >= 0);
        Assert.assertEquals(true, TestUtils.isNameIdListSorted(nameIdListDtoResponse.getNamesById()));

    }

    @Test
    public void testGetPlatformNames() throws Exception {

        // Assumes rice data with seed script is loaded
        NameIdListDTO nameIdListDTORequest = new NameIdListDTO();
        nameIdListDTORequest.setEntityName("platform");
        DtoRequestNameIdList dtoRequestNameIdList = new DtoRequestNameIdList();
        NameIdListDTO nameIdListDtoResponse = dtoRequestNameIdList.process(nameIdListDTORequest);


        Assert.assertNotEquals(null, nameIdListDtoResponse);
        Assert.assertEquals(true, nameIdListDtoResponse.getStatus().isSucceeded());
        Assert.assertTrue(nameIdListDtoResponse.getNamesById().size() >= 0);
        Assert.assertEquals(true, TestUtils.isNameIdListSorted(nameIdListDtoResponse.getNamesById()));

    } // testGetMarkers()

    @Test
    public void testGetPlatformNamesByTypeId() throws Exception {
        // Assumes rice data with seed script is loaded
        NameIdListDTO nameIdListDTORequest = new NameIdListDTO();
        nameIdListDTORequest.setEntityName("platformByTypeId");
        nameIdListDTORequest.setFilter("1");
        DtoRequestNameIdList dtoRequestNameIdList = new DtoRequestNameIdList();
        NameIdListDTO nameIdListDtoResponse = dtoRequestNameIdList.process(nameIdListDTORequest);


        Assert.assertNotEquals(null, nameIdListDtoResponse);
        Assert.assertEquals(true, nameIdListDtoResponse.getStatus().isSucceeded());
        Assert.assertTrue(nameIdListDtoResponse.getNamesById().size() >= 0);
        Assert.assertEquals(true, TestUtils.isNameIdListSorted(nameIdListDtoResponse.getNamesById()));

    } // testGetMarkers()

    @Test
    public void testGetProjectNames() throws Exception {


        // Assumes rice data with seed script is loaded
        NameIdListDTO nameIdListDTORequest = new NameIdListDTO();
        nameIdListDTORequest.setEntityName("projectnames");
        DtoRequestNameIdList dtoRequestNameIdList = new DtoRequestNameIdList();
        NameIdListDTO nameIdListDtoResponse = dtoRequestNameIdList.process(nameIdListDTORequest);


        Assert.assertNotEquals(null, nameIdListDtoResponse);
        Assert.assertEquals(true, nameIdListDtoResponse.getStatus().isSucceeded());
        Assert.assertTrue(nameIdListDtoResponse.getNamesById().size() >= 0);
        Assert.assertEquals(true, TestUtils.isNameIdListSorted(nameIdListDtoResponse.getNamesById()));

    } // testGetMarkers()

    @Test
    public void testGetMarkerGroupNames() throws Exception {


        // Assumes rice data with seed script is loaded
        NameIdListDTO nameIdListDTORequest = new NameIdListDTO();
        nameIdListDTORequest.setEntityName("markergroup");
        DtoRequestNameIdList dtoRequestNameIdList = new DtoRequestNameIdList();
        NameIdListDTO nameIdListDtoResponse = dtoRequestNameIdList.process(nameIdListDTORequest);


        Assert.assertNotEquals(null, nameIdListDtoResponse);
        Assert.assertEquals(true, nameIdListDtoResponse.getStatus().isSucceeded());
        Assert.assertTrue(nameIdListDtoResponse.getNamesById().size() >= 0);
        Assert.assertEquals(true, TestUtils.isNameIdListSorted(nameIdListDtoResponse.getNamesById()));

    } // testGetMarkerGroupNames()

    @Test
    public void testGetReferenceNames() throws Exception {


        // Assumes rice data with seed script is loaded
        NameIdListDTO nameIdListDTORequest = new NameIdListDTO();
        nameIdListDTORequest.setEntityName("reference");
        DtoRequestNameIdList dtoRequestNameIdList = new DtoRequestNameIdList();
        NameIdListDTO nameIdListDtoResponse = dtoRequestNameIdList.process(nameIdListDTORequest);


        Assert.assertNotEquals(null, nameIdListDtoResponse);
        Assert.assertEquals(true, nameIdListDtoResponse.getStatus().isSucceeded());
        Assert.assertTrue(nameIdListDtoResponse.getNamesById().size() >= 0);
        Assert.assertEquals(true, TestUtils.isNameIdListSorted(nameIdListDtoResponse.getNamesById()));

    } // testGetMarkers()

    @Test
    public void testGetMapNames() throws Exception {

        // Assumes rice data with seed script is loaded
        NameIdListDTO nameIdListDTORequest = new NameIdListDTO();
        nameIdListDTORequest.setEntityName("mapset");
        DtoRequestNameIdList dtoRequestNameIdList = new DtoRequestNameIdList();
        NameIdListDTO nameIdListDtoResponse = dtoRequestNameIdList.process(nameIdListDTORequest);


        Assert.assertNotEquals(null, nameIdListDtoResponse);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(nameIdListDtoResponse));
        Assert.assertTrue(nameIdListDtoResponse.getNamesById().size() >= 0);
        Assert.assertEquals(true, TestUtils.isNameIdListSorted(nameIdListDtoResponse.getNamesById()));
    } // testGetMarkers()

    @Test
    public void testGetCvTypes() throws Exception {

        // Assumes rice data with seed script is loaded
        NameIdListDTO nameIdListDTORequest = new NameIdListDTO();
        nameIdListDTORequest.setEntityName("cvgroups");
        DtoRequestNameIdList dtoRequestNameIdList = new DtoRequestNameIdList();
        NameIdListDTO nameIdListDtoResponse = dtoRequestNameIdList.process(nameIdListDTORequest);


        Assert.assertNotEquals(null, nameIdListDtoResponse);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(nameIdListDtoResponse));
        Assert.assertTrue(nameIdListDtoResponse.getNamesById().size() >= 0);
        Assert.assertEquals(true, TestUtils.isNameIdListSorted(nameIdListDtoResponse.getNamesById()));
    } // testGetMarkers()

    @Test
    public void testGetCvNames() throws Exception {

        // Assumes rice data with seed script is loaded
        NameIdListDTO nameIdListDTORequest = new NameIdListDTO();
        nameIdListDTORequest.setEntityName("cvnames");
        DtoRequestNameIdList dtoRequestNameIdList = new DtoRequestNameIdList();
        NameIdListDTO nameIdListDtoResponse = dtoRequestNameIdList.process(nameIdListDTORequest);


        Assert.assertNotEquals(null, nameIdListDtoResponse);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(nameIdListDtoResponse));
        Assert.assertTrue(nameIdListDtoResponse.getNamesById().size() >= 0);
        Assert.assertEquals(true, TestUtils.isNameIdListSorted(nameIdListDtoResponse.getNamesById()));
    } // testGetMarkers()

    @Test
    public void testGetRoles() throws Exception {

        // Assumes rice data with seed script is loaded
        NameIdListDTO nameIdListDTORequest = new NameIdListDTO();
        nameIdListDTORequest.setEntityName("role");
        DtoRequestNameIdList dtoRequestNameIdList = new DtoRequestNameIdList();
        NameIdListDTO nameIdListDtoResponse = dtoRequestNameIdList.process(nameIdListDTORequest);


        Assert.assertNotEquals(null, nameIdListDtoResponse);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(nameIdListDtoResponse));
        Assert.assertTrue(nameIdListDtoResponse.getNamesById().size() >= 0);
        Assert.assertEquals(true, TestUtils.isNameIdListSorted(nameIdListDtoResponse.getNamesById()));
    } // testGetMarkers()

    @Test
    public void testGetMapNamesByType() throws Exception {

        // Assumes rice data with seed script is loaded
        NameIdListDTO nameIdListDTORequest = new NameIdListDTO();
        nameIdListDTORequest.setEntityName("mapNameByTypeId");
        nameIdListDTORequest.setFilter("19");
        DtoRequestNameIdList dtoRequestNameIdList = new DtoRequestNameIdList();
        NameIdListDTO nameIdListDtoResponse = dtoRequestNameIdList.process(nameIdListDTORequest);


        Assert.assertNotEquals(null, nameIdListDtoResponse);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(nameIdListDtoResponse));
        Assert.assertTrue(nameIdListDtoResponse.getNamesById().size() >= 0);
        Assert.assertEquals(true, TestUtils.isNameIdListSorted(nameIdListDtoResponse.getNamesById()));
    } // testGetMarkers()


    @Test
    public void testGetManifestNames() throws Exception {

        // Assumes rice data with seed script is loaded
        NameIdListDTO nameIdListDTORequest = new NameIdListDTO();
        nameIdListDTORequest.setEntityName("manifest");
        DtoRequestNameIdList dtoRequestNameIdList = new DtoRequestNameIdList();
        NameIdListDTO nameIdListDtoResponse = dtoRequestNameIdList.process(nameIdListDTORequest);

        Assert.assertNotEquals(null, nameIdListDtoResponse);
        Assert.assertEquals(true, nameIdListDtoResponse.getStatus().isSucceeded());
        Assert.assertTrue(nameIdListDtoResponse.getNamesById().size() >= 0);
        Assert.assertEquals(true, TestUtils.isNameIdListSorted(nameIdListDtoResponse.getNamesById()));

    }

    @Test
    public void testGetOrganizationNames() throws Exception {

        // Assumes rice data with seed script is loaded
        NameIdListDTO nameIdListDTORequest = new NameIdListDTO();
        nameIdListDTORequest.setEntityName("organization");
        DtoRequestNameIdList dtoRequestNameIdList = new DtoRequestNameIdList();
        NameIdListDTO nameIdListDtoResponse = dtoRequestNameIdList.process(nameIdListDTORequest);

        Assert.assertNotEquals(null, nameIdListDtoResponse);
        //Assert.assertEquals(true, nameIdListDtoResponse.getStatus().isSucceeded());
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(nameIdListDtoResponse));
        Assert.assertTrue(nameIdListDtoResponse.getNamesById().size() >= 0);
        Assert.assertEquals(true, TestUtils.isNameIdListSorted(nameIdListDtoResponse.getNamesById()));

    }

    @Test
    public void testGetDataSetFileNames() throws Exception {

        // Assumes rice data with seed script is loaded
        NameIdListDTO nameIdListDTORequest = new NameIdListDTO();
        nameIdListDTORequest.setEntityName("datasetnamesbyexperimentid");
        nameIdListDTORequest.setFilter("2");
        DtoRequestNameIdList dtoRequestNameIdList = new DtoRequestNameIdList();
        NameIdListDTO nameIdListDtoResponse = dtoRequestNameIdList.process(nameIdListDTORequest);

        Assert.assertNotEquals(null, nameIdListDtoResponse);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(nameIdListDtoResponse));
        Assert.assertTrue(nameIdListDtoResponse.getNamesById().size() >= 0);
        Assert.assertEquals(true, TestUtils.isNameIdListSorted(nameIdListDtoResponse.getNamesById()));

    }

    @Test
    public void testGetDataSetNames() throws Exception {

        // Assumes rice data with seed script is loaded
        NameIdListDTO nameIdListDTORequest = new NameIdListDTO();
        nameIdListDTORequest.setEntityName("datasetnames");
        DtoRequestNameIdList dtoRequestNameIdList = new DtoRequestNameIdList();
        NameIdListDTO nameIdListDtoResponse = dtoRequestNameIdList.process(nameIdListDTORequest);


        Assert.assertNotEquals(null, nameIdListDtoResponse);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(nameIdListDtoResponse));
        Assert.assertEquals(true, nameIdListDtoResponse.getStatus().isSucceeded());
        Assert.assertTrue(nameIdListDtoResponse.getNamesById().size() >= 0);
        Assert.assertEquals(true, TestUtils.isNameIdListSorted(nameIdListDtoResponse.getNamesById()));
    }
}
