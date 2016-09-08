package org.gobiiproject.gobidomain.services.impl;

import javassist.bytecode.stackmap.BasicBlock;
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
    public ResultEnvelope<ContactDTO> processDml(RequestEnvelope<ContactDTO> requestEnvelope) {

        ResultEnvelope<ContactDTO> returnVal = new ResultEnvelope<>();
        ContactDTO contactDTOToProcess = requestEnvelope.getRequestData();

        try {
            switch (requestEnvelope.getHeader().getProcessType()) {

                case CREATE:
                    contactDTOToProcess.setCreatedDate(new Date());
                    contactDTOToProcess.setModifiedDate(new Date());
                    contactDTOToProcess = dtoMapContact.createContact(contactDTOToProcess);
                    returnVal.getResult().getData().add(contactDTOToProcess);
                    break;

                case UPDATE:
                    contactDTOToProcess.setCreatedDate(new Date());
                    contactDTOToProcess.setModifiedDate(new Date());
                    contactDTOToProcess = dtoMapContact.updateContact(contactDTOToProcess);
                    returnVal.getResult().getData().add(contactDTOToProcess);
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

    @Override
    public ResultEnvelope<ContactDTO> getContactById(Integer contactId) {

        ResultEnvelope<ContactDTO> returnVal = new ResultEnvelope<>();

        try {
            ContactDTO contactDTO = dtoMapContact.getContactDetails(contactId);
            returnVal.getResult().getData().add(contactDTO);

        } catch(Exception e) {

            returnVal.getHeader().getStatus().addException(e);

        }
        return returnVal;
    }

    @Override
    public ResultEnvelope<ContactDTO> getContactByEmail(String email) {
        return null;
    }

    @Override
    public ResultEnvelope<ContactDTO> getContactByLastName(String lastName) {
        return null;
    }

    @Override
    public ResultEnvelope<ContactDTO> getContactByFirstName(String email, String lastName, String firstName) {
        return null;
    }
}