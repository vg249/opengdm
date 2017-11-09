package org.gobiiproject.gobiidtomapping.entity.auditable;

import org.gobiiproject.gobiidtomapping.entity.auditable.DtoMap;
import org.gobiiproject.gobiidtomapping.core.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.dto.entity.auditable.ContactDTO;

import java.util.List;

/**
 * Created by Anggel on 5/4/2016.
 */
public interface DtoMapContact extends DtoMap<ContactDTO> {

    ContactDTO create(ContactDTO contactDTO) throws GobiiDtoMappingException;
    ContactDTO replace(Integer contactId, ContactDTO contactDTO) throws GobiiDtoMappingException;
    ContactDTO get(Integer contactId) throws GobiiDtoMappingException;
    List<ContactDTO> getList() throws GobiiDtoMappingException;

    ContactDTO getByEmail(String email) throws GobiiDtoMappingException;
    ContactDTO getByUserName(String email) throws GobiiDtoMappingException;

}
