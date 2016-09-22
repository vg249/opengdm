package org.gobiiproject.gobidomain.services;

import org.gobiiproject.gobiimodel.dto.container.ContactDTO;
import org.gobiiproject.gobiimodel.dto.response.PayloadEnvelope;


/**
 * Created by Angel on 5/4/2016.
 */
public interface ContactService {

    PayloadEnvelope<ContactDTO> processDml(PayloadEnvelope<ContactDTO> payloadEnvelope);

    PayloadEnvelope<ContactDTO> getContactById(Integer contactId);
    PayloadEnvelope<ContactDTO> getContactByEmail(String email);
    PayloadEnvelope<ContactDTO> createContact(PayloadEnvelope<ContactDTO> payloadEnvelope);
    PayloadEnvelope<ContactDTO> getContactByLastName(String lastName);
    PayloadEnvelope<ContactDTO> getContactByFirstName(String email, String lastName, String firstName);


}
