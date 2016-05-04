// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests;


import org.gobiiproject.gobiiclient.dtorequests.DtoRequestDataSet;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestDtoFactory;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.dto.DtoMetaData;
import org.gobiiproject.gobiimodel.dto.container.ContactDTO;
import org.gobiiproject.gobiimodel.dto.container.DataSetDTO;
import org.junit.Assert;
import org.junit.Test;

import java.awt.event.ContainerAdapter;
import java.util.Date;

public class DtoRequestContactTest {


    @Test
    public void testGetContact() throws Exception {

        DtoRequestContact dtoRequestContact = new DtoRequestContact();
        ContactDTO contactDTORequest = new ContactDTO();
        contactDTORequest.setContactId(6);
        ContactDTO contactDTOResponse = dtoRequestContact.processContact(contactDTORequest);

        Assert.assertNotEquals(null, contactDTOResponse);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(contactDTOResponse));

    } //


    @Test
    public void testCreateContact() throws Exception {

        DtoRequestContact dtoRequestContact = new DtoRequestContact();
        ContactDTO contactDTORequest = new ContactDTO(DtoMetaData.ProcessType.CREATE);

        // set the plain properties
        contactDTORequest.setFirstName("Angel Manica");
        contactDTORequest.setLastName("Raquel");
        contactDTORequest.setEmail("added dummy email");
        contactDTORequest.setCode("added New Code");
        contactDTORequest.setCreatedBy(1);
        contactDTORequest.setCreatedDate(new Date());
        contactDTORequest.setModifiedBy(1);
        contactDTORequest.setModifiedDate(new Date());

        contactDTORequest.getRoles().add(1);
        contactDTORequest.getRoles().add(2);
        ContactDTO contactDTOResponse = dtoRequestContact.processContact(contactDTORequest);

        Assert.assertNotEquals(null, contactDTOResponse);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(contactDTOResponse));
        Assert.assertTrue(contactDTOResponse.getContactId() > 0);

    }


}
