package org.gobiiproject.gobidomain.services.impl;

import org.gobiiproject.gobidomain.services.ContactService;
import org.gobiiproject.gobiidtomapping.DtoMapContact;
import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.dto.container.ContactDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Angel on 5/4/2016.
 */
public class ContactServiceImpl implements ContactService {

    Logger LOGGER = LoggerFactory.getLogger(ContactServiceImpl.class);

    @Autowired
    DtoMapContact dtoMapContact = null;

    @Override
    public ContactDTO processContact(ContactDTO contactDTO) {

        ContactDTO returnVal = new ContactDTO();

        try {
            switch (contactDTO.getProcessType()) {
                case READ:
                    returnVal = dtoMapContact.getContactDetails(contactDTO);
                    break;

                case CREATE:
                    returnVal = dtoMapContact.createContact(contactDTO);
                    break;

                default:
                    throw new GobiiDtoMappingException("Unsupported procesCv type " + contactDTO.getProcessType().toString());

            }

        } catch (GobiiDtoMappingException e) {

            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error(e.getMessage());
        }

        return returnVal;
    }
}