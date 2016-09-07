package org.gobiiproject.gobidomain.services.impl;

import org.gobiiproject.gobidomain.services.ContactService;
import org.gobiiproject.gobiidtomapping.DtoMapContact;
import org.gobiiproject.gobiimodel.dto.container.ContactDTO;
import org.gobiiproject.gobiimodel.dto.response.RequestEnvelope;
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
    public ResultEnvelope<ContactDTO> processContact(RequestEnvelope<ContactDTO> requestEnvelope) {

        ResultEnvelope<ContactDTO> returnVal = new ResultEnvelope<>();

        try {
            switch (requestEnvelope.getHeader().getProcessType()) {
                case READ:
                    returnVal = dtoMapContact.getContactDetails(requestEnvelope);
                    break;

                case CREATE:
                    requestEnvelope.getRequestData().setCreatedDate(new Date());
                    requestEnvelope.getRequestData().setModifiedDate(new Date());
                    returnVal = dtoMapContact.createContact(requestEnvelope);
                    break;

                case UPDATE:
                    requestEnvelope.getRequestData().setCreatedDate(new Date());
                    requestEnvelope.getRequestData().setModifiedDate(new Date());
                    returnVal = dtoMapContact.updateContact(requestEnvelope);
                    break;

                default:
                    returnVal.getHeader().getStatus().addStatusMessage(Status.StatusLevel.ERROR,
                            Status.ValidationStatusType.BAD_REQUEST,
                            "Unsupported proces contact type " + requestEnvelope.getHeader().getProcessType().toString());

            }

        } catch (Exception e) {

            returnVal.getHeader().getStatus().addException(e);
            LOGGER.error("Gobii service error", e);
        }

        return returnVal;
    }
}