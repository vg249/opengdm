package org.gobiiproject.gobiidtomapping.entity.auditable;

import org.gobiiproject.gobiidtomapping.core.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.dto.base.DTOBaseAuditable;

import java.util.List;

/***
 * This class must be implemented by all DtoMap* classes that deal with
 * DTO classes deriving from DTOBaseAuditable -- i.e., those that are
 * used for tables that have the created_date, modified_date, created_by,
 * and modified_by columns. By deriving from this class, and by being in the
 * entity.auditible namespace, such map classes will be appropriately targeted by
 * the DtoMapAspect class, such that the date and user columns will be automatically
 * updated.
 * @param <T> The DTO type
 */
public interface DtoMap<T extends DTOBaseAuditable> {

    T create(T  dto) throws GobiiDtoMappingException;
    T replace(Integer dtoPkId, T dto) throws GobiiDtoMappingException;
    T get(Integer dtoPkId) throws GobiiDtoMappingException;
    List<T> getList() throws GobiiDtoMappingException;

}
