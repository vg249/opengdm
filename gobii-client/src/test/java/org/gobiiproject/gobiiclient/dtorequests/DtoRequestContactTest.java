// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests;


import org.gobiiproject.gobiiclient.core.restmethods.RestResource;
import org.gobiiproject.gobiiclient.core.restmethods.RestUri;
import org.gobiiproject.gobiiclient.core.restmethods.UriFactory;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.Authenticator;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.EntityParamValues;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestDtoFactory;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.dto.response.Header;
import org.gobiiproject.gobiimodel.dto.container.ContactDTO;
import org.gobiiproject.gobiimodel.dto.response.PayloadEnvelope;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Date;
import java.util.UUID;

public class DtoRequestContactTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
        Assert.assertTrue(Authenticator.authenticate());
    }

    @AfterClass
    public static void tearDownUpClass() throws Exception {
        Assert.assertTrue(Authenticator.deAuthenticate());
    }


    @Test
    public void testGetContact() throws Exception {

        RestUri restUriContact = UriFactory.contactByContactUriParam();
        restUriContact.setParamValue("contactId", "6");
        RestResource<ContactDTO> restResource = new RestResource<>(restUriContact);
        PayloadEnvelope<ContactDTO> resultEnvelope = restResource
                .get(ContactDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        ContactDTO contactDTO = resultEnvelope.getPayload().getData().get(0);
        Assert.assertTrue(contactDTO.getContactId() > 0);
        Assert.assertNotNull(contactDTO.getEmail());
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
//        PayloadEnvelope<ContactDTO> contactDTOResponseEnvelope = dtoRequestContact.process(new PayloadEnvelope<>(contactDTORequest, Header.ProcessType.CREATE));
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
                .makePopulatedContactDTO(Header.ProcessType.CREATE, 1);

        RestResource<ContactDTO> restResourceContacts = new RestResource<>(UriFactory.contacts());


        PayloadEnvelope<ContactDTO> resultEnvelopeNewContact = restResourceContacts.post(ContactDTO.class,
                new PayloadEnvelope<>(newContactDto, Header.ProcessType.CREATE));
        //PayloadEnvelope<ContactDTO> resultEnvelopeNewContact = dtoRequestContact.process(new PayloadEnvelope<>(newContactDto, Header.ProcessType.CREATE));

        ContactDTO newContactDTOResponse = resultEnvelopeNewContact.getPayload().getData().get(0);


        RestUri restUriContact = UriFactory.contactByContactUriParam();
        restUriContact.setParamValue("contactId", newContactDTOResponse.getContactId().toString());
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

        DtoRequestContact dtoRequestContact = new DtoRequestContact();
        PayloadEnvelope<ContactDTO> contactDTOResponseEnvelopeUpdate = dtoRequestContact.process(new PayloadEnvelope<>(contactDTOReceived, Header.ProcessType.UPDATE));
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(contactDTOResponseEnvelopeUpdate.getHeader()));


        RestUri restUriContactReRetrive = UriFactory.contactByContactUriParam();
        restUriContactReRetrive.setParamValue("contactId", contactDTOReceived.getContactId().toString());
        RestResource<ContactDTO> restResourceReRetrieve = new RestResource<>(restUriContactReRetrive);
        PayloadEnvelope<ContactDTO> contactDTOResponseEnvelopeReRetrieved = restResourceReRetrieve
                .get(ContactDTO.class);


        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(contactDTOResponseEnvelopeReRetrieved.getHeader()));
        ContactDTO dtoRequestContactReRetrieved =
                contactDTOResponseEnvelopeReRetrieved.getPayload().getData().get(0);

        Assert.assertTrue(dtoRequestContactReRetrieved.getLastName().equals(newName));

    }

    @Test
    public void getSingleContactWithHttpGet() throws Exception {

        RestUri restUriContact = UriFactory.contactByContactUriParam();
        restUriContact.setParamValue("contactId", "1");
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

        RestUri restUriContact = UriFactory.contactsByQueryParams();
        restUriContact.setParamValue("email", "loader.user@temp.com");
        RestResource<ContactDTO> restResource = new RestResource<>(restUriContact);
        PayloadEnvelope<ContactDTO> resultEnvelope = restResource
                .get(ContactDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        ContactDTO contactDTO = resultEnvelope.getPayload().getData().get(0);
        Assert.assertNotNull(contactDTO.getEmail());

        //restUriContact.setParamValue(Param);
    }

    @Test
    public void testCreateContactWithHttpPost() throws Exception {



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

        PayloadEnvelope<ContactDTO> payloadEnvelope =  new PayloadEnvelope<>(newContactDTO, Header.ProcessType.CREATE);
        RestResource<ContactDTO> restResource = new RestResource<>(UriFactory.contacts());
        PayloadEnvelope<ContactDTO> contactDTOResponseEnvelope = restResource.post(ContactDTO.class,
                payloadEnvelope);


        Assert.assertNotEquals(null, contactDTOResponseEnvelope);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(contactDTOResponseEnvelope.getHeader()));

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(contactDTOResponseEnvelope.getHeader()));

        ContactDTO contactDTOResponse = contactDTOResponseEnvelope.getPayload().getData().get(0);
        Assert.assertNotEquals(null, contactDTOResponse);
        Assert.assertTrue(contactDTOResponse.getContactId() > 0);
    }

}
