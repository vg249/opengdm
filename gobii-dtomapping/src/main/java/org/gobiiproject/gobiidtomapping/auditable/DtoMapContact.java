package org.gobiiproject.gobiidtomapping.auditable;

import org.gobiiproject.gobiidtomapping.DtoMap;
import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.headerlesscontainer.ContactDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.PlatformDTO;

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
