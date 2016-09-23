package org.gobiiproject.gobiidtomapping;

import org.gobiiproject.gobiimodel.dto.container.ContactDTO;

/**
 * Created by Anggel on 5/4/2016.
 */
public interface DtoMapContact {

    ContactDTO getContactDetails(Integer contactId) throws Exception;
    ContactDTO getContactByEmail(String email) throws Exception;
    ContactDTO createContact(ContactDTO contactDTO) throws Exception;
    ContactDTO replaceContact(Integer contactId, ContactDTO contactDTO) throws Exception;

}
