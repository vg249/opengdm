// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests;

import org.apache.commons.lang.StringUtils;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.RestUri;
import org.gobiiproject.gobiiapimodel.restresources.UriFactory;
import org.gobiiproject.gobiiclient.core.ClientContext;
import org.gobiiproject.gobiiclient.core.restmethods.RestResource;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.Authenticator;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.dto.container.NameIdDTO;
import org.gobiiproject.gobiimodel.dto.container.NameIdListDTO;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;
import org.gobiiproject.gobiimodel.types.GobiiFilterType;
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

    private void testNameRetrieval(GobiiEntityNameType gobiiEntityNameType,
                                   GobiiFilterType gobiiFilterType,
                                   String filterValue) throws Exception {
        RestUri namesUri = uriFactory.nameIdList();
        RestResource<NameIdDTO> restResource = new RestResource<>(namesUri);
        namesUri.setParamValue("entity", gobiiEntityNameType.toString().toLowerCase());

        if (GobiiFilterType.NONE != gobiiFilterType) {
            namesUri.setParamValue("filterType", StringUtils.capitalize(gobiiFilterType.toString().toUpperCase()));
            namesUri.setParamValue("filterValue", filterValue);
        }


        PayloadEnvelope<NameIdDTO> resultEnvelope = restResource
                .get(NameIdDTO.class);

        String assertionErrorStem = "Error testing name-id retrieval of entity "
                + gobiiEntityNameType.toString();

        if( GobiiFilterType.NONE != gobiiFilterType) {

            assertionErrorStem += " with filter type "
                    + gobiiFilterType.toString()
                    + " and filter value "
                    + filterValue;
        }

        assertionErrorStem += ": ";

        Assert.assertFalse(assertionErrorStem,
                TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));

        List<NameIdDTO> NameIdDTOList = resultEnvelope.getPayload().getData();
        Assert.assertNotNull(assertionErrorStem
                + "null name id list",
                NameIdDTOList);

        Assert.assertTrue(assertionErrorStem
                + "empty name id list",
                NameIdDTOList.size() > 0);

        Assert.assertNotNull(assertionErrorStem
                + "null name",
                NameIdDTOList.get(0).getName());

        Assert.assertNotNull(assertionErrorStem
                + "null ",
                NameIdDTOList.get(0).getId());

        Assert.assertTrue(assertionErrorStem
                + "id <= 0",
                NameIdDTOList.get(0).getId() > 0);

    }


    @Test
    public void testGetAnalysisNames() throws Exception {

        // Assumes rice data with seed script is loaded
        testNameRetrieval(GobiiEntityNameType.ANALYSES, GobiiFilterType.NONE, null);

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
        testNameRetrieval(GobiiEntityNameType.ANALYSES, GobiiFilterType.BYTYPEID, "33");

//        RestUri namesUri = uriFactory.nameIdList();
//        RestResource<NameIdDTO> restResource = new RestResource<>(namesUri);
//        namesUri.setParamValue("entity", GobiiEntityNameType.ANALYSES.toString().toLowerCase());
//        namesUri.setParamValue("filterType", StringUtils.capitalize(GobiiFilterType.BYTYPEID.toString().toUpperCase()));
//        namesUri.setParamValue("filterValue", "33");
//        PayloadEnvelope<NameIdDTO> resultEnvelope = restResource
//                .get(NameIdDTO.class);
//
//        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
//        List<NameIdDTO> NameIdDTOList = resultEnvelope.getPayload().getData();
//        Assert.assertNotNull(NameIdDTOList);
//        Assert.assertTrue(NameIdDTOList.size() > 0);
//        Assert.assertNotNull(NameIdDTOList.get(0).getName());
//        Assert.assertNotNull(NameIdDTOList.get(0).getId());
//        Assert.assertTrue(NameIdDTOList.get(0).getId() > 0);
    }

    @Test
    public void testGetNamesWithBadEntityValue() throws Exception {

        // Assumes rice data with seed script is loaded
        RestUri namesUri = uriFactory.nameIdList();
        RestResource<NameIdDTO> restResource = new RestResource<>(namesUri);

        namesUri.setParamValue("entity", "foo");
        PayloadEnvelope<NameIdDTO> resultEnvelope = restResource
                .get(NameIdDTO.class);

        Assert.assertTrue("There should be exactly one error for bad entity type",
                1 == resultEnvelope
                        .getHeader()
                        .getStatus()
                        .getStatusMessages()
                        .stream()
                        .filter(m -> m.getMessage().toLowerCase().contains("unsupported entity for list request"))
                        .count());

    }

    @Test
    public void testGetAnalysisNamesByTypeIdErrorBadFilterType() throws Exception {

        // Assumes rice data with seed script is loaded
        RestUri namesUri = uriFactory.nameIdList();
        RestResource<NameIdDTO> restResource = new RestResource<>(namesUri);

        namesUri.setParamValue("entity", GobiiEntityNameType.ANALYSES.toString().toLowerCase());
        namesUri.setParamValue("filterType", "foo");
        namesUri.setParamValue("filterValue", "33");
        PayloadEnvelope<NameIdDTO> resultEnvelope = restResource
                .get(NameIdDTO.class);

        Assert.assertTrue("There should be exactly one error for the unsupported filter type",
                1 == resultEnvelope
                        .getHeader()
                        .getStatus()
                        .getStatusMessages()
                        .stream()
                        .filter(m -> m.getMessage().toLowerCase().contains("unsupported filter"))
                        .count());

    }

    @Test
    public void testGetAnalysisNamesByTypeIdErrorEmptyFilterValue() throws Exception {

        // Assumes rice data with seed script is loaded
        RestUri namesUri = uriFactory.nameIdList();
        RestResource<NameIdDTO> restResource = new RestResource<>(namesUri);

        namesUri.setParamValue("entity", GobiiEntityNameType.ANALYSES.toString().toLowerCase());
        namesUri.setParamValue("filterType", StringUtils.capitalize(GobiiFilterType.BYTYPEID.toString().toUpperCase()));
        // normally would also specify "filterValue" here

        PayloadEnvelope<NameIdDTO> resultEnvelope = restResource
                .get(NameIdDTO.class);

        Assert.assertTrue("There should be exactly one error for filter value not supplied",
                1 == resultEnvelope
                        .getHeader()
                        .getStatus()
                        .getStatusMessages()
                        .stream()
                        .filter(m -> m.getMessage().toLowerCase().contains("a value was not supplied for filter"))
                        .count());

    }


    @Test
    public void testGetContactsByIdForContactType() throws Exception {

        testNameRetrieval(GobiiEntityNameType.CONTACTS, GobiiFilterType.BYTYPENAME, "Curator");


//        RestUri namesUri = uriFactory.nameIdList();
//        RestResource<NameIdDTO> restResource = new RestResource<>(namesUri);
//        namesUri.setParamValue("entity", GobiiEntityNameType.CONTACTS.toString().toLowerCase());
//        namesUri.setParamValue("filterType", StringUtils.capitalize(GobiiFilterType.BYTYPENAME.toString().toUpperCase()));
//        namesUri.setParamValue("filterValue", "Curator");
//        PayloadEnvelope<NameIdDTO> resultEnvelope = restResource
//                .get(NameIdDTO.class);
//
//        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
//        List<NameIdDTO> NameIdDTOList = resultEnvelope.getPayload().getData();
//        Assert.assertNotNull(NameIdDTOList);
//        Assert.assertTrue(NameIdDTOList.size() > 0);
//        Assert.assertNotNull(NameIdDTOList.get(0).getName());
//        Assert.assertNotNull(NameIdDTOList.get(0).getId());
//        Assert.assertTrue(NameIdDTOList.get(0).getId() > 0);

        // Assumes rice data with seed script is loaded
//        DtoRequestNameIdList dtoRequestNameIdList = new DtoRequestNameIdList();
//        NameIdListDTO nameIdListDTOReuest = new NameIdListDTO();
//        nameIdListDTOReuest.setFilter("Curator");
//        nameIdListDTOReuest.setEntityName("contact");
//
//        NameIdListDTO nameIdListDtoResponse = dtoRequestNameIdList.process(nameIdListDTOReuest);
//
//
//        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(nameIdListDtoResponse));
//        Assert.assertNotEquals(null, nameIdListDtoResponse);
//        Assert.assertEquals(true, nameIdListDtoResponse.getStatus().isSucceeded());
//        Assert.assertTrue(nameIdListDtoResponse.getNamesById().size() >= 0);
//        Assert.assertEquals(true, TestUtils.isNameIdListSorted(nameIdListDtoResponse.getNamesById()));

    } // testGetMarkers()

    @Test
    public void testGetContactNames() throws Exception {

        // Assumes rice data with seed script is loaded
        testNameRetrieval(GobiiEntityNameType.CONTACTS, GobiiFilterType.NONE, null);

//        RestUri namesUri = uriFactory.nameIdList();
//        RestResource<NameIdDTO> restResource = new RestResource<>(namesUri);
//        namesUri.setParamValue("entity", GobiiEntityNameType.CONTACTS.toString().toLowerCase());
//        PayloadEnvelope<NameIdDTO> resultEnvelope = restResource
//                .get(NameIdDTO.class);
//
//        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
//        List<NameIdDTO> NameIdDTOList = resultEnvelope.getPayload().getData();
//        Assert.assertNotNull(NameIdDTOList);
//        Assert.assertTrue(NameIdDTOList.size() > 0);
//        Assert.assertNotNull(NameIdDTOList.get(0).getName());
//        Assert.assertNotNull(NameIdDTOList.get(0).getId());
//        Assert.assertTrue(NameIdDTOList.get(0).getId() > 0);

        // Assumes rice data with seed script is loaded
//        DtoRequestNameIdList dtoRequestNameIdList = new DtoRequestNameIdList();
//        NameIdListDTO nameIdListDTORequest = new NameIdListDTO();
//        nameIdListDTORequest.setEntityName("allContacts");
//
//        NameIdListDTO nameIdListDtoResponse = dtoRequestNameIdList.process(nameIdListDTORequest);
//
//
//        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(nameIdListDtoResponse));
//        Assert.assertNotEquals(null, nameIdListDtoResponse);
//        Assert.assertEquals(true, nameIdListDtoResponse.getStatus().isSucceeded());
//        Assert.assertTrue(nameIdListDtoResponse.getNamesById().size() >= 0);
//        Assert.assertEquals(true, TestUtils.isNameIdListSorted(nameIdListDtoResponse.getNamesById()));
//
    } // testGetMarkers()

    @Test
    public void testGetProjectNames() throws Exception {


        // Assumes rice data with seed script is loaded
        testNameRetrieval(GobiiEntityNameType.PROJECTS, GobiiFilterType.NONE, null);



//        NameIdListDTO nameIdListDTORequest = new NameIdListDTO();
//        nameIdListDTORequest.setEntityName("projectnames");
//        DtoRequestNameIdList dtoRequestNameIdList = new DtoRequestNameIdList();
//        NameIdListDTO nameIdListDtoResponse = dtoRequestNameIdList.process(nameIdListDTORequest);
//
//
//        Assert.assertNotEquals(null, nameIdListDtoResponse);
//        Assert.assertEquals(true, nameIdListDtoResponse.getStatus().isSucceeded());
//        Assert.assertTrue(nameIdListDtoResponse.getNamesById().size() >= 0);
//        Assert.assertEquals(true, TestUtils.isNameIdListSorted(nameIdListDtoResponse.getNamesById()));

    } // testGetMarkers()


    @Test
    public void testGetProjectNamesByContactId() throws Exception {

        // Assumes rice data with seed script is loaded
        testNameRetrieval(GobiiEntityNameType.PROJECTS, GobiiFilterType.BYTYPEID, "2");

//        DtoRequestNameIdList dtoRequestNameIdList = new DtoRequestNameIdList();
//        NameIdListDTO nameIdListDTORequest = new NameIdListDTO();
//        nameIdListDTORequest.setEntityName("project");
//        nameIdListDTORequest.setFilter("2");
//        NameIdListDTO nameIdListDtoResponse = dtoRequestNameIdList.process(nameIdListDTORequest);
//
//
//        Assert.assertNotEquals(null, nameIdListDtoResponse);
//        Assert.assertEquals(true, nameIdListDtoResponse.getStatus().isSucceeded());
//        Assert.assertTrue(nameIdListDtoResponse.getNamesById().size() >= 0);
//        Assert.assertEquals(true, TestUtils.isNameIdListSorted(nameIdListDtoResponse.getNamesById()));

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

        testNameRetrieval(GobiiEntityNameType.CVTERMS, GobiiFilterType.BYTYPENAME, "strand");


//        DtoRequestNameIdList dtoRequestNameIdList = new DtoRequestNameIdList();
//        NameIdListDTO nameIdListDTORequest = new NameIdListDTO();
//        nameIdListDTORequest.setEntityName("cvgroupterms");
//        nameIdListDTORequest.setFilter("map_type");
//        NameIdListDTO nameIdListDtoResponse = dtoRequestNameIdList.process(nameIdListDTORequest);
//
//        Assert.assertNotEquals(null, nameIdListDtoResponse);
//        Assert.assertEquals(true, nameIdListDtoResponse.getStatus().isSucceeded());
//        Assert.assertTrue(nameIdListDtoResponse.getNamesById().size() >= 0);
//        Assert.assertEquals(true, TestUtils.isNameIdListSorted(nameIdListDtoResponse.getNamesById()));

    }

    @Test
    public void testGetPlatformNames() throws Exception {

        // Assumes rice data with seed script is loaded
        testNameRetrieval(GobiiEntityNameType.PLATFORMS,GobiiFilterType.NONE,null);
//        NameIdListDTO nameIdListDTORequest = new NameIdListDTO();
//        nameIdListDTORequest.setEntityName("platform");
//        DtoRequestNameIdList dtoRequestNameIdList = new DtoRequestNameIdList();
//        NameIdListDTO nameIdListDtoResponse = dtoRequestNameIdList.process(nameIdListDTORequest);
//
//
//        Assert.assertNotEquals(null, nameIdListDtoResponse);
//        Assert.assertEquals(true, nameIdListDtoResponse.getStatus().isSucceeded());
//        Assert.assertTrue(nameIdListDtoResponse.getNamesById().size() >= 0);
//        Assert.assertEquals(true, TestUtils.isNameIdListSorted(nameIdListDtoResponse.getNamesById()));

    } // testGetMarkers()

    @Test
    public void testGetPlatformNamesByTypeId() throws Exception {
        // Assumes rice data with seed script is loaded
        testNameRetrieval(GobiiEntityNameType.PLATFORMS,GobiiFilterType.BYTYPEID,"1");

//        NameIdListDTO nameIdListDTORequest = new NameIdListDTO();
//        nameIdListDTORequest.setEntityName("platformByTypeId");
//        nameIdListDTORequest.setFilter("1");
//        DtoRequestNameIdList dtoRequestNameIdList = new DtoRequestNameIdList();
//        NameIdListDTO nameIdListDtoResponse = dtoRequestNameIdList.process(nameIdListDTORequest);
//
//
//        Assert.assertNotEquals(null, nameIdListDtoResponse);
//        Assert.assertEquals(true, nameIdListDtoResponse.getStatus().isSucceeded());
//        Assert.assertTrue(nameIdListDtoResponse.getNamesById().size() >= 0);
//        Assert.assertEquals(true, TestUtils.isNameIdListSorted(nameIdListDtoResponse.getNamesById()));

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
        testNameRetrieval(GobiiEntityNameType.CVGROUPS, GobiiFilterType.NONE, null);

//        NameIdListDTO nameIdListDTORequest = new NameIdListDTO();
//        nameIdListDTORequest.setEntityName("cvgroups");
//        DtoRequestNameIdList dtoRequestNameIdList = new DtoRequestNameIdList();
//        NameIdListDTO nameIdListDtoResponse = dtoRequestNameIdList.process(nameIdListDTORequest);
//
//
//        Assert.assertNotEquals(null, nameIdListDtoResponse);
//        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(nameIdListDtoResponse));
//        Assert.assertTrue(nameIdListDtoResponse.getNamesById().size() >= 0);
//        Assert.assertEquals(true, TestUtils.isNameIdListSorted(nameIdListDtoResponse.getNamesById()));
    } // testGetMarkers()

    @Test
    public void testGetCvNames() throws Exception {

        // Assumes rice data with seed script is loaded
        testNameRetrieval(GobiiEntityNameType.CVTERMS, GobiiFilterType.NONE, null);

//        NameIdListDTO nameIdListDTORequest = new NameIdListDTO();
//        nameIdListDTORequest.setEntityName("cvnames");
//        DtoRequestNameIdList dtoRequestNameIdList = new DtoRequestNameIdList();
//        NameIdListDTO nameIdListDtoResponse = dtoRequestNameIdList.process(nameIdListDTORequest);
//
//
//        Assert.assertNotEquals(null, nameIdListDtoResponse);
//        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(nameIdListDtoResponse));
//        Assert.assertTrue(nameIdListDtoResponse.getNamesById().size() >= 0);
//        Assert.assertEquals(true, TestUtils.isNameIdListSorted(nameIdListDtoResponse.getNamesById()));
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

        testNameRetrieval(GobiiEntityNameType.MANIFESTS,GobiiFilterType.NONE,null);


        // Assumes rice data with seed script is loaded
//        NameIdListDTO nameIdListDTORequest = new NameIdListDTO();
//        nameIdListDTORequest.setEntityName("manifest");
//        DtoRequestNameIdList dtoRequestNameIdList = new DtoRequestNameIdList();
//        NameIdListDTO nameIdListDtoResponse = dtoRequestNameIdList.process(nameIdListDTORequest);
//
//        Assert.assertNotEquals(null, nameIdListDtoResponse);
//        Assert.assertEquals(true, nameIdListDtoResponse.getStatus().isSucceeded());
//        Assert.assertTrue(nameIdListDtoResponse.getNamesById().size() >= 0);
//        Assert.assertEquals(true, TestUtils.isNameIdListSorted(nameIdListDtoResponse.getNamesById()));

    }

    @Test
    public void testGetOrganizationNames() throws Exception {

        // Assumes rice data with seed script is loaded
        testNameRetrieval(GobiiEntityNameType.ORGANIZATIONS,GobiiFilterType.NONE,null);

    }

    @Test
    public void testGetDataSetNamesByExperimentId() throws Exception {

        // Assumes rice data with seed script is loaded
        testNameRetrieval(GobiiEntityNameType.DATASETS, GobiiFilterType.BYTYPEID, "2");

//        RestUri namesUri = uriFactory.nameIdList();
//        RestResource<NameIdDTO> restResource = new RestResource<>(namesUri);
//        namesUri.setParamValue("entity", GobiiEntityNameType.DATASETS.toString().toLowerCase());
//        namesUri.setParamValue("filterType", StringUtils.capitalize(GobiiFilterType.BYTYPEID.toString().toUpperCase()));
//        namesUri.setParamValue("filterValue", "2");
//        PayloadEnvelope<NameIdDTO> resultEnvelope = restResource
//                .get(NameIdDTO.class);
//
//        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
//        List<NameIdDTO> NameIdDTOList = resultEnvelope.getPayload().getData();
//        Assert.assertNotNull(NameIdDTOList);
//        Assert.assertTrue(NameIdDTOList.size() > 0);
//        Assert.assertNotNull(NameIdDTOList.get(0).getName());
//        Assert.assertNotNull(NameIdDTOList.get(0).getId());
//        Assert.assertTrue(NameIdDTOList.get(0).getId() > 0);
//        NameIdListDTO nameIdListDTORequest = new NameIdListDTO();
//        nameIdListDTORequest.setEntityName("datasetnamesbyexperimentid");
//        nameIdListDTORequest.setFilter("2");
//        DtoRequestNameIdList dtoRequestNameIdList = new DtoRequestNameIdList();
//        NameIdListDTO nameIdListDtoResponse = dtoRequestNameIdList.process(nameIdListDTORequest);
//
//        Assert.assertNotEquals(null, nameIdListDtoResponse);
//        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(nameIdListDtoResponse));
//        Assert.assertTrue(nameIdListDtoResponse.getNamesById().size() >= 0);
//        Assert.assertEquals(true, TestUtils.isNameIdListSorted(nameIdListDtoResponse.getNamesById()));

    }

    @Test
    public void testGetDataSetNames() throws Exception {

        // Assumes rice data with seed script is loaded

        testNameRetrieval(GobiiEntityNameType.DATASETS, GobiiFilterType.NONE, null);

//        RestUri namesUri = uriFactory.nameIdList();
//        RestResource<NameIdDTO> restResource = new RestResource<>(namesUri);
//        namesUri.setParamValue("entity", GobiiEntityNameType.DATASETS.toString().toLowerCase());
//        PayloadEnvelope<NameIdDTO> resultEnvelope = restResource
//                .get(NameIdDTO.class);
//
//        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
//        List<NameIdDTO> NameIdDTOList = resultEnvelope.getPayload().getData();
//        Assert.assertNotNull(NameIdDTOList);
//        Assert.assertTrue(NameIdDTOList.size() > 0);
//        Assert.assertNotNull(NameIdDTOList.get(0).getName());
//        Assert.assertNotNull(NameIdDTOList.get(0).getId());
//        Assert.assertTrue(NameIdDTOList.get(0).getId() > 0);


//        NameIdListDTO nameIdListDTORequest = new NameIdListDTO();
//        nameIdListDTORequest.setEntityName("datasetnames");
//        DtoRequestNameIdList dtoRequestNameIdList = new DtoRequestNameIdList();
//        NameIdListDTO nameIdListDtoResponse = dtoRequestNameIdList.process(nameIdListDTORequest);
//
//
//        Assert.assertNotEquals(null, nameIdListDtoResponse);
//        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(nameIdListDtoResponse));
//        Assert.assertEquals(true, nameIdListDtoResponse.getStatus().isSucceeded());
//        Assert.assertTrue(nameIdListDtoResponse.getNamesById().size() >= 0);
//        Assert.assertEquals(true, TestUtils.isNameIdListSorted(nameIdListDtoResponse.getNamesById()));
    }
}
