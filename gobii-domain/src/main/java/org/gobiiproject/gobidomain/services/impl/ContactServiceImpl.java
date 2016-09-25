package org.gobiiproject.gobidomain.services.impl;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobidomain.services.ContactService;
import org.gobiiproject.gobiidtomapping.DtoMapContact;
import org.gobiiproject.gobiimodel.dto.container.ContactDTO;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
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
    public ContactDTO createContact(ContactDTO contactDTO) throws GobiiDomainException {

        ContactDTO returnVal;

        try {

            returnVal = dtoMapContact.createContact(contactDTO);

        } catch (Exception e) {

            LOGGER.error("Gobii service error", e);
            throw new GobiiDomainException(e);
        }
        return returnVal;
    }

    @Override
    public ContactDTO replaceContact(Integer contactId, ContactDTO contactDTO) throws GobiiDomainException {

        ContactDTO returnVal;

        try {

            if (null == contactDTO.getContactId() ||
                    contactDTO.getContactId().equals(contactId)) {


                ContactDTO existingContactDTO = dtoMapContact.getContactDetails(contactId);
                if (null != existingContactDTO.getContactId() && existingContactDTO.getContactId().equals(contactId)) {


                    returnVal = dtoMapContact.replaceContact(contactId, contactDTO);

                } else {

                    throw new GobiiDomainException(GobiiStatusLevel.VALIDATION,
                            GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                            "The specified contactId ("
                                    + contactId
                                    + ") does not match an existing contact ");
                }

            } else {

                throw new GobiiDomainException(GobiiStatusLevel.VALIDATION,
                        GobiiValidationStatusType.BAD_REQUEST,
                        "The contactId specified in the dto ("
                                + contactDTO.getContactId()
                                + ") does not match the contactId passed as a parameter "
                                + "("
                                + contactId
                                + ")");

            }


        } catch (Exception e) {

            LOGGER.error("Gobii service error", e);
            throw new GobiiDomainException(e);
        }


        return returnVal;
    }

    @Override
    public ContactDTO getContactById(Integer contactId) throws GobiiDomainException {

        ContactDTO returnVal;

        try {
            returnVal = dtoMapContact.getContactDetails(contactId);

            if (null == returnVal) {
                throw new GobiiDomainException(GobiiStatusLevel.VALIDATION,
                        GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                        "The specified contactId ("
                                + contactId
                                + ") does not match an existing contact ");
            }

        } catch (Exception e) {

            LOGGER.error("Gobii service error", e);
            throw new GobiiDomainException(e);

        }

        return returnVal;
    }

    @Override
    public ContactDTO getContactByEmail(String email) {
        ContactDTO returnVal;
        try {
            returnVal = dtoMapContact.getContactByEmail(email);

        } catch (Exception e) {

            LOGGER.error("Gobii service error", e);
            throw new GobiiDomainException(e);

        }

        return returnVal;
    }

    @Override
    public ContactDTO getContactByLastName(String lastName) {
        return null;
    }

    @Override
    public ContactDTO getContactByFirstName(String email, String lastName, String firstName) {
        return null;
    }
}