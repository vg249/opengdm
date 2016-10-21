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
import org.gobiiproject.gobiiclient.dtorequests.Helpers.EntityParamValues;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestDtoFactory;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.dto.container.*;
import org.gobiiproject.gobiimodel.headerlesscontainer.OrganizationDTO;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by Angel on 5/9/2016.
 */
public class DtoRequestOrganizationTest {

    private static UriFactory uriFactory;

    @BeforeClass
    public static void setUpClass() throws Exception {
        Assert.assertTrue(Authenticator.authenticate());
        String currentCropContextRoot = ClientContext.getInstance(null, false).getCurrentCropContextRoot();
        DtoRequestOrganizationTest.uriFactory = new UriFactory(currentCropContextRoot);

    }

    @AfterClass
    public static void tearDownUpClass() throws Exception {
        Assert.assertTrue(Authenticator.deAuthenticate());
    }


    @Test
    public void testCreateOrganizationWithHttpPost() throws Exception {

        OrganizationDTO newOrganizationDto = TestDtoFactory
                .makePopulatedOrganizationDTO(GobiiProcessType.CREATE, 1);

        PayloadEnvelope<OrganizationDTO> payloadEnvelope = new PayloadEnvelope<>(newOrganizationDto, GobiiProcessType.CREATE);
        RestResource<OrganizationDTO> restResource = new RestResource<>(DtoRequestOrganizationTest
                .uriFactory
                .resourceColl(ServiceRequestId.URL_ORGANIZATION));
        PayloadEnvelope<OrganizationDTO> organizationDTOResponseEnvelope = restResource.post(OrganizationDTO.class,
                payloadEnvelope);
        OrganizationDTO organizationDTOResponse = organizationDTOResponseEnvelope.getPayload().getData().get(0);

        Assert.assertNotEquals(null, organizationDTOResponse);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(organizationDTOResponseEnvelope.getHeader()));
        Assert.assertTrue(organizationDTOResponse.getOrganizationId() > 0);


        RestUri restUriOrganizationForGetById = DtoRequestOrganizationTest
                .uriFactory
                .resourceByUriIdParam(ServiceRequestId.URL_ORGANIZATION);
        restUriOrganizationForGetById.setParamValue("id", organizationDTOResponse.getOrganizationId().toString());
        RestResource<OrganizationDTO> restResourceForGetById = new RestResource<>(restUriOrganizationForGetById);
        PayloadEnvelope<OrganizationDTO> resultEnvelopeForGetByID = restResourceForGetById
                .get(OrganizationDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForGetByID.getHeader()));
        OrganizationDTO organizationDTOResponseForParams = resultEnvelopeForGetByID.getPayload().getData().get(0);

    }

    @Test
    public void testUpdateOrganizationWithHttpPut() throws Exception {

        // create a new organization for our test
        OrganizationDTO newOrganizationDto = TestDtoFactory
                .makePopulatedOrganizationDTO(GobiiProcessType.CREATE, 1);

        PayloadEnvelope<OrganizationDTO> payloadEnvelope = new PayloadEnvelope<>(newOrganizationDto, GobiiProcessType.CREATE);
        RestResource<OrganizationDTO> restResource = new RestResource<>(DtoRequestOrganizationTest
                .uriFactory
                .resourceColl(ServiceRequestId.URL_ORGANIZATION));
        PayloadEnvelope<OrganizationDTO> organizationDTOResponseEnvelope = restResource.post(OrganizationDTO.class,
                payloadEnvelope);
        OrganizationDTO newOrganizationDTOResponse = organizationDTOResponseEnvelope.getPayload().getData().get(0);

        // re-retrieve the organization we just created so we start with a fresh READ mode dto

        RestUri restUriOrganizationForGetById = DtoRequestOrganizationTest
                .uriFactory
                .resourceByUriIdParam(ServiceRequestId.URL_ORGANIZATION);
        restUriOrganizationForGetById.setParamValue("id", newOrganizationDTOResponse.getOrganizationId().toString());
        RestResource<OrganizationDTO> restResourceForGetById = new RestResource<>(restUriOrganizationForGetById);
        PayloadEnvelope<OrganizationDTO> resultEnvelopeForGetByID = restResourceForGetById
                .get(OrganizationDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForGetByID.getHeader()));
        OrganizationDTO organizationDTOReceived = resultEnvelopeForGetByID.getPayload().getData().get(0);



        // so this would be the typical workflow for the client app
        String newName = UUID.randomUUID().toString();
        organizationDTOReceived.setName(newName);
        restResourceForGetById.setParamValue("id", organizationDTOReceived.getOrganizationId().toString());
        PayloadEnvelope<OrganizationDTO> organizationDTOResponseEnvelopeUpdate = restResourceForGetById.put(OrganizationDTO.class,
                new PayloadEnvelope<>(organizationDTOReceived, GobiiProcessType.UPDATE));

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(organizationDTOResponseEnvelopeUpdate.getHeader()));

        OrganizationDTO OrganizationDTORequest = organizationDTOResponseEnvelopeUpdate.getPayload().getData().get(0);

