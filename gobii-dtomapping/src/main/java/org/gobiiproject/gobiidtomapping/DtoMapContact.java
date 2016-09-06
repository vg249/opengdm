package org.gobiiproject.gobiidtomapping;

import org.gobiiproject.gobiimodel.dto.container.ContactDTO;
import org.gobiiproject.gobiimodel.dto.response.ResultEnvelope;

/**
 * Created by Anggel on 5/4/2016.
 */
public interface DtoMapContact {

    ResultEnvelope<ContactDTO> getContactDetails(ContactDTO contactDTO) throws GobiiDtoMappingException;
    ResultEnvelope<ContactDTO> createContact(ContactDTO contactDTO) throws GobiiDtoMappingException;
    ResultEnvelope<ContactDTO> updateContact(ContactDTO contactDTO) throws GobiiDtoMappingException;

}
