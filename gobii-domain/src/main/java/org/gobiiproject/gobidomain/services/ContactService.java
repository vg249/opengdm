package org.gobiiproject.gobidomain.services;

import org.gobiiproject.gobiimodel.dto.container.ContactDTO;
import org.gobiiproject.gobiimodel.dto.response.RequestEnvelope;
import org.gobiiproject.gobiimodel.dto.response.ResultEnvelope;

/**
 * Created by Angel on 5/4/2016.
 */
public interface ContactService {

    ResultEnvelope<ContactDTO> processDml(RequestEnvelope<ContactDTO> requestEnvelope);

    ResultEnvelope<ContactDTO> getContactById(Integer contactId);
    ResultEnvelope<ContactDTO> getContactByEmail(String email);
    ResultEnvelope<ContactDTO> getContactByLastName(String lastName);
    ResultEnvelope<ContactDTO> getContactByFirstName(String email, String lastName, String firstName);


}
