// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests.dtorequest;


import org.gobiiproject.gobiiapimodel.hateos.Link;
import org.gobiiproject.gobiiapimodel.hateos.LinkCollection;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.types.ServiceRequestId;
import org.gobiiproject.gobiiclient.core.ClientContext;
import org.gobiiproject.gobiiclient.core.restmethods.RestResource;
import org.gobiiproject.gobiiapimodel.restresources.RestUri;
import org.gobiiproject.gobiiapimodel.restresources.UriFactory;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.Authenticator;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.EntityParamValues;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestDtoFactory;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.headerlesscontainer.ContactDTO;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class DtoRequestContactTest {

    private static UriFactory uriFactory;

    @BeforeClass
    public static void setUpClass() throws Exception {
        Assert.assertTrue(Authenticator.authenticate());
        String currentCropContextRoot = ClientContext.getInstance(null, false).getCurrentCropContextRoot();
        DtoRequestContactTest.uriFactory = new UriFactory(currentCropContextRoot);

    }

    @AfterClass
    public static void tearDownUpClass() throws Exception {
        Assert.assertTrue(Authenticator.deAuthenticate());
    }


    @Test
    public void testGetContact() throws Exception {

        RestUri restUriContact = DtoRequestContactTest
                .uriFactory
                .resourceByUriIdParam(ServiceRequestId.URL_CONTACTS);
        restUriContact.setParamValue("id", "6");
        RestResource<ContactDTO> restResource = new RestResource<>(restUriContact);
        PayloadEnvelope<ContactDTO> resultEnvelope = restResource
                .get(ContactDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        ContactDTO contactDTO = resultEnvelope.getPayload().getData().get(0);
        Assert.assertTrue(contactDTO.getContactId() > 0);
        Assert.assertNotNull(contactDTO.getEmail());
        Assert.assertTrue(contactDTO.getRoles().size() > 0 );
    } //


//    @Test
//    public void testCreateContact() throws Exception {
//
//        DtoRequestContact dtoRequestContact = new DtoRequestContact();
//        ContactDTO contactDTORequest = new ContactDTO();
//
//        // set the plain properties
//        contactDTORequest.setFirstName("Angel Manica");
//        contactDTORequest.setLastName("Raquel");
//        contactDTORequest.setEmail("added dummy email");
//        contactDTORequest.setCode("added New Code");
//        contactDTORequest.setCreatedBy(1);
//        contactDTORequest.setCreatedDate(new Date());
//        contactDTORequest.setModifiedBy(1);
//        contactDTORequest.setModifiedDate(new Date());
//        contactDTORequest.setOrganizationId(1);
//        contactDTORequest.getRoles().add(1);
//        contactDTORequest.getRoles().add(2);
//
//        PayloadEnvelope<ContactDTO> contactDTOResponseEnvelope = dtoRequestContact.process(new PayloadEnvelope<>(contactDTORequest, GobiiProcessType.CREATE));
//        Assert.assertNotEquals(null, contactDTOResponseEnvelope);
//        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(contactDTOResponseEnvelope.getHeader()));
//
//        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(contactDTOResponseEnvelope.getHeader()));
//
//        ContactDTO contactDTOResponse = contactDTOResponseEnvelope.getPayload().getData().get(0);
//        Assert.assertNotEquals(null, contactDTOResponse);
//        Assert.assertTrue(contactDTOResponse.getContactId() > 0);
//
//    }

    @Test
    public void testUpdateContact() throws Exception {


        // create a new contact for our test
        EntityParamValues entityParamValues = TestDtoFactory.makeArbitraryEntityParams();
        ContactDTO newContactDto = TestDtoFactory
                .makePopulatedContactDTO(GobiiProcessType.CREATE, 1);

        RestResource<ContactDTO> restResourceContacts = new RestResource<>(DtoRequestContactTest.uriFactory.contacts());


        PayloadEnvelope<ContactDTO> resultEnvelopeNewContact = restResourceContacts.post(ContactDTO.class,
                new PayloadEnvelope<>(newContactDto, GobiiProcessType.CREATE));
        //PayloadEnvelope<ContactDTO> resultEnvelopeNewContact = dtoRequestContact.process(new PayloadEnvelope<>(newContactDto, GobiiProcessType.CREATE));

        ContactDTO newContactDTOResponse = resultEnvelopeNewContact.getPayload().getData().get(0);


        RestUri restUriContact = DtoRequestContactTest.uriFactory.resourceByUriIdParam(ServiceRequestId.URL_CONTACTS);
        restUriContact.setParamValue("id", newContactDTOResponse.getContactId().toString());
        RestResource<ContactDTO> restResourceContactById = new RestResource<>(restUriContact);
        PayloadEnvelope<ContactDTO> contactDTOResponseEnvelope = restResourceContactById
                .get(ContactDTO.class);

        Assert.assertNotEquals(null, contactDTOResponseEnvelope);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(contactDTOResponseEnvelope.getHeader()));
        ContactDTO contactDTOReceived = contactDTOResponseEnvelope.getPayload().getData().get(0);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(contactDTOResponseEnvelope.getHeader()));


        // so this would be the typical workflow for the client app

        String newName = UUID.randomUUID().toString();
        contactDTOReceived.setLastName(newName);

        contactDTOReceived.setOrganizationId(null);

        //PayloadEnvelope<ContactDTO> contactDTOResponseEnvelopeUpdate = dtoRequestContact.process();

        restResourceContactById.setParamValue("id", contactDTOReceived.getContactId().toString());
        PayloadEnvelope<ContactDTO> contactDTOResponseEnvelopeUpdate = restResourceContactById.put(ContactDTO.class,
                new PayloadEnvelope<>(contactDTOReceived, GobiiProcessType.UPDATE));

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(contactDTOResponseEnvelopeUpdate.getHeader()));


        RestUri restUriContactReRetrive = DtoRequestContactTest.uriFactory.resourceByUriIdParam(ServiceRequestId.URL_CONTACTS);
        restUriContactReRetrive.setParamValue("id", contactDTOReceived.getContactId().toString());
        RestResource<ContactDTO> restResourceReRetrieve = new RestResource<>(restUriContactReRetrive);
        PayloadEnvelope<ContactDTO> contactDTOResponseEnvelopeReRetrieved = restResourceReRetrieve
                .get(ContactDTO.class);


        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(contactDTOResponseEnvelopeReRetrieved.getHeader()));
        ContactDTO dtoRequestContactReRetrieved =
                contactDTOResponseEnvelopeReRetrieved.getPayload().getData().get(0);

        Assert.assertTrue(dtoRequestContactReRetrieved.getLastName().equals(newName));
        Assert.assertNull(dtoRequestContactReRetrieved.getOrganizationId());

    }

    @Test
    public void getSingleContactWithHttpGet() throws Exception {

        RestUri restUriContact = DtoRequestContactTest.uriFactory.resourceByUriIdParam(ServiceRequestId.URL_CONTACTS);
        restUriContact.setParamValue("id", "1");
        RestResource<ContactDTO> restResource = new RestResource<>(restUriContact);
        PayloadEnvelope<ContactDTO> resultEnvelope = restResource
                .get(ContactDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        ContactDTO contactDTO = resultEnvelope.getPayload().getData().get(0);
        Assert.assertNotNull(contactDTO.getEmail());

        //restUriContact.setParamValue(Param);
    }

    @Test
    public void getContactsBySearchWithHttpGet() throws Exception {

        RestUri restUriContact = DtoRequestContactTest.uriFactory.contactsByQueryParams();
        restUriContact.setParamValue("email", "dummy@email.address");
        RestResource<ContactDTO> restResource = new RestResource<>(restUriContact);
        PayloadEnvelope<ContactDTO> resultEnvelope = restResource
                .get(ContactDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        ContactDTO contactDTO = resultEnvelope.getPayload().getData().get(0);
        Assert.assertNotNull(contactDTO);
        Assert.assertNotNull(contactDTO.getEmail());
        Assert.assertTrue(contactDTO.getRoles().size() > 0);

        //restUriContact.setParamValue(Param);
    }

    @Test
    public void testCreateContactWithHttpPost() throws Exception {


        // We are creating a new contact here.
        ContactDTO newContactDTO = new ContactDTO();

        // set the plain properties
        newContactDTO.setFirstName("Angel Manica");
        newContactDTO.setLastName("Raquel");
        newContactDTO.setEmail("added dummy email");
        newContactDTO.setCode("added New Code");
        newContactDTO.setCreatedBy(1);
        newContactDTO.setCreatedDate(new Date());
        newContactDTO.setModifiedBy(1);
        newContactDTO.setModifiedDate(new Date());
        newContactDTO.setOrganizationId(1);
        newContactDTO.getRoles().add(1);
        newContactDTO.getRoles().add(2);

        //Set up the POST request to create the contact
        PayloadEnvelope<ContactDTO> payloadEnvelope = new PayloadEnvelope<>(newContactDTO, GobiiProcessType.CREATE);
        RestResource<ContactDTO> restResource = new RestResource<>(DtoRequestContactTest.uriFactory.contacts());
        PayloadEnvelope<ContactDTO> contactDTOResponseEnvelope = restResource.post(ContactDTO.class,
                payloadEnvelope);


        Assert.assertNotEquals(null, contactDTOResponseEnvelope);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(contactDTOResponseEnvelope.getHeader()));

        ContactDTO contactDTOResponse = contactDTOResponseEnvelope.getPayload().getData().get(0);
        Assert.assertNotEquals(null, contactDTOResponse);
        Assert.assertTrue(contactDTOResponse.getContactId() > 0);

        //Now re-retrieve with the link we got back
        Assert.assertNotNull(contactDTOResponseEnvelope.getPayload().getLinkCollection());
        Assert.assertNotNull(contactDTOResponseEnvelope.getPayload().getLinkCollection().getLinksPerDataItem());
        Assert.assertNotNull(contactDTOResponseEnvelope.getPayload().getLinkCollection().getLinksPerDataItem().get(0));

        // The name of the game is for the server to be able to create these links
        // with as little code duplication as possible
        Link linkForCreatedItem = contactDTOResponseEnvelope.getPayload().getLinkCollection().getLinksPerDataItem().get(0);


        RestUri restUriContact = DtoRequestContactTest
                .uriFactory
                .RestUriFromUri(linkForCreatedItem.getHref());
        RestResource<ContactDTO> restResourceForReRetrieve = new RestResource<>(restUriContact);
        PayloadEnvelope<ContactDTO> reRetrieveResultEnvelope = restResourceForReRetrieve
                .get(ContactDTO.class);
        Assert.assertNotNull(reRetrieveResultEnvelope);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(reRetrieveResultEnvelope.getHeader()));

        ContactDTO contactDTOReRetrieveResponse = reRetrieveResultEnvelope.getPayload().getData().get(0);
        Assert.assertNotNull(contactDTOReRetrieveResponse);
        Assert.assertTrue(contactDTOReRetrieveResponse.getContactId().equals(contactDTOResponse.getContactId()));

    }



    @Test
    public void getContactsWithHttpGet() throws Exception {

        RestUri restUriContact = DtoRequestContactTest.uriFactory.resourceColl(ServiceRequestId.URL_CONTACTS);
        RestResource<ContactDTO> restResource = new RestResource<>(restUriContact);
        PayloadEnvelope<ContactDTO> resultEnvelope = restResource
                .get(ContactDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        List<ContactDTO> contactDTOList = resultEnvelope.getPayload().getData();
        Assert.assertNotNull(contactDTOList);
        Assert.assertTrue(contactDTOList.size() > 0);
        Assert.assertNotNull(contactDTOList.get(0).getLastName());


        LinkCollection linkCollection = resultEnvelope.getPayload().getLinkCollection();
        Assert.assertTrue(linkCollection.getLinksPerDataItem().size() == contactDTOList.size() );

        List<Integer> itemsToTest = new ArrayList<>();
        if (contactDTOList.size() > 50) {
            itemsToTest = TestUtils.makeListOfIntegersInRange(10, contactDTOList.size());

        } else {
            for (int idx = 0; idx < contactDTOList.size(); idx++) {
                itemsToTest.add(idx);
            }
        }

        for (Integer currentIdx : itemsToTest) {
            ContactDTO currentContactDto = contactDTOList.get(currentIdx);

            Link currentLink = linkCollection.getLinksPerDataItem().get(currentIdx);

            RestUri restUriContactForGetById = DtoRequestContactTest
                    .uriFactory
                    .RestUriFromUri(currentLink.getHref());
            RestResource<ContactDTO> restResourceForGetById = new RestResource<>(restUriContactForGetById);
            PayloadEnvelope<ContactDTO> resultEnvelopeForGetByID = restResourceForGetById
                    .get(ContactDTO.class);
            Assert.assertNotNull(resultEnvelopeForGetByID);
            Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeForGetByID.getHeader()));
            ContactDTO contactDTOFromLink = resultEnvelopeForGetByID.getPayload().getData().get(0);
            Assert.assertTrue(currentContactDto.getLastName().equals(contactDTOFromLink.getLastName()));
            Assert.assertTrue(currentContactDto.getContactId().equals(contactDTOFromLink.getContactId()));
            Assert.assertTrue(currentContactDto.getRoles().size() > 0);
        }

    }
    

}