//        OrganizationDTO OrganizationDTOResponse = dtoRequestOrganization.process(organizationDTOReceived);
//        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(OrganizationDTOResponse));

        restUriOrganizationForGetById.setParamValue("id",OrganizationDTORequest.getOrganizationId().toString());
        resultEnvelopeForGetByID = restResourceForGetById
                .get(OrganizationDTO.class);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForGetByID.getHeader()));


        OrganizationDTO dtoRequestOrganizationReRetrieved =resultEnvelopeForGetByID.getPayload().getData().get(0);


        Assert.assertTrue(dtoRequestOrganizationReRetrieved.getName().equals(newName));
    }



    @Test
    public void testGetOrganizationDetailsWithHttpGet() throws Exception {


        // get a list of organizations
        RestUri restUriOrganization = DtoRequestOrganizationTest.uriFactory.resourceColl(ServiceRequestId.URL_ORGANIZATION);
        RestResource<OrganizationDTO> restResource = new RestResource<>(restUriOrganization);
        PayloadEnvelope<OrganizationDTO> resultEnvelope = restResource
                .get(OrganizationDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        List<OrganizationDTO> organizationDTOList = resultEnvelope.getPayload().getData();
        Assert.assertNotNull(organizationDTOList);
        Assert.assertTrue(organizationDTOList.size() > 0 );
        Assert.assertNotNull(organizationDTOList.get(0).getName());


        // use an artibrary organization id
        Integer organizationId = organizationDTOList.get(0).getOrganizationId();
        RestUri restUriOrganizationForGetById = DtoRequestOrganizationTest
                .uriFactory
                .resourceByUriIdParam(ServiceRequestId.URL_ORGANIZATION);
        restUriOrganizationForGetById.setParamValue("id", organizationId.toString());
        RestResource<OrganizationDTO> restResourceForGetById = new RestResource<>(restUriOrganizationForGetById);
        PayloadEnvelope<OrganizationDTO> resultEnvelopeForGetByID = restResourceForGetById
                .get(OrganizationDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        OrganizationDTO organizationDTO = resultEnvelopeForGetByID.getPayload().getData().get(0);
        Assert.assertTrue(organizationDTO.getOrganizationId() > 0);
        Assert.assertNotNull(organizationDTO.getName());
    }

    @Test
    public void getOrganizationsWithHttpGet() throws Exception {

        RestUri restUriOrganization = DtoRequestOrganizationTest.uriFactory.resourceColl(ServiceRequestId.URL_ORGANIZATION);
        RestResource<OrganizationDTO> restResource = new RestResource<>(restUriOrganization);
        PayloadEnvelope<OrganizationDTO> resultEnvelope = restResource
                .get(OrganizationDTO.class);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        List<OrganizationDTO> organizationDTOList = resultEnvelope.getPayload().getData();
        Assert.assertNotNull(organizationDTOList);
        Assert.assertTrue(organizationDTOList.size() > 0 );
        Assert.assertNotNull(organizationDTOList.get(0).getName());


        LinkCollection linkCollection = resultEnvelope.getPayload().getLinkCollection();
        Assert.assertTrue(linkCollection.getLinksPerDataItem().size() == organizationDTOList.size() );
        List<Integer> itemsToTest = TestUtils.makeListOfIntegersInRange(10, organizationDTOList.size());
        for(Integer currentItemIdx : itemsToTest  ) {
            OrganizationDTO currentOrganizationDto = organizationDTOList.get(currentItemIdx);

            Link currentLink = linkCollection.getLinksPerDataItem().get(currentItemIdx);

            RestUri restUriOrganizationForGetById = DtoRequestOrganizationTest
                    .uriFactory
                    .RestUriFromUri(currentLink.getHref());
            RestResource<OrganizationDTO> restResourceForGetById = new RestResource<>(restUriOrganizationForGetById);
            PayloadEnvelope<OrganizationDTO> resultEnvelopeForGetByID = restResourceForGetById
                    .get(OrganizationDTO.class);
            Assert.assertNotNull(resultEnvelopeForGetByID);
            Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForGetByID.getHeader()));
            OrganizationDTO organizationDTOFromLink = resultEnvelopeForGetByID.getPayload().getData().get(0);
            Assert.assertTrue(currentOrganizationDto.getName().equals(organizationDTOFromLink.getName()));
            Assert.assertTrue(currentOrganizationDto.getOrganizationId().equals(organizationDTOFromLink.getOrganizationId()));
        }

    }

}
