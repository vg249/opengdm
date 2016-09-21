// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests;


import org.gobiiproject.gobiiclient.core.restmethods.get.EnvelopeGetRequestProcessor;
import org.gobiiproject.gobiiclient.core.restmethods.get.RestUrl;
import org.gobiiproject.gobiiclient.core.restmethods.get.GetRequestFactory;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.Authenticator;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.EntityParamValues;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestDtoFactory;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.dto.response.Header;
import org.gobiiproject.gobiimodel.dto.container.ContactDTO;
import org.gobiiproject.gobiimodel.dto.response.RequestEnvelope;
import org.gobiiproject.gobiimodel.dto.response.ResultEnvelope;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
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

        RestUrl restUrlContact = GetRequestFactory.makeGetRequestContactById();
        restUrlContact.setParamValue("contactId", "6");
        EnvelopeGetRequestProcessor<ContactDTO> envelopeGetRequestProcessor = new EnvelopeGetRequestProcessor<>();
        ResultEnvelope<ContactDTO> resultEnvelope = envelopeGetRequestProcessor
                .processGetRequest(restUrlContact,ContactDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        ContactDTO contactDTO = resultEnvelope.getResult().getData().get(0);
        Assert.assertTrue(contactDTO.getContactId() > 0);
        Assert.assertNotNull(contactDTO.getEmail());
    } //


    @Test
    public void testCreateContact() throws Exception {

        DtoRequestContact dtoRequestContact = new DtoRequestContact();
        ContactDTO contactDTORequest = new ContactDTO();

        // set the plain properties
        contactDTORequest.setFirstName("Angel Manica");
        contactDTORequest.setLastName("Raquel");
        contactDTORequest.setEmail("added dummy email");
        contactDTORequest.setCode("added New Code");
        contactDTORequest.setCreatedBy(1);
        contactDTORequest.setCreatedDate(new Date());
        contactDTORequest.setModifiedBy(1);
        contactDTORequest.setModifiedDate(new Date());
        contactDTORequest.setOrganizationId(1);
        contactDTORequest.getRoles().add(1);
        contactDTORequest.getRoles().add(2);

        ResultEnvelope<ContactDTO> contactDTOResponseEnvelope = dtoRequestContact.process(new RequestEnvelope<>(contactDTORequest, Header.ProcessType.CREATE));
        Assert.assertNotEquals(null, contactDTOResponseEnvelope);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(contactDTOResponseEnvelope.getHeader()));

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(contactDTOResponseEnvelope.getHeader()));

        ContactDTO contactDTOResponse = contactDTOResponseEnvelope.getResult().getData().get(0);
        Assert.assertNotEquals(null, contactDTOResponse);
        Assert.assertTrue(contactDTOResponse.getContactId() > 0);

    }

    @Test
    public void testUpdateContact() throws Exception {

        DtoRequestContact dtoRequestContact = new DtoRequestContact();

        // create a new contact for our test
        EntityParamValues entityParamValues = TestDtoFactory.makeArbitraryEntityParams();
        ContactDTO newContactDto = TestDtoFactory
                .makePopulatedContactDTO(Header.ProcessType.CREATE, 1);


        ResultEnvelope<ContactDTO> resultEnvelopeNewContact = dtoRequestContact.process(new RequestEnvelope<>(newContactDto, Header.ProcessType.CREATE));

        ContactDTO newContactDTOResponse = resultEnvelopeNewContact.getResult().getData().get(0);


        RestUrl restUrlContact = GetRequestFactory.makeGetRequestContactById();
        restUrlContact.setParamValue("contactId", newContactDTOResponse.getContactId().toString());
        EnvelopeGetRequestProcessor<ContactDTO> envelopeGetRequestProcessor = new EnvelopeGetRequestProcessor<>();
        ResultEnvelope<ContactDTO> contactDTOResponseEnvelope = envelopeGetRequestProcessor
                .processGetRequest(restUrlContact,ContactDTO.class);

        Assert.assertNotEquals(null, contactDTOResponseEnvelope);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(contactDTOResponseEnvelope.getHeader()));
        ContactDTO contactDTOReceived = contactDTOResponseEnvelope.getResult().getData().get(0);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(contactDTOResponseEnvelope.getHeader()));


        // so this would be the typical workflow for the client app

        String newName = UUID.randomUUID().toString();
        contactDTOReceived.setLastName(newName);

        ResultEnvelope<ContactDTO> contactDTOResponseEnvelopeUpdate = dtoRequestContact.process(new RequestEnvelope<>(contactDTOReceived, Header.ProcessType.UPDATE));
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(contactDTOResponseEnvelopeUpdate.getHeader()));


        RestUrl restUrlContactReRetrive = GetRequestFactory.makeGetRequestContactById();
        restUrlContactReRetrive.setParamValue("contactId", contactDTOReceived.getContactId().toString());
        EnvelopeGetRequestProcessor<ContactDTO> envelopeGetRequestProcessorReRetrieve = new EnvelopeGetRequestProcessor<>();
        ResultEnvelope<ContactDTO> contactDTOResponseEnvelopeReRetrieved = envelopeGetRequestProcessorReRetrieve
                .processGetRequest(restUrlContactReRetrive,ContactDTO.class);


        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(contactDTOResponseEnvelopeReRetrieved.getHeader()));
        ContactDTO dtoRequestContactReRetrieved =
                contactDTOResponseEnvelopeReRetrieved.getResult().getData().get(0);

        Assert.assertTrue(dtoRequestContactReRetrieved.getLastName().equals(newName));

    }

    @Test
    public void getSingleContactWithHttpGet() throws Exception {

        RestUrl restUrlContact = GetRequestFactory.makeGetRequestContactById();
        restUrlContact.setParamValue("contactId", "1");
        EnvelopeGetRequestProcessor<ContactDTO> envelopeGetRequestProcessor = new EnvelopeGetRequestProcessor<>();
        ResultEnvelope<ContactDTO> resultEnvelope = envelopeGetRequestProcessor
                .processGetRequest(restUrlContact,ContactDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        ContactDTO contactDTO = resultEnvelope.getResult().getData().get(0);
        Assert.assertNotNull(contactDTO.getEmail());

        //restUrlContact.setParamValue(Param);
    }

    @Test
    public void getContactsBySearchWithHttpGet() throws Exception {

        RestUrl restUrlContact = GetRequestFactory.makeGetRequestContactBySearch();
        restUrlContact.setParamValue("email", "loader.user@temp.com");
        EnvelopeGetRequestProcessor<ContactDTO> envelopeGetRequestProcessor = new EnvelopeGetRequestProcessor<>();
        ResultEnvelope<ContactDTO> resultEnvelope = envelopeGetRequestProcessor
                .processGetRequest(restUrlContact,ContactDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        ContactDTO contactDTO = resultEnvelope.getResult().getData().get(0);
        Assert.assertNotNull(contactDTO.getEmail());

        //restUrlContact.setParamValue(Param);
    }

}
