package org.gobiiproject.gobiidtomapping.entity.auditable;

import org.gobiiproject.gobiidtomapping.core.GobiiDtoMappingException;

import java.util.List;

public interface DtoMap<T> {

    T create(T  dto) throws GobiiDtoMappingException;
    T replace(Integer dtoPkId, T dto) throws GobiiDtoMappingException;
    T get(Integer dtoPkId) throws GobiiDtoMappingException;
    List<T> getList() throws GobiiDtoMappingException;

}
