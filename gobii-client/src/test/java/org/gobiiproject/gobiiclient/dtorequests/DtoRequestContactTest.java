// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests;


import org.gobiiproject.gobiiclient.dtorequests.Helpers.Authenticator;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.EntityParamValues;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestDtoFactory;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.dto.response.Header;
import org.gobiiproject.gobiimodel.dto.container.ContactDTO;
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

        DtoRequestContact dtoRequestContact = new DtoRequestContact();
        ContactDTO contactDTORequest = new ContactDTO();
        contactDTORequest.setContactId(6);
        ResultEnvelope<ContactDTO> contactDTOResponseEnvelope = dtoRequestContact.process(contactDTORequest);

        Assert.assertNotEquals(null, contactDTOResponseEnvelope);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(contactDTOResponseEnvelope.getHeader()));
        ContactDTO contactDTOResponse = contactDTOResponseEnvelope.getResult().getData().get(0);

        Assert.assertTrue(contactDTOResponse.getContactId() > 0);

    } //


    @Test
    public void testCreateContact() throws Exception {

        DtoRequestContact dtoRequestContact = new DtoRequestContact();
        ContactDTO contactDTORequest = new ContactDTO(Header.ProcessType.CREATE);

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

        ResultEnvelope<ContactDTO> contactDTOResponseEnvelope = dtoRequestContact.process(contactDTORequest);
        Assert.assertNotEquals(null, contactDTOResponseEnvelope);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(contactDTOResponseEnvelope.getHeader()));
        ContactDTO contactDTOResponse = contactDTOResponseEnvelope.getResult().getData().get(0);


        Assert.assertNotEquals(null, contactDTOResponse);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(contactDTOResponse));
        Assert.assertTrue(contactDTOResponse.getContactId() > 0);

    }

    @Test
    public void testUpdateContact() throws Exception {

        DtoRequestContact dtoRequestContact = new DtoRequestContact();

        // create a new contact for our test
        EntityParamValues entityParamValues = TestDtoFactory.makeArbitraryEntityParams();
        ContactDTO newContactDto = TestDtoFactory
                .makePopulatedContactDTO(Header.ProcessType.CREATE, 1);


        ResultEnvelope<ContactDTO> resultEnvelopeNewContact = dtoRequestContact.process(newContactDto);

        ContactDTO newContactDTOResponse = resultEnvelopeNewContact.getResult().getData().get(0);


        // re-retrieve the contact we just created so we start with a fresh READ mode dto
        ContactDTO ContactDTORequest = new ContactDTO();
        ContactDTORequest.setContactId(newContactDTOResponse.getContactId());

        ResultEnvelope<ContactDTO> contactDTOResponseEnvelope = dtoRequestContact.process(newContactDTOResponse);
        Assert.assertNotEquals(null, contactDTOResponseEnvelope);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(contactDTOResponseEnvelope.getHeader()));
        ContactDTO contactDTOReceived = contactDTOResponseEnvelope.getResult().getData().get(0);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(contactDTOReceived));


        // so this would be the typical workflow for the client app
        contactDTOReceived.setProcessType(Header.ProcessType.UPDATE);
        String newName = UUID.randomUUID().toString();
        contactDTOReceived.setLastName(newName);

        ResultEnvelope<ContactDTO> contactDTOResponseEnvelopeUpdate = dtoRequestContact.process(contactDTOReceived);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(contactDTOResponseEnvelopeUpdate.getHeader()));


        ContactDTO contactDTOReRequest = new ContactDTO(Header.ProcessType.READ);
        contactDTOReRequest.setContactId(contactDTOReceived.getContactId());
        ResultEnvelope<ContactDTO> contactDTOResponseEnvelopeReRetrieved = dtoRequestContact.process(contactDTOReRequest);
        ContactDTO dtoRequestContactReRetrieved =
                contactDTOResponseEnvelopeReRetrieved.getResult().getData().get(0);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(dtoRequestContactReRetrieved));

        Assert.assertTrue(dtoRequestContactReRetrieved.getLastName().equals(newName));

    }

}
