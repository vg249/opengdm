package org.gobiiproject.gobidomain.services.impl;

import org.gobiiproject.gobidomain.services.ContactService;
import org.gobiiproject.gobiidtomapping.DtoMapContact;
import org.gobiiproject.gobiimodel.dto.container.ContactDTO;
import org.gobiiproject.gobiimodel.dto.response.ResultEnvelope;
import org.gobiiproject.gobiimodel.dto.response.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * Created by Angel on 5/4/2016.
 */
public class ContactServiceImpl implements ContactService {

    Logger LOGGER = LoggerFactory.getLogger(ContactServiceImpl.class);

    @Autowired
    DtoMapContact dtoMapContact = null;

    @Override
    public ResultEnvelope<ContactDTO> processContact(ContactDTO contactDTO) {

        ResultEnvelope<ContactDTO> returnVal = new ResultEnvelope<>();

        try {
            switch (contactDTO.getProcessType()) {
                case READ:
                    returnVal = dtoMapContact.getContactDetails(contactDTO);
                    break;

                case CREATE:
                    contactDTO.setCreatedDate(new Date());
                    contactDTO.setModifiedDate(new Date());
                    returnVal = dtoMapContact.createContact(contactDTO);
                    break;

                case UPDATE:
                    contactDTO.setCreatedDate(new Date());
                    contactDTO.setModifiedDate(new Date());
                    returnVal = dtoMapContact.updateContact(contactDTO);
                    break;

                default:
                    returnVal.getHeader().getStatus().addStatusMessage(Status.StatusLevel.ERROR,
                            Status.ValidationStatusType.BAD_REQUEST,
                            "Unsupported proces contact type " + contactDTO.getProcessType().toString());

            }

        } catch (Exception e) {

            returnVal.getHeader().getStatus().addException(e);
            LOGGER.error("Gobii service error", e);
        }

        return returnVal;
    }
}