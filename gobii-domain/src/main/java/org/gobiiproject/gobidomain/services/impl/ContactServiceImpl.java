package org.gobiiproject.gobidomain.services.impl;

import org.gobiiproject.gobidomain.services.ContactService;
import org.gobiiproject.gobiidtomapping.DtoMapContact;
import org.gobiiproject.gobiimodel.dto.container.ContactDTO;
import org.gobiiproject.gobiimodel.dto.response.PayloadEnvelope;
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
    public PayloadEnvelope<ContactDTO> processDml(PayloadEnvelope<ContactDTO> payloadEnvelope) {

        PayloadEnvelope<ContactDTO> returnVal = new PayloadEnvelope<>();
        ContactDTO contactDTOToProcess = payloadEnvelope.getPayload().getData().get(0);

        returnVal.getHeader().getStatus().addStatusMessage(Status.StatusLevel.ERROR,
                Status.ValidationStatusType.BAD_REQUEST,
                "Unsupported proces contact type " + payloadEnvelope.getHeader().getProcessType().toString());
//        try {
//            switch (payloadEnvelope.getHeader().getProcessType()) {
//
//                case UPDATE:
//                    contactDTOToProcess.setCreatedDate(new Date());
//                    contactDTOToProcess.setModifiedDate(new Date());
//                    contactDTOToProcess = dtoMapContact.replaceContact(contactDTOToProcess);
//                    returnVal.getPayload().getData().add(contactDTOToProcess);
//                    break;
//
//                default:
//                    returnVal.getHeader().getStatus().addStatusMessage(Status.StatusLevel.ERROR,
//                            Status.ValidationStatusType.BAD_REQUEST,
//                            "Unsupported proces contact type " + payloadEnvelope.getHeader().getProcessType().toString());
//
//            }
//
//        } catch (Exception e) {
//
//            returnVal.getHeader().getStatus().addException(e);
//            LOGGER.error("Gobii service error", e);
//        }

        return returnVal;
    }

    @Override
    public PayloadEnvelope<ContactDTO> createContact(PayloadEnvelope<ContactDTO> payloadEnvelope) {

        PayloadEnvelope<ContactDTO> returnVal = new PayloadEnvelope<>();

        try {

            ContactDTO contactDTOToProcess = payloadEnvelope.getPayload().getData().get(0);
            contactDTOToProcess = dtoMapContact.createContact(contactDTOToProcess);
            returnVal.getPayload().getData().add(contactDTOToProcess);

        } catch (Exception e) {

            returnVal.getHeader().getStatus().addException(e);
            LOGGER.error("Gobii service error", e);
        }


        return returnVal;
    }

    @Override
    public PayloadEnvelope<ContactDTO> replaceContact(Integer contactId, PayloadEnvelope<ContactDTO> payloadEnvelope) {

        PayloadEnvelope<ContactDTO> returnVal = new PayloadEnvelope<>();

        try {

            ContactDTO contactDTOToProcess = payloadEnvelope.getPayload().getData().get(0);

            if (null == contactDTOToProcess.getContactId() ||
                    contactDTOToProcess.getContactId().equals(contactId)) {

                ContactDTO existingContactDTO = dtoMapContact.getContactDetails(contactId);
                if( null != existingContactDTO.getContactId() && existingContactDTO.getContactId().equals(contactId)) {


                    contactDTOToProcess = dtoMapContact.replaceContact(contactId, contactDTOToProcess);
                    returnVal.getPayload().getData().add(contactDTOToProcess);

                } else {
                    returnVal.getHeader().getStatus().addStatusMessage(Status.StatusLevel.VALIDATION,
                            Status.ValidationStatusType.BAD_REQUEST,
                            "The specified contactId ("
                                    + contactId
                                    + ") does not match an existing contact ");

                }

            } else {
                returnVal.getHeader().getStatus().addStatusMessage(Status.StatusLevel.VALIDATION,
                        Status.ValidationStatusType.BAD_REQUEST,
                        "The contactId specified in the payload ("
                                + contactDTOToProcess.getContactId()
                                + ") does not match the contactId passed as a parameter "
                                + "("
                                + contactId
                                + ")");
            }


        } catch (Exception e) {

            returnVal.getHeader().getStatus().addException(e);
            LOGGER.error("Gobii service error", e);
        }


        return returnVal;
    }

    @Override
    public PayloadEnvelope<ContactDTO> getContactById(Integer contactId) {

        PayloadEnvelope<ContactDTO> returnVal = new PayloadEnvelope<>();

        try {
            ContactDTO contactDTO = dtoMapContact.getContactDetails(contactId);
            returnVal.getPayload().getData().add(contactDTO);

        } catch (Exception e) {

            returnVal.getHeader().getStatus().addException(e);

        }
        return returnVal;
    }

    @Override
    public PayloadEnvelope<ContactDTO> getContactByEmail(String email) {
        PayloadEnvelope<ContactDTO> returnVal = new PayloadEnvelope<>();
        try {
            ContactDTO contactDTO = dtoMapContact.getContactByEmail(email);
            returnVal.getPayload().getData().add(contactDTO);

        } catch (Exception e) {

            returnVal.getHeader().getStatus().addException(e);

        }


        return returnVal;
    }

    @Override
    public PayloadEnvelope<ContactDTO> getContactByLastName(String lastName) {
        return null;
    }

    @Override
    public PayloadEnvelope<ContactDTO> getContactByFirstName(String email, String lastName, String firstName) {
        return null;
    }
}