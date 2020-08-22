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
@Transactional
public class ContactServiceImpl implements ContactService {

    @Autowired
    private ContactDao contactDao;
    
    @Override
    public PagedResult<ContactDTO> getContacts(Integer page, Integer pageSize,
                                               Integer organizationId) throws Exception {
        try {
            Objects.requireNonNull(page);
            Objects.requireNonNull(pageSize);
            List<ContactDTO> contactDTOs  = new ArrayList<>();

            List<Contact> contacts = contactDao.getContacts(page, pageSize, organizationId);
            contacts.forEach(contact -> {
                ContactDTO contactDTO = new ContactDTO();
                ModelMapper.mapEntityToDto(contact, contactDTO);
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


}