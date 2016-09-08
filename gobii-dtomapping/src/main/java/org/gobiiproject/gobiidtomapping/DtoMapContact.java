package org.gobiiproject.gobiidtomapping;

import org.gobiiproject.gobiimodel.dto.container.ContactDTO;
import org.gobiiproject.gobiimodel.dto.response.RequestEnvelope;
import org.gobiiproject.gobiimodel.dto.response.ResultEnvelope;

/**
 * Created by Anggel on 5/4/2016.
 */
public interface DtoMapContact {

    ContactDTO getContactDetails(Integer contactId) throws Exception;
    ContactDTO getContactByEmail(String email) throws Exception;
    ContactDTO createContact(ContactDTO contactDTO) throws Exception;
    ContactDTO updateContact(ContactDTO contactDTO) throws Exception;

}
