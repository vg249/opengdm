package org.gobiiproject.gobidomain.services.gdmv3;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.transaction.Transactional;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.gdmv3.ContactDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;
import org.gobiiproject.gobiimodel.entity.Contact;
import org.gobiiproject.gobiimodel.modelmapper.ModelMapper;
import org.gobiiproject.gobiisampletrackingdao.ContactDao;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ContactServiceImpl implements ContactService {

    @Autowired
    private ContactDao contactDao;
    
    @Transactional
    @Override
    public PagedResult<ContactDTO> getContacts(Integer page, Integer pageSize, Integer organizationId) throws Exception {
        
        try {
            Objects.requireNonNull(page);
            Objects.requireNonNull(pageSize);
            List<ContactDTO> contactDTOs  = new ArrayList<>();

            List<Contact> contacts = contactDao.getContacts(page, pageSize, organizationId);
            contacts.forEach(contact -> {
                ContactDTO contactDTO = getContactDTO(contact);
                contactDTOs.add(contactDTO);
            });

            return PagedResult.createFrom(page, contactDTOs);
        } catch (GobiiException gE) {
            throw gE;
        } catch (Exception e) {
            log.error("Gobii service error", e);
            throw new GobiiDomainException(e);
        }
    }

    @Transactional
    @Override
    public ContactDTO addContact(String preferredUsername, String givenName, String familyName, String email)
            throws Exception {
        Contact contact = contactDao.getContactByUsername(preferredUsername);
        if (contact == null) {
            //create a new one
            contact = new Contact();
            contact.setLastName(familyName);
            contact.setFirstName(givenName);
            contact.setEmail(email);
            contact.setUsername(preferredUsername);
            contact.setCode(String.format("contact_keycloak_%s", preferredUsername));
            contact.setCreatedBy(1);
            contact.setCreatedDate(new java.util.Date());
            contactDao.addContact(contact);     
        }

        return getContactDTO(contact);
    }

    private ContactDTO getContactDTO(Contact contact) {
        ContactDTO contactDTO=  new ContactDTO();
        ModelMapper.mapEntityToDto(contact, contactDTO);
        return contactDTO;
    }

}